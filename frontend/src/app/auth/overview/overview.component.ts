import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-overview',
  imports: [
     CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    RouterLink
  ],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent {
   userEmail = localStorage.getItem('email') || 'user@email.com';

   stats = [
    {
      label: 'Total Balance',
      value: '$12,450.00',
      icon: 'account_balance',
      color: 'cyan',
      trend: '+5.2%'
    },
    {
      label: 'Total Income',
      value: '$15,650.00',
      icon: 'trending_up',
      color: 'green',
      trend: '+2.1%'
    },
    {
      label: 'Total Expenses',
      value: '$3,200.00',
      icon: 'trending_down',
      color: 'red',
      trend: '-1.3%'
    },
    {
      label: 'Savings Rate',
      value: '79%',
      icon: 'savings',
      color: 'orange',
      trend: '+3.4%'
    }
  ];

  recentTransactions = [
    { description: 'Grocery Shopping', amount: -150, date: '2026-03-29', type: 'EXPENSE' },
    { description: 'Monthly Salary',   amount: 3000, date: '2026-03-28', type: 'INCOME' },
    { description: 'Netflix',          amount: -15,  date: '2026-03-27', type: 'EXPENSE' },
    { description: 'Freelance Work',   amount: 500,  date: '2026-03-26', type: 'INCOME' },
  ];

}
