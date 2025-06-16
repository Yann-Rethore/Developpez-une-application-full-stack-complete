import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { ArticleDto } from '../../interfaces/article.dto';
import { Observable, Subject, switchMap, startWith, tap } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html'
})
export class ArticleDetailComponent implements OnInit {
  article$!: Observable<ArticleDto>;
  commentaireForm: FormGroup;
  private refresh$ = new Subject<void>();
  commentaireSuccess = false;

  constructor(
    private route: ActivatedRoute,
    private articleService: ArticleService,
    private fb: FormBuilder
  ) {
    this.commentaireForm = this.fb.group({
      contenu: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.article$ = this.refresh$.pipe(
      startWith(undefined),
      switchMap(() =>
        this.route.paramMap.pipe(
          switchMap(params => this.articleService.getArticleById(Number(params.get('id'))))
        )
      )
    );
  }
  
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
}
