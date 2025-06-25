import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    // Création des spies pour AuthService et Router
    authServiceSpy = jasmine.createSpyObj('AuthService', ['isLoggedIn', 'logout']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    // Configuration du module de test avec les dépendances nécessaires
    await TestBed.configureTestingModule({
      declarations: [HeaderComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    // Création de l'instance du composant à tester
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
  });

  // Test de création du composant
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test de l'appel à AuthService.isLoggedIn dans la méthode isLoggedIn()
  it('should call AuthService.isLoggedIn in isLoggedIn()', () => {
    authServiceSpy.isLoggedIn.and.returnValue(true);
    expect(component.isLoggedIn()).toBeTrue();
    expect(authServiceSpy.isLoggedIn).toHaveBeenCalled();
  });

  // Test de la déconnexion et de la redirection vers la page d'accueil
  it('should call logout and navigate to "/"', () => {
    component.logout();
    expect(authServiceSpy.logout).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/']);
  });
});