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
    authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    locationSpy = jasmine.createSpyObj('Location', ['back']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: Location, useValue: locationSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have an invalid form when empty', () => {
    expect(component.loginForm.valid).toBeFalse();
  });

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

  it('should call location.back on goBack()', () => {
    component.goBack();
    expect(locationSpy.back).toHaveBeenCalled();
  });

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