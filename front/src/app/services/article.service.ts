import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ArticleCreateDTO } from '../interfaces/article-create.dto';
import { ArticleDto } from '../interfaces/article.dto';

/**
 * Service pour la gestion des articles et des commentaires.
 * Permet de créer un article, récupérer les articles des abonnements,
 * obtenir le détail d'un article et ajouter un commentaire.
 */
@Injectable({ providedIn: 'root' })
export class ArticleService {
  /** URL de base de l'API pour les articles */
  private apiUrl = `${environment.apiUrl}/article`;

  /**
   * Constructeur injectant HttpClient pour les requêtes HTTP.
   * @param http Service Angular pour effectuer les requêtes HTTP
   */
  constructor(private http: HttpClient) {}

  /**
   * Crée un nouvel article.
   * @param dto Données de l'article à créer
   * @returns Observable de la réponse de l'API
   */
  createArticle(dto: ArticleCreateDTO): Observable<any> {
    return this.http.post(this.apiUrl, dto);
  }

  /**
   * Récupère la liste des articles des abonnements de l'utilisateur.
   * @returns Observable de la liste des articles
   */
  getArticlesAbonnes(): Observable<ArticleDto[]> {
    return this.http.get<ArticleDto[]>(`${this.apiUrl}/abonnes`);
  }

  /**
   * Récupère le détail d'un article par son identifiant.
   * @param id Identifiant de l'article
   * @returns Observable de l'article
   */
  getArticleById(id: number): Observable<ArticleDto> {
    return this.http.get<ArticleDto>(`${this.apiUrl}/${id}`);
  }

  /**
   * Ajoute un commentaire à un article.
   * @param articleId Identifiant de l'article
   * @param contenu Contenu du commentaire
   * @returns Observable de la réponse de l'API
   */
  ajouterCommentaire(articleId: number, contenu: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${articleId}/commentaire`, { contenu });
  }
}