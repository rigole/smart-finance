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

  budgets = [
    {
      id: 1, category: 'Food & Groceries',
      icon: 'https://img.icons8.com/?size=100&id=9671&format=png&color=000000', limit: 500, spent: 320
    },
    {
      id: 2, category: 'Transport',
      icon: 'https://img.icons8.com/?size=100&id=9671&format=png&color=000000', limit: 200, spent: 180
    },
    {
      id: 3, category: 'Entertainment',
      icon: 'https://img.icons8.com/?size=100&id=9671&format=png&color=000000', limit: 100, spent: 45
    },
    {
      id: 4, category: 'Utilities',
      icon: 'https://img.icons8.com/?size=100&id=9671&format=png&color=000000', limit: 150, spent: 150
    },
  ];

  categories = [
    'Food & Groceries', 'Transport', 'Entertainment',
    'Utilities', 'Health', 'Shopping', 'Education', 'Other'
  ];

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar
  ) {
    this.budgetForm = this.fb.group({
      category: ['', Validators.required],
      limit: ['', [Validators.required, Validators.min(1)]]
    });
  }

  getProgress(budget: any): number {
    return Math.min((budget.spent / budget.limit) * 100, 100);
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

    const newBudget = {
      id: this.budgets.length + 1,
      icon: '',
      spent: 0,
      ...this.budgetForm.value
    };

    this.budgets = [...this.budgets, newBudget];
    this.budgetForm.reset();
    this.showForm = false;
    this.snackBar.open('Budget created! ', 'Close',
      { duration: 3000 });
  }

  deleteBudget(id: number) {
    this.budgets = this.budgets.filter(b => b.id !== id);
    this.snackBar.open('Budget deleted', 'Close',
      { duration: 2000 });
  }
}