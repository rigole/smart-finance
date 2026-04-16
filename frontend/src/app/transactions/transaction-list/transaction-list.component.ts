import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatIconModule } from "@angular/material/icon";
import { MatCardModule } from "@angular/material/card";
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { TransactionStateService } from '../service/transaction-state.services';

@Component({
  selector: 'app-transaction-list',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    MatDialogModule,
    MatChipsModule,
    MatSnackBarModule],
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.css'
})
export class TransactionListComponent {
  showForm = false;
  transactionForm: FormGroup;
  displayedColumns = ['date', 'description', 'type', 'amount', 'actions'];

  transactions = [
    {
      description: 'Salary', type: 'INCOME',
      amount: 3000, date: '2026-03-28'
    },
    {
      description: 'Groceries', type: 'EXPENSE',
      amount: 150, date: '2026-03-29'
    },
    {
      description: 'Netflix', type: 'EXPENSE',
      amount: 15, date: '2026-03-27'
    },
  ];

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private transactionStateService: TransactionStateService
  ) {
    this.transactionForm = this.fb.group({
      description: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0)]],
      type: ['EXPENSE', Validators.required],
      date: [new Date(), Validators.required]
    });
  }

  ngOnInit() { }

  get totalIncome() {
    return this.transactions
      .filter(t => t.type === 'INCOME')
      .reduce((sum, t) => sum + t.amount, 0);
  }

  get totalExpenses() {
    return this.transactions
      .filter(t => t.type === 'EXPENSE')
      .reduce((sum, t) => sum + t.amount, 0);
  }

  onSubmit() {
    if (this.transactionForm.invalid) return;

    const newTransaction = {
      ...this.transactionForm.value,
      date: this.transactionForm.value.date
        .toISOString().split('T')[0]
    };

    this.transactionStateService.addTransaction(newTransaction).subscribe({
      next: (transaction) => {
        this.transactions = [transaction, ...this.transactions];
        this.transactionForm.reset({ type: 'EXPENSE', date: new Date() });
        this.showForm = false;
        this.snackBar.open('Transaction added! ', 'Close',
          { duration: 3000 });
      },
      error: (message) => {
        this.snackBar.open(message, 'Close',
          { duration: 3000 });
      }
    })
  }

  deleteTransaction(id: number) {
    /*
    this.transactions = this.transactions.filter(t => t.id !== id);
    this.snackBar.open('Transaction deleted', 'Close',
      { duration: 2000 });
    */
  }
}
