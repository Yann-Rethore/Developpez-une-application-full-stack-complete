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
    // Création des spies pour les services utilisés par le composant
    articleServiceSpy = jasmine.createSpyObj('ArticleService', ['getArticlesAbonnes']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    // Configuration du module de test avec les dépendances nécessaires
    await TestBed.configureTestingModule({
      declarations: [ArticlesAbonnesComponent],
      providers: [
        { provide: ArticleService, useValue: articleServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    // Création de l'instance du composant à tester
    fixture = TestBed.createComponent(ArticlesAbonnesComponent);
    component = fixture.componentInstance;
  });

  // Test de création du composant
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test du chargement des articles à l'initialisation
  it('should load articles on init', (done) => {
    const articlesMock = [
      { id: 1, titre: 'A', date: '2024-01-01', contenu: 'Contenu A', themeName: 'Thème A', auteurUsername: 'AuteurA', commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }] },
      { id: 2, titre: 'B', date: '2024-01-02', contenu: 'Contenu B', themeName: 'Thème B', auteurUsername: 'AuteurB', commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }] }
    ];
    articleServiceSpy.getArticlesAbonnes.and.returnValue(of(articlesMock));

    component.ngOnInit();

    // On saute la première émission (loading) pour tester le résultat réel
    component.articles$.pipe(skip(1)).subscribe(result => {
      expect(result.loading).toBeFalse();
      expect(result.error).toBeFalse();
      expect(result.data.length).toBe(2);
      done();
    });
  });

  // Test du tri décroissant par défaut
  it('should sort articles descending by default', (done) => {
    const articlesMock = [
      { id: 1, titre: 'A', date: '2024-01-01', contenu: 'Contenu A', themeName: 'Thème A', auteurUsername: 'AuteurA', commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }] },
      { id: 2, titre: 'B', date: '2024-01-02', contenu: 'Contenu B', themeName: 'Thème B', auteurUsername: 'AuteurB', commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }] }
    ];
    articleServiceSpy.getArticlesAbonnes.and.returnValue(of(articlesMock));

    component.sortDesc = true;
    component.loadArticles();

    component.articles$.pipe(skip(1)).subscribe(result => {
      expect(result.data[0].id).toBe(2); // L'article le plus récent en premier
      done();
    });
  });

  // Test du tri croissant après inversion
  it('should sort articles ascending when toggled', (done) => {
    const articlesMock = [
      { id: 1, titre: 'A', date: '2024-01-01', contenu: 'Contenu A', themeName: 'Thème A', auteurUsername: 'AuteurA', commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }] },
      { id: 2, titre: 'B', date: '2024-01-02', contenu: 'Contenu B', themeName: 'Thème B', auteurUsername: 'AuteurB', commentaires: [{ id: 1, contenu: 'Bravo', createurUsername: 'User1', date: '2024-01-01' }] }
    ];
    articleServiceSpy.getArticlesAbonnes.and.returnValue(of(articlesMock));

    component.sortDesc = false;
    component.loadArticles();

    component.articles$.pipe(skip(1)).subscribe(result => {
      expect(result.data[0].id).toBe(1); // L'article le plus ancien en premier
      done();
    });
  });

  // Test de l'inversion du tri et du rechargement des articles
  it('should toggle sort order and reload articles', () => {
    spyOn(component, 'loadArticles');
    component.toggleSort();
    expect(component.sortDesc).toBeFalse();
    expect(component.loadArticles).toHaveBeenCalled();
  });

  // Test de la navigation vers le détail d'un article
  it('should navigate to article detail', () => {
    component.goToDetail(42);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/article', 42]);
  });

  // Test du nettoyage des subscriptions lors de la destruction du composant
  it('should clean up destroy$ on ngOnDestroy', () => {
    spyOn(component['destroy$'], 'next');
    spyOn(component['destroy$'], 'complete');
    component.ngOnDestroy();
    expect(component['destroy$'].next).toHaveBeenCalled();
    expect(component['destroy$'].complete).toHaveBeenCalled();
  });
});