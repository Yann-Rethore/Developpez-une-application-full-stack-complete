import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard d'authentification pour protéger les routes.
 * Empêche l'accès aux routes protégées si l'utilisateur n'est pas connecté.
 */
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  /**
   * Constructeur injectant le service d'authentification et le routeur.
   * @param authService Service d'authentification pour vérifier l'état de connexion
   * @param router Service de navigation Angular
   */
  constructor(private authService: AuthService, private router: Router) {}

  /**
   * Détermine si la route peut être activée.
   * Si l'utilisateur est connecté, retourne true.
   * Sinon, redirige vers la page de connexion.
   * @returns true si connecté, sinon UrlTree vers /login
   */
  canActivate(): boolean | UrlTree {
    if (this.authService.isLoggedIn()) {
      return true;
    }
    // Redirige vers login si non authentifié
    return this.router.createUrlTree(['/login']);
  }
}