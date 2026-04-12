import { CommonModule } from '@angular/common';
import { Component, inject, Signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AuthStateService } from '../auth-state.services';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmDialogService } from '../../shared/services/confirm-dialog.service';

@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    RouterLink,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatDialogModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  private _snackBar = inject(MatSnackBar);

  loginForm: FormGroup;
  isLoading = false;
  hidePassword = true;
  formValueSignal!: Signal<any>;
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
    private authStateService: AuthStateService,
    private confirmDialogService: ConfirmDialogService
  ) {
    this.loginForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
    this.formValueSignal = toSignal(this.loginForm.valueChanges, {
      initialValue: this.loginForm.value
    });
  }

  onSubmit() {

    this.confirmDialogService.open({
      title: 'Register User',
      message: 'Are you sure you want to register this user?',
      confirmText: 'Yes, Register',
      cancelText: 'Cancel',
      type: 'info'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.authStateService.register(this.loginForm.value.fullName, this.loginForm.value.email, this.loginForm.value.password).subscribe({
          next: (user) => {
            const token = user.token;
            localStorage.setItem('token', token);
            this.snackBar.open('User registered successfully', 'Close', {
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
      }
    });


    /*if (this.loginForm.invalid) return;

    this.isLoading = true;
    this.errorMessage = '';
    console.log('Login form values:', this.loginForm.value);
    setTimeout(() => {
      this.isLoading = false;

    }, 1000);
    */
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }
  get fullName() { return this.loginForm.get('fullName'); }



}


