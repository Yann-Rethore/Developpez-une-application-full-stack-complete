import { Component } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { ArticleDto } from '../../interfaces/article.dto';
import { Observable, of } from 'rxjs';
import { map, catchError, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-articles-abonnes',
  templateUrl: './articles-abonnes.component.html'
})
export class ArticlesAbonnesComponent {
  articles$!: Observable<{ loading: boolean, error: boolean, data: ArticleDto[] }>;

  constructor(private articleService: ArticleService) {
    this.articles$ = this.articleService.getArticlesAbonnes().pipe(
      map(articles => ({
        loading: false,
        error: false,
        data: articles.sort((a, b) =>
          new Date(b.date).getTime() - new Date(a.date).getTime()
        )
      })),
      catchError(() => of({ loading: false, error: true, data: [] })),
      startWith({ loading: true, error: false, data: [] })
    );
  }
}
