import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Composant d'en-tête de l'application.
 * Affiche les liens de navigation et gère la déconnexion de l'utilisateur.
 */
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  /**
   * Constructeur injectant le service d'authentification et le routeur.
   * @param authService Service d'authentification pour vérifier l'état de connexion et déconnecter
   * @param router Service de navigation Angular
   */
  constructor(private authService: AuthService, private router: Router) {}

  /**
   * Vérifie si l'utilisateur est connecté.
   * @returns true si l'utilisateur est connecté, false sinon
   */
  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  /**
   * Déconnecte l'utilisateur et le redirige vers la page d'accueil.
   */
  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}