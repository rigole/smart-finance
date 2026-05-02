import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { Router } from "@angular/router";
import { catchError, throwError } from "rxjs";
import { AuthService } from "../services/auth.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService)
  const publicUrls = ['/auth/login', '/auth/register', 'assets/i18n/', 'i18n/'];
  const isPublic = publicUrls.some(url => req.url.includes(url));

  if (isPublic) {
    return next(req);
  }

  
  if (authService.isTokenExpired()) {
    localStorage.clear();
    router.navigate(['/auth/login']);
    return throwError(() => 'Session expired');
  }

  const token = authService.getToken();
  const authReq = token
    ? req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        authService.logout();
      }
      return throwError(() => error);
    })
  );
};