import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private router: Router, private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        // Exemple : redirige vers une page d’erreur personnalisée
        if (error.status === 404) {
          this.router.navigate(['/not-found']);
        } else if (error.status === 401) {
          if (!this.authService.isLoggedIn()) {
            this.router.navigate(['/login']);
          } else {
            // Si loggé mais 401, va sur /error ou affiche un message
            this.router.navigate(['/error']);
          }
        } else {
          this.router.navigate(['/error']);
        }
        return throwError(() => error);
      })
    );
  }
}