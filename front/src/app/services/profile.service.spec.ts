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
    // Configure le module de test avec HttpClientTestingModule pour intercepter les requêtes HTTP
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProfileService]
    });
    service = TestBed.inject(ProfileService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Vérifie qu'aucune requête HTTP n'est en attente après chaque test
    httpMock.verify();
  });

  // Test de création du service
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test de l'appel GET pour récupérer le profil utilisateur
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

  // Test de l'appel PUT pour mettre à jour le profil utilisateur
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