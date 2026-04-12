import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { AuthStateService } from '../../auth/auth-state.services';

export const authGuard: CanActivateFn = (route, state) => {
  const authStateService = inject(AuthStateService);
  const router = inject(Router);

  const token = localStorage.getItem('token');

  if (token) {
    return true;
  }

  //angular@springboot.com

  router.navigate(['/auth/login']);
  return false;
};
