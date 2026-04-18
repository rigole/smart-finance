import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BudgetService {

  private baseUrl = 'http://localhost:8080/api/budgets';

  constructor(private http: HttpClient) { }

  getAllBudgets() {
    return this.http.get(`${this.baseUrl}`);
  }

  createBudget(budget: any) {
    return this.http.post(`${this.baseUrl}`, budget);
  }

  updateBudget(id: string, budget: any) {
    return this.http.put(`${this.baseUrl}/${id}`, budget);
  }

  deleteBudget(id: string) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
