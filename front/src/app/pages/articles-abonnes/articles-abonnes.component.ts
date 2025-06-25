import { Component, OnDestroy, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { ArticleDto } from '../../interfaces/article.dto';
import { Observable, of, Subject } from 'rxjs';
import { map, catchError, startWith, takeUntil } from 'rxjs/operators';
import { Router } from '@angular/router';

/**
 * Composant affichant la liste des articles des abonnements de l'utilisateur.
 * Permet de trier les articles par date et d'accéder au détail d'un article.
 */
@Component({
  selector: 'app-articles-abonnes',
  templateUrl: './articles-abonnes.component.html',
  styleUrls: ['./articles-abonnes.component.scss']
})
export class ArticlesAbonnesComponent implements OnInit, OnDestroy {
  /** Observable contenant l'état de chargement, d'erreur et la liste des articles */
  articles$!: Observable<{ loading: boolean, error: boolean, data: ArticleDto[] }>;

  /** Indique si le tri est décroissant (du plus récent au plus ancien) */
  sortDesc = true;
  /** Subject pour gérer la destruction des subscriptions */
  private destroy$ = new Subject<void>();

  /**
   * Constructeur injectant les services nécessaires.
   * @param articleService Service pour récupérer les articles des abonnements
   * @param router Service de navigation
   */
  constructor(
    private articleService: ArticleService,
    private router: Router
  ) {}

  /**
   * Initialise le composant et charge les articles à l'initialisation.
   */
  ngOnInit() {
    this.loadArticles();
  }

  /**
   * Charge les articles des abonnements et gère le tri, le chargement et les erreurs.
   */
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

  /**
   * Inverse l'ordre de tri des articles et recharge la liste.
   */
  toggleSort() {
    this.sortDesc = !this.sortDesc;
    this.loadArticles();
  }

  /**
   * Navigue vers la page de détail de l'article sélectionné.
   * @param id Identifiant de l'article
   */
  goToDetail(id: number) {
    this.router.navigate(['/article', id]);
  }

  /**
   * Nettoie les subscriptions lors de la destruction du composant.
   */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}