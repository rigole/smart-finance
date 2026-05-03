import { Injectable, signal } from "@angular/core";
import { InsightsService } from "../../shared/services/insights.service";
import { catchError, finalize, tap, throwError } from "rxjs";
import { HttpErrorResponse } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})

export class InsightStateService {
    private readonly _loading = signal(false);
    private readonly _insights = signal<any[]>([]);
    private readonly _error = signal<string | null>(null);

    readonly loading = this._loading.asReadonly();
    readonly insights = this._insights.asReadonly();
    readonly error = this._error.asReadonly();

    constructor(private insightsService: InsightsService) { }

    loadInsights() {
        this._loading.set(true);
        this._error.set(null);
        return this.insightsService.getAllInsights().pipe(
            tap((insights: any) => {
                this._insights.set(insights)
                this._loading.set(false)
            }),
            catchError((error: HttpErrorResponse) => {
                let message = 'insights failed';
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
        )
    }

    addInsights() {
        this._loading.set(true)
        this._error.set(null)

        return this.insightsService.generateInsights().pipe(
            tap((insight: any) => {
                this._insights.set(insight)
                this._error.set(null)
                this._loading.set(false)

            }),
            catchError((error: HttpErrorResponse) => {
                let message = 'insights failed';
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
        )
    }
}