import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { InsightStateService } from '../services/insights-state.services';
import { MatProgressSpinner } from "@angular/material/progress-spinner";

@Component({
  selector: 'app-insights-list',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatProgressBarModule,
    MatSnackBarModule,
    MatProgressSpinner
  ],
  templateUrl: './insights-list.component.html',
  styleUrl: './insights-list.component.css'
})
export class InsightsListComponent implements OnInit {
  insights: any;
  loading: any;
  error: any;
  generating: boolean = false;

  constructor(
    private snackBar: MatSnackBar,
    private insightStateService: InsightStateService) {
    this.insights = this.insightStateService.insights;
    this.loading = this.insightStateService.loading;
    this.error = this.insightStateService.error;
  }

  ngOnInit(): void {
    this.loadInsights()
  }

  loadInsights() {
    this.insightStateService.loadInsights().subscribe({
      error: (message) => {
        this.snackBar.open(message, 'Close',
          { duration: 3000 });
      }
    })
  }

  generateInsights() {
    this.insightStateService.addInsights().subscribe({
      next: () => {
        this.snackBar.open('Insighted generated! ', 'Close',
          { duration: 3000 });
      }
    })
  }

  getIcon(type: string): string {
    const icons: any = {
      SPENDING_INCREASE: 'trending_up',
      SPENDING_DECREASE: 'trending_down',
      BUDGET_EXCEEDED: 'warning',
      BUDGET_WARNING: 'notifications',
      SAVINGS_RATE: 'savings',
      TOP_CATEGORY: 'category'
    };
    return icons[type] || 'lightbulb';
  }

  getColor(type: string): string {
    const colors: any = {
      SPENDING_INCREASE: '#f44336',
      SPENDING_DECREASE: '#4caf50',
      BUDGET_EXCEEDED: '#f44336',
      BUDGET_WARNING: '#ff9800',
      SAVINGS_RATE: '#00bcd4',
      TOP_CATEGORY: '#9c27b0'
    };
    return colors[type] || '#607d8b';
  }

}
