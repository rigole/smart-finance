import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly baseUrl = 'http://localhost:8080/api';
  
  constructor(private http: HttpClient) { }

  login(email: string, password: string) {
    return this.http.post(`${this.baseUrl}/auth/login`, {email, password});
  }

  register(fullName:string, email: string, password: string) {
    return this.http.post(`${this.baseUrl}/auth/register`, {fullName, email, password});
  }
}
