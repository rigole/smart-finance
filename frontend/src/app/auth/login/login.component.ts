import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { ConfirmDialogService } from '../../shared/services/confirm-dialog.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthStateService } from '../auth-state.services';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

    loginForm: FormGroup;
    isLoading = false;
    hidePassword = true;
    errorMessage = '';
    constructor(
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
    private authStateService: AuthStateService,
    private confirmDialogService: ConfirmDialogService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) return;
    this.isLoading = true;
    this.errorMessage = '';

     this.confirmDialogService.open({
      title: 'Login User',
      message: 'Are you sure you want to login this user?',
      confirmText: 'Yes, Login',
      cancelText: 'Cancel',
      type: 'info'
    }).subscribe(confirmed => {
      if (confirmed) {
         this.authStateService.login(this.loginForm.value.email, this.loginForm.value.password).subscribe({
      next: (user) => {
        const token = user.token;
        localStorage.setItem('token', token);
        this.snackBar.open('User logged in successfully', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
        this.router.navigate(['/auth/profile/', token]);
      },
      error: (error) => {
        this.snackBar.open(error, 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar']
        });
      }
        });
    }});

   
    
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }


}
