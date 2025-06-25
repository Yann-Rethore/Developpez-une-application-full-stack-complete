import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ThemeComponent } from './theme.component';
import { TopicService } from '../../services/topic.service';
import { of, throwError } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ThemeComponent', () => {
  let component: ThemeComponent;
  let fixture: ComponentFixture<ThemeComponent>;
  let topicServiceSpy: jasmine.SpyObj<TopicService>;

  beforeEach(async () => {
    // Création du spy pour TopicService avec les méthodes utilisées dans le composant
    topicServiceSpy = jasmine.createSpyObj('TopicService', [
      'getAllTopics',
      'getUserSubscriptions',
      'subscribeToTopic'
    ]);

    // Configuration du module de test avec les dépendances nécessaires
    await TestBed.configureTestingModule({
      declarations: [ThemeComponent],
      imports: [HttpClientTestingModule],
      providers: [
        { provide: TopicService, useValue: topicServiceSpy }
      ]
    }).compileComponents();

    // Création de l'instance du composant à tester
    fixture = TestBed.createComponent(ThemeComponent);
    component = fixture.componentInstance;
  });

  // Test de création du composant
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test du chargement des thèmes à l'initialisation
  it('should load topics on init', (done) => {
    const topicsMock = [
      { id: 1, name: 'Thème 1', description: 'desc' },
      { id: 2, name: 'Thème 2', description: 'desc2' }
    ];
    topicServiceSpy.getAllTopics.and.returnValue(of(topicsMock));
    topicServiceSpy.getUserSubscriptions.and.returnValue(of([1]));

    component.ngOnInit();

    component.topics$.subscribe(topics => {
      expect(topics.length).toBe(2);
      expect(topics[0].name).toBe('Thème 1');
      done();
    });
  });

  // Test du chargement des abonnements utilisateur à l'initialisation
  it('should load user subscriptions on init', (done) => {
    topicServiceSpy.getAllTopics.and.returnValue(of([]));
    topicServiceSpy.getUserSubscriptions.and.returnValue(of([1, 2]));

    component.ngOnInit();

    component.subscribedTopicIds$.subscribe(ids => {
      expect(ids).toEqual([1, 2]);
      done();
    });
  });

  // Test de la méthode isSubscribed pour un thème abonné
  it('should return true if topic is subscribed', () => {
    expect(component.isSubscribed(1, [1, 2, 3])).toBeTrue();
  });

  // Test de la méthode isSubscribed pour un thème non abonné
  it('should return false if topic is not subscribed', () => {
    expect(component.isSubscribed(4, [1, 2, 3])).toBeFalse();
  });

  // Test de l'abonnement à un thème non encore abonné
  it('should call subscribeToTopic and refresh if not already subscribed', () => {
    topicServiceSpy.subscribeToTopic.and.returnValue(of({}));
    spyOn(component['refresh$'], 'next');
    component.toggleSubscription(4, [1, 2, 3]);
    expect(topicServiceSpy.subscribeToTopic).toHaveBeenCalledWith(4);
    expect(component['refresh$'].next).toHaveBeenCalled();
  });

  // Test de la non-action si déjà abonné au thème
  it('should not call subscribeToTopic if already subscribed', () => {
    topicServiceSpy.subscribeToTopic.and.returnValue(of({}));
    spyOn(component['refresh$'], 'next');
    component.toggleSubscription(2, [1, 2, 3]);
    expect(topicServiceSpy.subscribeToTopic).not.toHaveBeenCalled();
    expect(component['refresh$'].next).not.toHaveBeenCalled();
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