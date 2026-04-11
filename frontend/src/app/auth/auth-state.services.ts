import { Injectable, signal } from "@angular/core";
import { AuthService } from "../shared/services/auth.service";
import { catchError, finalize, Observable, of, tap } from "rxjs";


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

  register(fullName: string, email: string, password: string): Observable<any> {
    this._loading.set(true);
    this._error.set(null);

    return this.authService.register(fullName, email, password).pipe(
      tap((newUser: any) => {
        this._user.set(newUser);
        this._loading.set(false);
      }),
      catchError((error) => {
        this._error.set(error);
        return of(null);
      }),
      finalize(() => {
        this._loading.set(false);
      })
    );
  }

}