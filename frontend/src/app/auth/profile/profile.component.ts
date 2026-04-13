import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatBadgeModule } from '@angular/material/badge';

@Component({
  selector: 'app-profile',
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatDividerModule,
    MatBadgeModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  //angular@springboot.com
  //awerty

  userEmail = localStorage.getItem('email') || 'user@email.com';
  userName = localStorage.getItem('fullName') || 'User';

  navItems = [
    { icon: 'dashboard', label: 'Overview', route: '' },
    { icon: 'receipt_long', label: 'Transactions', route: '/transactions' },
    { icon: 'account_balance_wallet', label: 'Budgets', route: '/budgets' },
    { icon: 'insights', label: 'AI Insights', route: 'insights' },
  ];

  logout() {
    localStorage.clear();
    window.location.href = '/auth/login';
  }
}
