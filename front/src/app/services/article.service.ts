import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ArticleCreateDTO } from '../interfaces/article-create.dto';
import { ArticleDto } from '../interfaces/article.dto';

@Injectable({ providedIn: 'root' })
export class ArticleService {
  private apiUrl = `${environment.apiUrl}/article`;

  constructor(private http: HttpClient) {}

  createArticle(dto: ArticleCreateDTO): Observable<any> {
    return this.http.post(this.apiUrl, dto);
  }

  getArticlesAbonnes(): Observable<ArticleDto[]> {
  return this.http.get<ArticleDto[]>(`${this.apiUrl}/abonnes`);
  }

  getArticleById(id: number): Observable<ArticleDto> {
  return this.http.get<ArticleDto>(`${this.apiUrl}/${id}`);
  }

  ajouterCommentaire(articleId: number, contenu: string): Observable<any> {
  return this.http.post(`${this.apiUrl}/${articleId}/commentaire`, { contenu });
  }
}