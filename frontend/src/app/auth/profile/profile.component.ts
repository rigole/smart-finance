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
import { ActivatedRoute } from '@angular/router';
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
  userId: string = '';
  navItems: { icon: string; label: string; route: string; }[] = [];
  //angular@springboot.com
  //awerty
  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userId = params['id'];
      this.navItems = [
        {
          icon: 'dashboard',
          label: 'Overview',
          route: `/auth/profile/${this.userId}`
        },
        {
          icon: 'receipt_long',
          label: 'Transactions',
          route: `/auth/profile/${this.userId}/transactions`
        },
        {
          icon: 'account_balance_wallet',
          label: 'Budgets',
          route: `/auth/profile/${this.userId}/budgets`
        },
        {
          icon: 'insights',
          label: 'AI Insights',
          route: `/auth/profile/${this.userId}/insights`
        },
      ];
    });
  }

  userEmail = localStorage.getItem('email') || 'user@email.com';
  userName = localStorage.getItem('fullName') || 'User';

  logout() {
    localStorage.clear();
    window.location.href = '/auth/login';
  }
}
