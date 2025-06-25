import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfileComponent } from './profile.component';
import { ProfileService } from '../../services/profile.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let profileServiceSpy: jasmine.SpyObj<ProfileService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    // Création des spies pour les services utilisés par le composant
    profileServiceSpy = jasmine.createSpyObj('ProfileService', ['getProfile', 'updateProfile']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['logout']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    // Configuration du module de test avec les dépendances nécessaires
    await TestBed.configureTestingModule({
      declarations: [ProfileComponent],
      providers: [
        FormBuilder,
        { provide: ProfileService, useValue: profileServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    // Création de l'instance du composant à tester
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
  });

  // Test de création du composant
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test du chargement du profil à l'initialisation
  it('should load profile on init', () => {
    const profileMock = { username: 'user', email: 'user@mail.com', abonnements: [], password: '' };
    profileServiceSpy.getProfile.and.returnValue(of(profileMock));
    component.ngOnInit();
    expect(profileServiceSpy.getProfile).toHaveBeenCalled();
    // PatchValue est appelé, donc le formulaire doit contenir username et email
    expect(component.profileForm.value.username).toBe('user');
    expect(component.profileForm.value.email).toBe('user@mail.com');
  });

  // Test de la non-soumission si le formulaire est invalide
  it('should not submit if form is invalid', () => {
    spyOn(component.profileForm, 'markAllAsTouched');
    component.profileForm.setValue({ username: '', email: '', password: '' });
    component.onSubmit();
    expect(component.profileForm.markAllAsTouched).toHaveBeenCalled();
    expect(profileServiceSpy.updateProfile).not.toHaveBeenCalled();
  });

  // Test de la soumission et de la déconnexion si le formulaire est valide
  it('should submit and logout on valid form', () => {
    profileServiceSpy.updateProfile.and.returnValue(of({}));
    component.profileForm.setValue({ username: 'user', email: 'user@mail.com', password: 'Password1!' });
    component.onSubmit();
    expect(profileServiceSpy.updateProfile).toHaveBeenCalledWith({
      username: 'user',
      email: 'user@mail.com',
      password: 'Password1!'
    });
    expect(authServiceSpy.logout).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/']);
  });

  // Test du désabonnement à un thème
  it('should call updateProfile and trigger unsubscribe$ on unsubscribe', () => {
    profileServiceSpy.updateProfile.and.returnValue(of({}));
    spyOn(component['unsubscribe$'], 'next');
    component.unsubscribe(123);
    expect(profileServiceSpy.updateProfile).toHaveBeenCalledWith({ desabonnements: [123] });
    expect(component['unsubscribe$'].next).toHaveBeenCalledWith(123);
  });

  // Test du nettoyage des subscriptions lors de la destruction du composant
  it('should clean up destroy$ on ngOnDestroy', () => {
    spyOn(component['destroy$'], 'next');
    spyOn(component['destroy$'], 'complete');
    component.ngOnDestroy();
    expect(component['destroy$'].next).toHaveBeenCalled();
    expect(component['destroy$'].complete).toHaveBeenCalled();
  });
});