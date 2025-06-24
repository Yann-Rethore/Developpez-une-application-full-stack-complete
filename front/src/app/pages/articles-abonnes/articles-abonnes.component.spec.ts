import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ArticlesAbonnesComponent } from './articles-abonnes.component';
import { ArticleService } from '../../services/article.service';
import { Router } from '@angular/router';
import { of, skip } from 'rxjs';

describe('ArticlesAbonnesComponent', () => {
  let component: ArticlesAbonnesComponent;
  let fixture: ComponentFixture<ArticlesAbonnesComponent>;
  let articleServiceSpy: jasmine.SpyObj<ArticleService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    articleServiceSpy = jasmine.createSpyObj('ArticleService', ['getArticlesAbonnes']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [ArticlesAbonnesComponent],
      providers: [
        { provide: ArticleService, useValue: articleServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ArticlesAbonnesComponent);
    component = fixture.componentInstance;
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load articles on init', (done) => {
    const articlesMock = [
  {
    id: 1,
    titre: 'A',
    date: '2024-01-01',
    contenu: 'Contenu A',
    themeName: 'Thème A',
    auteurUsername: 'AuteurA',
    commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }]
  },
  {
    id: 2,
    titre: 'B',
    date: '2024-01-02',
    contenu: 'Contenu B',
    themeName: 'Thème B',
    auteurUsername: 'AuteurB',
    commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }]
  }
];
    articleServiceSpy.getArticlesAbonnes.and.returnValue(of(articlesMock));

    component.ngOnInit();

    component.articles$.pipe(skip(1)).subscribe(result => {
      expect(result.loading).toBeFalse();
      expect(result.error).toBeFalse();
      expect(result.data.length).toBe(2);
      done();
    });
  });

  it('should sort articles descending by default', (done) => {
    const articlesMock = [
    {
    id: 1,
    titre: 'A',
    date: '2024-01-01',
    contenu: 'Contenu A',
    themeName: 'Thème A',
    auteurUsername: 'AuteurA',
    commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }]
    },
    {
    id: 2,
    titre: 'B',
    date: '2024-01-02',
    contenu: 'Contenu B',
    themeName: 'Thème B',
    auteurUsername: 'AuteurB',
    commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }]
   }];
    articleServiceSpy.getArticlesAbonnes.and.returnValue(of(articlesMock));

    component.sortDesc = true;
    component.loadArticles();

    component.articles$.pipe(skip(1)).subscribe(result => {
      expect(result.data[0].id).toBe(2); // Most recent first
      done();
    });
  });

  it('should sort articles ascending when toggled', (done) => {
    const articlesMock = [
  {
    id: 1,
    titre: 'A',
    date: '2024-01-01',
    contenu: 'Contenu A',
    themeName: 'Thème A',
    auteurUsername: 'AuteurA',
    commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }]
  },
  {
    id: 2,
    titre: 'B',
    date: '2024-01-02',
    contenu: 'Contenu B',
    themeName: 'Thème B',
    auteurUsername: 'AuteurB',
    commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }]
  }
];
    articleServiceSpy.getArticlesAbonnes.and.returnValue(of(articlesMock));

    component.sortDesc = false;
    component.loadArticles();

    component.articles$.pipe(skip(1)).subscribe(result => {
      expect(result.data[0].id).toBe(1); // Oldest first
      done();
    });
  });

  it('should toggle sort order and reload articles', () => {
    spyOn(component, 'loadArticles');
    component.toggleSort();
    expect(component.sortDesc).toBeFalse();
    expect(component.loadArticles).toHaveBeenCalled();
  });

  it('should navigate to article detail', () => {
    component.goToDetail(42);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/article', 42]);
  });

  it('should clean up destroy$ on ngOnDestroy', () => {
    spyOn(component['destroy$'], 'next');
    spyOn(component['destroy$'], 'complete');
    component.ngOnDestroy();
    expect(component['destroy$'].next).toHaveBeenCalled();
    expect(component['destroy$'].complete).toHaveBeenCalled();
  });
});