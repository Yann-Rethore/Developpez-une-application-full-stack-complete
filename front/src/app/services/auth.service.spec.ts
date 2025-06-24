import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const apiUrl = `${environment.apiUrl}/auth`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.removeItem('authToken'); // Nettoie le localStorage avant chaque test
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.removeItem('authToken'); // Nettoie après chaque test aussi
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call POST on login', () => {
    const credentials = { identifier: 'user', password: 'pass' };
    const mockResponse = { token: 'abc' };
    service.login(credentials).subscribe(res => {
      expect(res).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(`${apiUrl}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(credentials);
    req.flush(mockResponse);
  });

  it('should call POST on register', () => {
    const user = { username: 'user', email: 'mail@test.com', password: 'pass' };
    const mockResponse = {};
    service.register(user).subscribe(res => {
      expect(res).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(`${apiUrl}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(user);
    req.flush(mockResponse);
  });

  it('should return false if no token in localStorage', () => {
    expect(service.isLoggedIn()).toBeFalse();
  });

  it('should return true if token is in localStorage', () => {
    localStorage.setItem('authToken', 'abc');
    expect(service.isLoggedIn()).toBeTrue();
  });

  it('should remove token on logout', () => {
    localStorage.setItem('authToken', 'abc');
    service.logout();
    expect(localStorage.getItem('authToken')).toBeNull();
  });
});