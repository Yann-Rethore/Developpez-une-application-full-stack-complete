import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TopicService } from './topic.service';
import { environment } from '../../environments/environment';
import { TopicDto } from '../interfaces/topic.dto';

describe('TopicService', () => {
  let service: TopicService;
  let httpMock: HttpTestingController;
  const apiUrl = `${environment.apiUrl}/themes`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TopicService]
    });
    service = TestBed.inject(TopicService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

   it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call GET on getAllTopics', () => {
    const mockTopics: TopicDto[] = [
      { id: 1, name: 'Thème 1', description: 'desc' },
      { id: 2, name: 'Thème 2', description: 'desc2' }
    ];
    service.getAllTopics().subscribe(topics => {
      expect(topics).toEqual(mockTopics);
    });
    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockTopics);
  });

  it('should call GET on subscribeToTopic', () => {
    service.subscribeToTopic(42).subscribe();
    const req = httpMock.expectOne(`${apiUrl}/subscribe/42`);
    expect(req.request.method).toBe('GET');
    req.flush({});
  });

  it('should call GET on unsubscribeFromTopic', () => {
    service.unsubscribeFromTopic(99).subscribe();
    const req = httpMock.expectOne(`${apiUrl}/unsubscribe/99`);
    expect(req.request.method).toBe('GET');
    req.flush({});
  });

  it('should call GET on getUserSubscriptions', () => {
    const mockIds = [1, 2, 3];
    service.getUserSubscriptions().subscribe(ids => {
      expect(ids).toEqual(mockIds);
    });
    const req = httpMock.expectOne(`${apiUrl}/me/subscriptions`);
    expect(req.request.method).toBe('GET');
    req.flush(mockIds);
  });
});