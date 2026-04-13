import { Injectable, signal } from "@angular/core";
import { AuthService } from "../shared/services/auth.service";
import { catchError, finalize, Observable, of, tap, throwError } from "rxjs";
import { HttpErrorResponse } from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})

export class AuthStateService {
  private readonly _loading = signal(false)
  private readonly _user = signal<any | null>(null);
  private readonly _error = signal<string | null>(null);

  readonly loading = this._loading.asReadonly();
  readonly user = this._user.asReadonly();
  readonly error = this._error.asReadonly();

  constructor(private authService: AuthService) { }

  login(email: string, password: string): Observable<any> {
    this._loading.set(true);
    this._error.set(null);

    return this.authService.login(email, password).pipe(
      tap((user: any) => {
        this._user.set(user);
        this._loading.set(false);
      }),
      catchError((error: HttpErrorResponse) => {
        let message = 'Login failed';
        if (error.status === 0) {
          message = 'Could not connect to the server';
        } else if (error.status === 403) {
          message = "Invalid email or password";
        }
        else {
          message = error.error.message;
        }
        this._error.set(message);
        return throwError(() => message);
      }),
      finalize(() => {
        this._loading.set(false);
      })
    );
  }

  register(fullName: string, email: string, password: string): Observable<any> {
    this._loading.set(true);
    this._error.set(null);

    return this.authService.register(fullName, email, password).pipe(
      tap((newUser: any) => {
        this._user.set(newUser);
        this._loading.set(false);
      }),
      catchError((error: HttpErrorResponse) => {
        let message = 'Registration failed';
        if (error.status === 0) {
          message = 'Could not connect to the server';
        } else if (error.status === 403) {
          message = "This email is already taken";
        }
        else {
          message = error.error.message;
        }
        this._error.set(message);
        return throwError(() => message);
      }),
      finalize(() => {
        this._loading.set(false);
      })
    );
  }

}