import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { ArticleDto } from '../../interfaces/article.dto';
import { Observable, Subject, switchMap, startWith } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { takeUntil } from 'rxjs/operators';

/**
 * Composant affichant le détail d'un article et permettant d'ajouter un commentaire.
 */
@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss'],
})
export class ArticleDetailComponent implements OnInit, OnDestroy {
  /** Observable de l'article à afficher */
  article$!: Observable<ArticleDto>;
  /** Formulaire pour la saisie d'un commentaire */
  commentaireForm: FormGroup;
  /** Subject pour déclencher le rafraîchissement de l'article */
  private refresh$ = new Subject<void>();
  /** Indique si l'ajout du commentaire a réussi */
  commentaireSuccess = false;
  /** Subject pour gérer la destruction des subscriptions */
  private destroy$ = new Subject<void>();

  /**
   * Constructeur injectant les services nécessaires et initialisant le formulaire de commentaire.
   */
  constructor(
    private route: ActivatedRoute,
    private articleService: ArticleService,
    private fb: FormBuilder,
    private location: Location
  ) {
    // Initialisation du formulaire avec un champ 'contenu' requis
    this.commentaireForm = this.fb.group({
      contenu: ['', Validators.required]
    });
  }

  /**
   * Initialise le composant, charge l'article à afficher selon l'id de l'URL,
   * et prépare le rafraîchissement automatique après ajout de commentaire.
   */
  ngOnInit(): void {
    this.article$ = this.refresh$.pipe(
      startWith(undefined),
      switchMap(() =>
        this.route.paramMap.pipe(
          switchMap(params => this.articleService.getArticleById(Number(params.get('id'))))
        )
      )
    );
    // Note : takeUntil(this.destroy$) devrait être utilisé dans les subscriptions pour éviter les fuites mémoire
  }

  /**
   * Ajoute un commentaire à l'article courant si le formulaire est valide.
   * Après succès, réinitialise le formulaire, affiche un message de succès et rafraîchit l'article.
   */
  ajouterCommentaire() {
    if (this.commentaireForm.valid) {
      const contenu = this.commentaireForm.value.contenu;
      const articleId = Number(this.route.snapshot.paramMap.get('id'));
      this.articleService.ajouterCommentaire(articleId, contenu).subscribe(() => {
        this.commentaireForm.reset();
        this.commentaireSuccess = true;
        this.refresh$.next(); // Pour recharger l'article et ses commentaires si besoin
      });
    }
  }

  /**
   * Retourne à la page précédente.
   */
  goBack() {
    this.location.back();
  }

  /**
   * Nettoie les subscriptions lors de la destruction du composant.
   */
  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}