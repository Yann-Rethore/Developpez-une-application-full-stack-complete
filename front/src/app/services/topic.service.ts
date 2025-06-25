import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { TopicDto } from '../interfaces/topic.dto';

/**
 * Service pour la gestion des thèmes (topics).
 * Permet de récupérer la liste des thèmes, de s'abonner/désabonner à un thème,
 * et de récupérer les abonnements de l'utilisateur.
 */
@Injectable({
  providedIn: 'root'
})
export class TopicService {
  /** URL de base de l'API pour les thèmes */
  private apiUrl = `${environment.apiUrl}/themes`; // adapte l’URL si besoin

  /**
   * Constructeur injectant HttpClient pour les requêtes HTTP.
   * @param http Service Angular pour effectuer les requêtes HTTP
   */
  constructor(private http: HttpClient) {}

  /**
   * Récupère la liste de tous les thèmes.
   * @returns Observable contenant la liste des thèmes
   */
  getAllTopics(): Observable<TopicDto[]> {
    return this.http.get<TopicDto[]>(this.apiUrl);
  }

  /**
   * Abonne l'utilisateur au thème spécifié.
   * @param topicId Identifiant du thème à abonner
   * @returns Observable de la réponse de l'API
   */
  subscribeToTopic(topicId: number) {
    return this.http.get(`${this.apiUrl}/subscribe/${topicId}`);
  }

  /**
   * Désabonne l'utilisateur du thème spécifié.
   * @param topicId Identifiant du thème à désabonner
   * @returns Observable de la réponse de l'API
   */
  unsubscribeFromTopic(topicId: number) {
    return this.http.get(`${this.apiUrl}/unsubscribe/${topicId}`);
  }

  /**
   * Récupère la liste des identifiants des thèmes auxquels l'utilisateur est abonné.
   * @returns Observable contenant la liste des identifiants
   */
  getUserSubscriptions(): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/me/subscriptions`);
  }
}