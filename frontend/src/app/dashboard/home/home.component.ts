import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon'
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule, MatIconModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  features = [
    {
      icon: 'credit_card',
      title: 'Track Transactions',
      description: 'Record and manage all your income and expenses in one place'
    },
    {
      icon: 'dashboard',
      title: 'Visual Dashboard',
      description: 'Beautiful charts to visualize your spending habits'
    },
    {
      icon: 'flag',
      title: 'Budget Goals',
      description: 'Set monthly budgets and get alerts when you overspend'
    },
    {
      icon: 'smart_toy',
      title: 'AI Insights',
      description: 'Get smart financial tips powered by AI analysis'
    }
  ];
}
