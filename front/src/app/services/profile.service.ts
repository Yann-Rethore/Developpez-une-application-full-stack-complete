import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserProfileDTO, UserProfileUpdateDTO } from '../interfaces/user-profile.dto';

/**
 * Service pour la gestion du profil utilisateur.
 * Permet de récupérer et de mettre à jour les informations du profil.
 */
@Injectable({ providedIn: 'root' })
export class ProfileService {
  /** URL de base de l'API pour le profil utilisateur */
  private apiUrl = `${environment.apiUrl}/profile`;

  /**
   * Constructeur injectant HttpClient pour les requêtes HTTP.
   * @param http Service Angular pour effectuer les requêtes HTTP
   */
  constructor(private http: HttpClient) {}

  /**
   * Récupère les informations du profil utilisateur courant.
   * @returns Observable contenant les données du profil utilisateur
   */
  getProfile(): Observable<UserProfileDTO> {
    return this.http.get<UserProfileDTO>(this.apiUrl);
  }

  /**
   * Met à jour les informations du profil utilisateur.
   * @param data Données à mettre à jour (username, email, password, désabonnements, etc.)
   * @returns Observable de la réponse de l'API
   */
  updateProfile(data: UserProfileUpdateDTO): Observable<any> {
    return this.http.put(this.apiUrl, data);
  }
}