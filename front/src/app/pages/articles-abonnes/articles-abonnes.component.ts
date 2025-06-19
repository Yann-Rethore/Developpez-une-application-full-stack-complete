import { Component } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { ArticleDto } from '../../interfaces/article.dto';
import { Observable, of } from 'rxjs';
import { map, catchError, startWith } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'app-articles-abonnes',
  templateUrl: './articles-abonnes.component.html',
  styleUrls: ['./articles-abonnes.component.scss']
})
export class ArticlesAbonnesComponent {
  articles$!: Observable<{ loading: boolean, error: boolean, data: ArticleDto[] }>;
  sortDesc = true;

  constructor(
    private articleService: ArticleService,
    private router: Router
  ) {
    this.loadArticles();
  }
  loadArticles() {
    this.articles$ = this.articleService.getArticlesAbonnes().pipe(
      map(articles => ({
        loading: false,
        error: false,
        data: articles.sort((a, b) =>
          this.sortDesc
            ? new Date(b.date).getTime() - new Date(a.date).getTime()
            : new Date(a.date).getTime() - new Date(b.date).getTime()
        )
      })),
      catchError(() => of({ loading: false, error: true, data: [] })),
      startWith({ loading: true, error: false, data: [] })
    );
  }

  toggleSort() {
    this.sortDesc = !this.sortDesc;
    this.loadArticles();
  }

  goToDetail(id: number) {
    this.router.navigate(['/article', id]);
  }
}
