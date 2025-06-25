import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Intercepteur HTTP pour ajouter le token d'authentification à chaque requête sortante.
 * Si un token est présent dans le localStorage, il est ajouté dans l'en-tête Authorization.
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  /**
   * Intercepte chaque requête HTTP sortante.
   * Ajoute le header Authorization si un token est présent.
   * @param req Requête HTTP sortante
   * @param next Prochain gestionnaire de la chaîne d'intercepteurs
   * @returns Observable de l'événement HTTP
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('authToken');
    if (token) {
      // Clone la requête en y ajoutant le header Authorization
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(cloned);
    }
    // Si pas de token, la requête est transmise sans modification
    return next.handle(req);
  }
}