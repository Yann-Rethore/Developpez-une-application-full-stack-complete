import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterRequest } from '../interfaces/register-request.dto';
import { LoginRequest } from '../interfaces/login-request.dto';
import { environment } from '../../environments/environment';

/**
 * Service d'authentification utilisateur.
 * Permet l'inscription, la connexion, la vérification de l'état de connexion et la déconnexion.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  /** URL de l'API pour l'inscription */
  private registerUrl = `${environment.apiUrl}/auth/register`;
  /** URL de l'API pour la connexion */
  private loginUrl = `${environment.apiUrl}/auth/login`;

  /**
   * Constructeur injectant HttpClient pour les requêtes HTTP.
   * @param http Service Angular pour effectuer les requêtes HTTP
   */
  constructor(private http: HttpClient) {}

  /**
   * Inscrit un nouvel utilisateur.
   * @param data Données d'inscription (username, email, password)
   * @returns Observable de la réponse de l'API
   */
  register(data: RegisterRequest): Observable<any> {
    return this.http.post(this.registerUrl, data);
  }

  /**
   * Connecte un utilisateur.
   * @param data Données de connexion (identifier, password)
   * @returns Observable de la réponse de l'API
   */
  login(data: LoginRequest): Observable<any> {
    return this.http.post(this.loginUrl, data);
  }

  /**
   * Vérifie si l'utilisateur est connecté (présence du token dans le localStorage).
   * @returns true si connecté, false sinon
   */
  isLoggedIn(): boolean {
    return !!localStorage.getItem('authToken');
  }

  /**
   * Déconnecte l'utilisateur en supprimant le token du localStorage.
   */
  logout(): void {
    localStorage.removeItem('authToken');
  }
}