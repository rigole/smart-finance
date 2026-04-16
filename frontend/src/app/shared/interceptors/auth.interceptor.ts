import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { Inject } from "@angular/core";
import { Router } from "@angular/router";
import { catchError, throwError } from "rxjs";

export const authInterceptor : HttpInterceptorFn = (req, next) => {
    const token = localStorage.getItem('token');
    const router = Inject(Router);

     const authReq = token
    ? req.clone({
        headers: req.headers.set(
          'Authorization', `Bearer ${token}`
        )
      })
    : req;
    return next(authReq).pipe(
        catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
            localStorage.clear();
            router.navigate(['/auth/login']);
        }

        if (error.status === 403) {
            console.error(' Access forbidden:', error.message);
        }

        if (error.status === 500) {
            console.error(' Server error:', error.message);
        }

        return throwError(() => error);
        })
    );
}