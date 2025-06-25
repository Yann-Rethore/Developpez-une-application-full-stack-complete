import { Component, OnDestroy, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { ArticleDto } from '../../interfaces/article.dto';

import { Observable, of, Subject } from 'rxjs';
import { map, catchError, startWith, takeUntil } from 'rxjs/operators';
import { Router } from '@angular/router';



@Component({
  selector: 'app-articles-abonnes',
  templateUrl: './articles-abonnes.component.html',
  styleUrls: ['./articles-abonnes.component.scss']
})
export class ArticlesAbonnesComponent implements   OnInit ,OnDestroy {
  articles$!: Observable<{ loading: boolean, error: boolean, data: ArticleDto[] }>;

  sortDesc = true;
  private destroy$ = new Subject<void>();

  constructor(
    private articleService: ArticleService,
    private router: Router
  ) {}

  ngOnInit() {
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
      startWith({ loading: true, error: false, data: [] }),
      takeUntil(this.destroy$)
    );
  }

  toggleSort() {
    this.sortDesc = !this.sortDesc;
    this.loadArticles();
  }

  goToDetail(id: number) {
    this.router.navigate(['/article', id]);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

}
