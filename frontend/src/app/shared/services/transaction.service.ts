import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Transaction } from '../models/Transaction';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
    private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  addTransaction(transaction: Transaction) {
    return this.http.post(`${this.baseUrl}/transactions`, transaction);
  }

  getTransactions() {
    return this.http.get(`${this.baseUrl}/transactions`);
  }

  deleteTransaction(id: string) {
    return this.http.delete(`${this.baseUrl}/transactions/${id}`);
  }

  updateTransaction(id: string, transaction: Transaction) {
    return this.http.put(`${this.baseUrl}/transactions/${id}`, transaction);
  }

}
