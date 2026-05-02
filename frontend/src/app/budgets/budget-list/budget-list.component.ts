import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule, FormBuilder,
  FormGroup, Validators
} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BudgetStateService } from '../service/budget-state.services';

@Component({
  selector: 'app-budget-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatProgressBarModule,
    MatSnackBarModule
  ],
  templateUrl: './budget-list.component.html',
  styleUrl: './budget-list.component.css'
})
export class BudgetListComponent {

  showForm = false;
  budgetForm: FormGroup;
  budgets: any;
  loading: any;
  error: any;

  categories = [
    'Food & Groceries', 'Transport', 'Entertainment',
    'Utilities', 'Health', 'Shopping', 'Education', 'Other'
  ];

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private budgetStateService: BudgetStateService
  ) {
    this.budgetForm = this.fb.group({
      category: ['', Validators.required],
      spent: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(1)]]
    });
    this.budgets = this.budgetStateService.budgets;
    this.loading = this.budgetStateService.loading;
    this.error = this.budgetStateService.error;
  }



   ngOnInit() {
      this.budgetStateService.getAllBudgets().subscribe({
        error: (message) => {
          this.snackBar.open(message, 'Close',
            { duration: 3000 });
        }
      })
  }

  getProgress(budget: any): number {
    return Math.min((budget.spent / budget.amount) * 100, 100);
  }

  getProgressColor(budget: any): string {
    const pct = this.getProgress(budget);
    if (pct >= 100) return 'warn';
    if (pct >= 75) return 'accent';
    return 'primary';
  }

  getStatusLabel(budget: any): string {
    const pct = this.getProgress(budget);
    if (pct >= 100) return ' Over budget!';
    if (pct >= 75) return ' Almost full';
    return ' On track';
  }

  onSubmit() {
    if (this.budgetForm.invalid) return;
    
    const newBudget = this.budgetForm.value

    
    this.budgetStateService.addBudget(newBudget).subscribe({
      next: () => {
        this.budgets = [...this.budgets, newBudget];
        this.budgetForm.reset();
        this.showForm = false;
        this.snackBar.open('Budget created! ', 'Close',
      { duration: 3000 });
    }})
    
  }

  deleteBudget(id: number) {
    this.budgets = this.budgets.filter((b: { id: number; }) => b.id !== id);
    this.snackBar.open('Budget deleted', 'Close',
      { duration: 2000 });
  }
}