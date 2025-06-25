import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;
  let locationSpy: jasmine.SpyObj<Location>;

  beforeEach(async () => {
    // Création des spies pour les services utilisés par le composant
    authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    locationSpy = jasmine.createSpyObj('Location', ['back']);

    // Configuration du module de test avec les dépendances nécessaires
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: Location, useValue: locationSpy }
      ]
    }).compileComponents();

    // Création de l'instance du composant à tester
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Test de création du composant
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test de la validité du formulaire vide
  it('should have an invalid form when empty', () => {
    expect(component.loginForm.valid).toBeFalse();
  });

  // Test de l'appel à AuthService.login lors d'une soumission valide
  it('should call AuthService.login on valid submit', () => {
    component.loginForm.setValue({
      identifier: 'testuser',
      password: 'password'
    });
    authServiceSpy.login.and.returnValue(of({ token: 'abc' }));

    component.onSubmit();
    expect(authServiceSpy.login).toHaveBeenCalledWith({
      identifier: 'testuser',
      password: 'password'
    });
  });

  // Test du stockage du token dans le localStorage après une connexion réussie
  it('should store token in localStorage on successful login', (done) => {
    spyOn(localStorage, 'setItem');
    authServiceSpy.login.and.returnValue(of({ token: 'abc' }));
    component.ngOnInit();

    component.loginForm.setValue({
      identifier: 'testuser',
      password: 'password'
    });

    component.success$.subscribe(success => {
      if (success) {
        expect(localStorage.setItem).toHaveBeenCalledWith('authToken', 'abc');
        done();
      }
    });

    component.onSubmit();
  });

  // Test de la redirection après une connexion réussie
  it('should navigate to /article/abonnes on successful login', (done) => {
    component.loginForm.setValue({
      identifier: 'testuser',
      password: 'password'
    });
    authServiceSpy.login.and.returnValue(of({ token: 'abc' }));
    component.ngOnInit();

    component.success$.subscribe(success => {
      if (success) {
        expect(routerSpy.navigate).toHaveBeenCalledWith(['/article/abonnes']);
        done();
      }
    });

    component.onSubmit();
  });

  // Test du retour arrière via la méthode goBack
  it('should call location.back on goBack()', () => {
    component.goBack();
    expect(locationSpy.back).toHaveBeenCalled();
  });

  // Test de l'émission de false si le formulaire est invalide
  it('should emit false if form is invalid', (done) => {
    component.ngOnInit();
    // Formulaire invalide (champs vides)
    component.loginForm.setValue({ identifier: '', password: '' });

    component.success$.subscribe(success => {
      // La première émission est startWith(false), la seconde doit aussi être false
      if (component.submitted) {
        expect(success).toBeFalse();
        done();
      }
    });

    component.onSubmit();
  });

  // Test de l'émission de false si AuthService.login lève une erreur
  it('should emit false if AuthService.login throws an error', (done) => {
    authServiceSpy.login.and.returnValue(throwError(() => new Error('Erreur API')));
    component.ngOnInit();
    component.loginForm.setValue({ identifier: 'user', password: 'pass' });

    component.success$.subscribe(success => {
      if (component.submitted) {
        expect(success).toBeFalse();
        done();
      }
    });

    component.onSubmit();
  });

  // Test de l'émission de true après une connexion réussie
  it('should emit true on successful login', (done) => {
    authServiceSpy.login.and.returnValue(of({ token: 'abc' }));
    component.ngOnInit();
    component.loginForm.setValue({ identifier: 'user', password: 'pass' });
    component.success$.subscribe(success => {
      if (component.submitted && success) {
        expect(success).toBeTrue();
        done();
      }
    });
    component.onSubmit();
  });

  // Test du stockage du résultat complet si le token n'est pas défini dans la réponse
  it('should store response as token if response.token is undefined', (done) => {
    spyOn(localStorage, 'setItem');
    authServiceSpy.login.and.returnValue(of({}));
    component.ngOnInit();
    component.loginForm.setValue({ identifier: 'user', password: 'pass' });

    component.success$.subscribe(success => {
      if (component.submitted && success) {
        expect(localStorage.setItem).toHaveBeenCalledWith('authToken', JSON.stringify({}));
        done();
      }
    });

    component.onSubmit();
  });
});