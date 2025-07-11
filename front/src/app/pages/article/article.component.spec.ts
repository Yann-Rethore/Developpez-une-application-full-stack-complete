import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ArticleComponent } from './article.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ArticleService } from '../../services/article.service';
import { TopicService } from '../../services/topic.service';
import { Location } from '@angular/common';
import { of } from 'rxjs';

describe('ArticleComponent', () => {
  let component: ArticleComponent;
  let fixture: ComponentFixture<ArticleComponent>;
  let articleServiceSpy: jasmine.SpyObj<ArticleService>;
  let topicServiceSpy: jasmine.SpyObj<TopicService>;
  let locationSpy: jasmine.SpyObj<Location>;

  beforeEach(async () => {
    // Création des spies pour les services utilisés par le composant
    articleServiceSpy = jasmine.createSpyObj('ArticleService', ['createArticle']);
    topicServiceSpy = jasmine.createSpyObj('TopicService', ['getAllTopics']);
    locationSpy = jasmine.createSpyObj('Location', ['back']);

    // Par défaut, retourne une liste vide de thèmes
    topicServiceSpy.getAllTopics.and.returnValue(of([]));

    // Configuration du module de test avec les dépendances nécessaires
    await TestBed.configureTestingModule({
      declarations: [ArticleComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: ArticleService, useValue: articleServiceSpy },
        { provide: TopicService, useValue: topicServiceSpy },
        { provide: Location, useValue: locationSpy }
      ]
    }).compileComponents();

    // Création de l'instance du composant à tester
    fixture = TestBed.createComponent(ArticleComponent);
    component = fixture.componentInstance;
  });

  // Test de création du composant
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test du chargement des thèmes à l'initialisation
  it('should load topics on init', (done) => {
    const topicsMock = [
      { id: 1, name: 'Thème 1', description : 'Description 1' },
      { id: 2, name: 'Thème 2', description : 'Description 2' }
    ];
    topicServiceSpy.getAllTopics.and.returnValue(of(topicsMock));
    component.ngOnInit();
    component.topics$.subscribe(topics => {
      expect(topics.length).toBe(2);
      expect(topics[0].name).toBe('Thème 1');
      done();
    });
  });

  // Test de la soumission du formulaire valide
  it('should call ArticleService.createArticle on valid submit', (done) => {
    articleServiceSpy.createArticle.and.returnValue(of({}));
    component.ngOnInit();

    component.articleForm.setValue({
      titre: 'Titre',
      contenu: 'Contenu',
      themeId: 1
    });

    component.success$.subscribe(success => {
      if (success) {
        expect(articleServiceSpy.createArticle).toHaveBeenCalledWith({
          titre: 'Titre',
          contenu: 'Contenu',
          themeId: 1
        });
        done();
      }
    });
    component.onSubmit();
  });

  // Test de la soumission du formulaire invalide
  it('should not call ArticleService.createArticle if form is invalid', (done) => {
    articleServiceSpy.createArticle.and.returnValue(of({}));
    component.articleForm.setValue({
      titre: '',
      contenu: '',
      themeId: ''
    });
    component.ngOnInit();
    component.onSubmit();

    component.success$.subscribe(success => {
      expect(articleServiceSpy.createArticle).not.toHaveBeenCalled();
      expect(success).toBeFalse();
      done();
    });
  });

  // Test du retour arrière via la méthode goBack
  it('should call location.back on goBack()', () => {
    component.goBack();
    expect(locationSpy.back).toHaveBeenCalled();
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