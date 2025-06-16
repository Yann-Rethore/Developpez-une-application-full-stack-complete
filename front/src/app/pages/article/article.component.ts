import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ArticleService } from '../../services/article.service';
import { TopicService } from '../../services/topic.service';
import { ArticleCreateDTO } from '../../interfaces/article-create.dto';
import { TopicDto } from '../../interfaces/topic.dto';
import { Observable, Subject, of, switchMap, catchError, startWith, map } from 'rxjs';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html'
})
export class ArticleComponent implements OnInit {
  articleForm: FormGroup;
  submitted = false;

  topics$!: Observable<TopicDto[]>;
  private submit$ = new Subject<void>();
  success$!: Observable<boolean>;

  constructor(
    private fb: FormBuilder,
    private articleService: ArticleService,
    private topicService: TopicService
  ) {
    this.articleForm = this.fb.group({
      titre: ['', Validators.required],
      contenu: ['', Validators.required],
      themeId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.topics$ = this.topicService.getAllTopics();

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
      startWith(false)
    );
  }

  onSubmit() {
    this.submit$.next();
  }
}