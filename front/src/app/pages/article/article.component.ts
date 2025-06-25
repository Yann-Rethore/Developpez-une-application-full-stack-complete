import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ArticleService } from '../../services/article.service';
import { TopicService } from '../../services/topic.service';
import { ArticleCreateDTO } from '../../interfaces/article-create.dto';
import { TopicDto } from '../../interfaces/topic.dto';
import { Observable, Subject, of, switchMap, catchError, startWith, map } from 'rxjs';

import { takeUntil } from 'rxjs/operators';
import { Location } from '@angular/common';

/**
 * Composant pour la création d'un nouvel article.
 * Permet à l'utilisateur de saisir un titre, un contenu et de choisir un thème,
 * puis de soumettre le formulaire pour créer l'article.
 */
@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit, OnDestroy {
  /** Formulaire de création d'article */
  articleForm: FormGroup;
  /** Indique si le formulaire a été soumis */
  submitted = false;
  /** Subject pour gérer la destruction des subscriptions */
  private destroy$ = new Subject<void>();

  /** Observable des thèmes disponibles */
  topics$!: Observable<TopicDto[]>;
  /** Subject pour déclencher la soumission du formulaire */
  private submit$ = new Subject<void>();
  /** Observable indiquant le succès de la création */
  success$!: Observable<boolean>;

  /**
   * Constructeur injectant les services nécessaires et initialisant le formulaire.
   */
  constructor(
    private fb: FormBuilder,
    private articleService: ArticleService,
    private topicService: TopicService,
    private location: Location
  ) {
    // Initialisation du formulaire avec les champs requis
    this.articleForm = this.fb.group({
      titre: ['', Validators.required],
      contenu: ['', Validators.required],
      themeId: ['', Validators.required]
    });
  }

  /**
   * Initialisation du composant.
   * Charge la liste des thèmes et prépare l'observable de succès de création.
   */
  ngOnInit(): void {
    // Récupère tous les thèmes disponibles
    this.topics$ = this.topicService.getAllTopics().pipe(
      takeUntil(this.destroy$)
    );

    // Gère la soumission du formulaire et le retour de succès ou d'échec
    this.success$ = this.submit$.pipe(
      switchMap(() => {
        this.submitted = true;
        if (this.articleForm.valid) {
          const dto: ArticleCreateDTO = this.articleForm.value;
          return this.articleService.createArticle(dto).pipe(
            map(() => true),
            catchError(() => of(false))
          );
        }
        return of(false);
      }),
      startWith(false),
      takeUntil(this.destroy$)
    );
  }

  /**
   * Déclenche la soumission du formulaire.
   */
  onSubmit() {
    this.submit$.next();
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