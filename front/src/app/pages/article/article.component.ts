import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ArticleService } from '../../services/article.service';
import { TopicService } from '../../services/topic.service';
import { ArticleCreateDTO } from '../../interfaces/article-create.dto';
import { TopicDto } from '../../interfaces/topic.dto';
import { Observable, Subject, of, switchMap, catchError, startWith, map } from 'rxjs';

import { takeUntil } from 'rxjs/operators';
import { Location } from '@angular/common';


@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit,  OnDestroy {
  articleForm: FormGroup;
  submitted = false;
  private destroy$ = new Subject<void>();

  topics$!: Observable<TopicDto[]>;
  private submit$ = new Subject<void>();
  success$!: Observable<boolean>;

  constructor(
    private fb: FormBuilder,
    private articleService: ArticleService,
    private topicService: TopicService,
    private location: Location
  ) {
    this.articleForm = this.fb.group({
      titre: ['', Validators.required],
      contenu: ['', Validators.required],
      themeId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.topics$ = this.topicService.getAllTopics().pipe(
      takeUntil(this.destroy$)
    );

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

  onSubmit() {
    this.submit$.next();
  }

  goBack() {
    this.location.back();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

}