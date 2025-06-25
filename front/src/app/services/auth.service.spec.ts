import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const apiUrl = `${environment.apiUrl}/auth`;

  beforeEach(() => {
    // Configuration du module de test avec HttpClientTestingModule
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.removeItem('authToken'); // Nettoie le localStorage avant chaque test
  });

  afterEach(() => {
    // Vérifie qu'aucune requête HTTP n'est en attente et nettoie le localStorage
    httpMock.verify();
    localStorage.removeItem('authToken');
  });

  // Test de création du service
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test de l'appel POST lors de la connexion
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

  // Test de l'appel POST lors de l'inscription
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

  // Test de la méthode isLoggedIn quand aucun token n'est présent
  it('should return false if no token in localStorage', () => {
    expect(service.isLoggedIn()).toBeFalse();
  });

  // Test de la méthode isLoggedIn quand un token est présent
  it('should return true if token is in localStorage', () => {
    localStorage.setItem('authToken', 'abc');
    expect(service.isLoggedIn()).toBeTrue();
  });

  // Test de la méthode logout
  it('should remove token on logout', () => {
    localStorage.setItem('authToken', 'abc');
    service.logout();
    expect(localStorage.getItem('authToken')).toBeNull();
  });
});