import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Composant de la page d'accueil.
 * Si l'utilisateur est connecté, il est automatiquement redirigé vers la page des articles abonnés.
 */
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  /**
   * Constructeur injectant le routeur et le service d'authentification.
   * @param router Service de navigation Angular
   * @param authService Service d'authentification pour vérifier l'état de connexion
   */
  constructor(private router: Router, private authService: AuthService) {}

  /**
   * À l'initialisation, redirige l'utilisateur connecté vers la page des articles abonnés.
   */
  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/article/abonnes']);
    }
  }
}
