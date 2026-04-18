import { Injectable, signal } from "@angular/core";
import { BudgetService } from "../../shared/services/budget.service";
import { catchError, finalize, tap } from "rxjs/operators";
import { HttpErrorResponse } from "@angular/common/http";
import { throwError } from "rxjs";


@Injectable({
  providedIn: 'root'
})

export class BudgetStateService {

    private readonly _loading = signal(false);
    private readonly _budgets = signal<any[]>([]);
    private readonly _error = signal<string | null>(null);

    readonly loading = this._loading.asReadonly();
    readonly budgets = this._budgets.asReadonly();
    readonly error = this._error.asReadonly();

    constructor(private budgetService: BudgetService) {}

    getAllBudgets() {
        this._loading.set(true);
        this._error.set(null);

        return this.budgetService.getAllBudgets().pipe(
            tap((budgets: any) => {
                this._budgets.set(budgets);
                this._loading.set(false);
            }),
            catchError((error: HttpErrorResponse) => {
                let message = 'Budget failed';
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
    
}