import { TestBed } from '@angular/core/testing';

import { TopicService } from './topic.service';

describe('ThemeService', () => {
  let service: TopicService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopicService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
