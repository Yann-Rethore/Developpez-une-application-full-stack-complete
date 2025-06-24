import { Component, OnDestroy, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { ArticleDto } from '../../interfaces/article.dto';
<<<<<<< Updated upstream
import { Observable, of } from 'rxjs';
import { map, catchError, startWith } from 'rxjs/operators';
=======
import { Observable, of, Subject } from 'rxjs';
import { map, catchError, startWith, takeUntil } from 'rxjs/operators';
import { Router } from '@angular/router';
>>>>>>> Stashed changes

@Component({
  selector: 'app-articles-abonnes',
  templateUrl: './articles-abonnes.component.html'
})
export class ArticlesAbonnesComponent implements   OnInit ,OnDestroy {
  articles$!: Observable<{ loading: boolean, error: boolean, data: ArticleDto[] }>;
<<<<<<< Updated upstream

  constructor(private articleService: ArticleService) {
=======
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
>>>>>>> Stashed changes
    this.articles$ = this.articleService.getArticlesAbonnes().pipe(
      map(articles => ({
        loading: false,
        error: false,
        data: articles.sort((a, b) =>
          new Date(b.date).getTime() - new Date(a.date).getTime()
        )
      })),
      catchError(() => of({ loading: false, error: true, data: [] })),
      startWith({ loading: true, error: false, data: [] }),
      takeUntil(this.destroy$)
    );
  }
<<<<<<< Updated upstream
=======

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
>>>>>>> Stashed changes
}
