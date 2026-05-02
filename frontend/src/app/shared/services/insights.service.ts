import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class InsightsService {
private baseUrl = 'http://localhost:8080/api/insights';

  constructor(private http: HttpClient) { }

  getAllInsights() {
    return this.http.get(`${this.baseUrl}`);
  }

  generateInsights() {
    return this.http.post(`${this.baseUrl}`, {});
  }
}
