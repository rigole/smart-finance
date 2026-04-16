import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable, signal } from "@angular/core";
import { TransactionService } from "../../shared/services/transaction.service";
import { catchError, finalize, Observable, tap, throwError } from "rxjs";


@Injectable({
  providedIn: 'root'
})

export class TransactionStateService {

    private readonly _loading = signal(false);
    private readonly _transactions = signal<any[]>([]);
    private readonly _error = signal<string | null>(null);

    readonly loading = this._loading.asReadonly();
    readonly transactions = this._transactions.asReadonly();
    readonly error = this._error.asReadonly();

    constructor(private transactionService: TransactionService) {}


    addTransaction(transaction: any): Observable<any> {
        this._loading.set(true);
        this._error.set(null);

        return this.transactionService.addTransaction(transaction).pipe(
            tap((newTransaction: any) => {
                this._transactions.update((transactions) => [...transactions, newTransaction]);
                this._loading.set(false);
            }),
            catchError((error: HttpErrorResponse) => {
                let message = 'Transaction failed';
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
    
