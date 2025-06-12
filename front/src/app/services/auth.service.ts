import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterRequest } from '../interfaces/register-request.dto';
import { LoginRequest } from '../interfaces/login-request.dto';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private registerUrl = `${environment.apiUrl}/auth/register`;
  private loginUrl = `${environment.apiUrl}/auth/login`;

  constructor(private http: HttpClient) {}

  register(data: RegisterRequest): Observable<any> {
    return this.http.post(this.registerUrl, data);
  }
  login(data: LoginRequest): Observable<any> {
    return this.http.post(this.loginUrl, data);
  }

  isLoggedIn(): boolean {
  return !!localStorage.getItem('authToken');
  }

  logout(): void {
    localStorage.removeItem('authToken');
  }
}