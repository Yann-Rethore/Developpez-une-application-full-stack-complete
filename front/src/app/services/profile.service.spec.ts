import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProfileService } from './profile.service';
import { environment } from '../../environments/environment';
import { UserProfileDTO, UserProfileUpdateDTO } from '../interfaces/user-profile.dto';

describe('ProfileService', () => {
  let service: ProfileService;
  let httpMock: HttpTestingController;
  const apiUrl = `${environment.apiUrl}/profile`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProfileService]
    });
    service = TestBed.inject(ProfileService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call GET on getProfile', () => {
    const mockProfile: UserProfileDTO = {
      username: 'user',
      email: 'mail@test.com',
      abonnements: []
    };
    service.getProfile().subscribe(profile => {
      expect(profile).toEqual(mockProfile);
    });
    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockProfile);
  });

  it('should call PUT on updateProfile', () => {
    const update: UserProfileUpdateDTO = { username: 'newuser' };
    const mockResponse = {};
    service.updateProfile(update).subscribe(res => {
      expect(res).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(update);
    req.flush(mockResponse);
  });
});