import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable, of, tap } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class ExportService {
    
  private baseUrl = 'http://localhost:8080/api/export'; 

  constructor(private http:HttpClient){}

  private triggerDownload(blob: Blob, filename: string) {
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = filename;
  link.click();
  URL.revokeObjectURL(link.href);
}

  exportTransactionsCsv(): Observable<void> {
  return this.http.get(
    `${this.baseUrl}/transactions/csv`,
    { responseType: 'blob' }
  ).pipe(
    tap(blob => this.triggerDownload(blob, 'transactions.csv')),
    map(() => void 0) 
  );
}

  exportBudgetsCsv(): Observable<void> {
    return this.http.get(
    `${this.baseUrl}/budgets/csv`,
    { responseType: 'blob' }
  ).pipe(
    tap(blob => this.triggerDownload(blob, 'budgets.csv')),
    map(() => void 0) 
  );
  }

  exportTransactionsPdf(): Observable<void> {
    return this.http.get(
    `${this.baseUrl}/transactions/pdf`,
    { responseType: 'blob' }
  ).pipe(
    tap(blob => this.triggerDownload(blob, 'transactions.pdf')),
    map(() => void 0) 
  );
  }

  exportBudgetsPdf(): Observable<void> {
    return this.http.get(
    `${this.baseUrl}/budgets/pdf`,
    { responseType: 'blob' }
    ).pipe(
        tap(blob => this.triggerDownload(blob, 'budgets.pdf')),
        map(() => void 0) 
    );
  }







}