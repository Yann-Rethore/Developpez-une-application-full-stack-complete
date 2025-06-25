import { TestBed } from '@angular/core/testing';
import { AuthGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

describe('AuthGuard', () => {
  let guard: AuthGuard;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    // Création des spies pour AuthService et Router
    authServiceSpy = jasmine.createSpyObj('AuthService', ['isLoggedIn']);
    routerSpy = jasmine.createSpyObj('Router', ['createUrlTree']);

    // Configuration du module de test avec les dépendances nécessaires
    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    // Injection du guard à tester
    guard = TestBed.inject(AuthGuard);
  });

  // Test : l'accès est autorisé si l'utilisateur est connecté
  it('should allow activation if user is logged in', () => {
    authServiceSpy.isLoggedIn.and.returnValue(true);
    expect(guard.canActivate()).toBeTrue();
  });

  // Test : l'accès est refusé et redirige vers /login si l'utilisateur n'est pas connecté
  it('should redirect to /login if user is not logged in', () => {
    authServiceSpy.isLoggedIn.and.returnValue(false);
    const urlTree = {} as unknown as import('@angular/router').UrlTree;
    routerSpy.createUrlTree.and.returnValue(urlTree);
    expect(guard.canActivate()).toBe(urlTree);
    expect(routerSpy.createUrlTree).toHaveBeenCalledWith(['/login']);
  });
});