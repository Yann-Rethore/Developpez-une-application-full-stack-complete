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
    topicServiceSpy = jasmine.createSpyObj('TopicService', [
      'getAllTopics',
      'getUserSubscriptions',
      'subscribeToTopic'
    ]);

    await TestBed.configureTestingModule({
      declarations: [ThemeComponent],
      imports: [HttpClientTestingModule],
      providers: [
        { provide: TopicService, useValue: topicServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ThemeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

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

  it('should load user subscriptions on init', (done) => {
    topicServiceSpy.getAllTopics.and.returnValue(of([]));
    topicServiceSpy.getUserSubscriptions.and.returnValue(of([1, 2]));

    component.ngOnInit();

    component.subscribedTopicIds$.subscribe(ids => {
      expect(ids).toEqual([1, 2]);
      done();
    });
  });

  it('should return true if topic is subscribed', () => {
    expect(component.isSubscribed(1, [1, 2, 3])).toBeTrue();
  });

  it('should return false if topic is not subscribed', () => {
    expect(component.isSubscribed(4, [1, 2, 3])).toBeFalse();
  });

  it('should call subscribeToTopic and refresh if not already subscribed', () => {
    topicServiceSpy.subscribeToTopic.and.returnValue(of({}));
    spyOn(component['refresh$'], 'next');
    component.toggleSubscription(4, [1, 2, 3]);
    expect(topicServiceSpy.subscribeToTopic).toHaveBeenCalledWith(4);
    expect(component['refresh$'].next).toHaveBeenCalled();
  });

  it('should not call subscribeToTopic if already subscribed', () => {
    topicServiceSpy.subscribeToTopic.and.returnValue(of({}));
    spyOn(component['refresh$'], 'next');
    component.toggleSubscription(2, [1, 2, 3]);
    expect(topicServiceSpy.subscribeToTopic).not.toHaveBeenCalled();
    expect(component['refresh$'].next).not.toHaveBeenCalled();
  });

  it('should clean up destroy$ on ngOnDestroy', () => {
    spyOn(component['destroy$'], 'next');
    spyOn(component['destroy$'], 'complete');
    component.ngOnDestroy();
    expect(component['destroy$'].next).toHaveBeenCalled();
    expect(component['destroy$'].complete).toHaveBeenCalled();
  });
    
  
});