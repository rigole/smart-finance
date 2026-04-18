import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly baseUrl = 'http://localhost:8080/api';
  
  constructor(private http: HttpClient, private router: Router) { }

  login(email: string, password: string) {
    return this.http.post(`${this.baseUrl}/auth/login`, {email, password});
  }

  register(fullName:string, email: string, password: string) {
    return this.http.post(`${this.baseUrl}/auth/register`, {fullName, email, password});
  }

  getToken(){
    return localStorage.getItem('token');
  }

  isTokenExpired(){
    const token = this.getToken();
    if(!token) return true;
    try{
      const decoded: any = jwtDecode(token)
      const now = Date.now() / 1000;
      return decoded.exp < now;
    } catch{
      return true;
    }
  }

  isLoggedIn(){
    return !this.isTokenExpired();
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/auth/login']);
  }
}
