import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ArticleDetailComponent } from './article-detail.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { Location } from '@angular/common';
import { of, Subject } from 'rxjs';

describe('ArticleDetailComponent', () => {
  let component: ArticleDetailComponent;
  let fixture: ComponentFixture<ArticleDetailComponent>;
  let articleServiceSpy: jasmine.SpyObj<ArticleService>;
  let locationSpy: jasmine.SpyObj<Location>;
  let activatedRouteStub: any;

  const articleMock = {
    id: 1,
    titre: 'Titre',
    date: '2024-01-01',
    contenu: 'Contenu',
    themeName: 'Thème',
    auteurUsername: 'Auteur',
    commentaires: []
  };

  beforeEach(async () => {
    articleServiceSpy = jasmine.createSpyObj('ArticleService', ['getArticleById', 'ajouterCommentaire']);
    locationSpy = jasmine.createSpyObj('Location', ['back']);

    // Simule un paramMap avec un id
    activatedRouteStub = {
      paramMap: of({
        get: (key: string) => key === 'id' ? '1' : null
      }),
      snapshot: {
        paramMap: {
          get: (key: string) => key === 'id' ? '1' : null
        }
      }
    };

    await TestBed.configureTestingModule({
      declarations: [ArticleDetailComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: ArticleService, useValue: articleServiceSpy },
        { provide: Location, useValue: locationSpy },
        { provide: ActivatedRoute, useValue: activatedRouteStub }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ArticleDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load article on init', (done) => {
    articleServiceSpy.getArticleById.and.returnValue(of(articleMock));
    component.ngOnInit();
    component.article$.subscribe(article => {
      expect(article).toEqual(articleMock);
      done();
    });
  });

  it('should call ajouterCommentaire and reset form on valid submit', () => {
    articleServiceSpy.ajouterCommentaire.and.returnValue(of({}));
    articleServiceSpy.getArticleById.and.returnValue(of(articleMock));
    component.ngOnInit();

    component.commentaireForm.setValue({ contenu: 'Nouveau commentaire' });
    component.ajouterCommentaire();

    expect(articleServiceSpy.ajouterCommentaire).toHaveBeenCalledWith(1, 'Nouveau commentaire');
    expect(component.commentaireForm.value.contenu).toBeNull();
    expect(component.commentaireSuccess).toBeTrue();
  });

  it('should not call ajouterCommentaire if form is invalid', () => {
    component.commentaireForm.setValue({ contenu: '' });
    component.ajouterCommentaire();
    expect(articleServiceSpy.ajouterCommentaire).not.toHaveBeenCalled();
  });

  it('should call location.back on goBack()', () => {
    component.goBack();
    expect(locationSpy.back).toHaveBeenCalled();
  });

  it('should clean up destroy$ on ngOnDestroy', () => {
    spyOn(component['destroy$'], 'next');
    spyOn(component['destroy$'], 'complete');
    component.ngOnDestroy();
    expect(component['destroy$'].next).toHaveBeenCalled();
    expect(component['destroy$'].complete).toHaveBeenCalled();
  });
});