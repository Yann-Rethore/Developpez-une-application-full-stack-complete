import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { of } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;
  let locationSpy: jasmine.SpyObj<Location>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['register']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    locationSpy = jasmine.createSpyObj('Location', ['back']);

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: Location, useValue: locationSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have an invalid form when empty', () => {
    expect(component.registerForm.valid).toBeFalse();
  });

  it('should validate password complexity', () => {
    const passwordControl = component.registerForm.controls['password'];
    passwordControl.setValue('simple');
    expect(passwordControl.valid).toBeFalse();

    passwordControl.setValue('Complexe1!');
    expect(passwordControl.valid).toBeTrue();
  });

  it('should call AuthService.register on valid submit', () => {
    component.registerForm.setValue({
      username: 'testuser',
      email: 'test@email.com',
      password: 'Complexe1!'
    });
    authServiceSpy.register.and.returnValue(of({}));

    component.onSubmit();
    expect(authServiceSpy.register).toHaveBeenCalled();
  });

  it('should navigate to /login on successful registration', (done) => {
  // Arrange

  authServiceSpy.register.and.returnValue(of({}));
  component.ngOnInit();

  component.registerForm.setValue({
    username: 'testuser',
    email: 'test@email.com',
    password: 'Complexe1!'
  });

  component.onSubmit();

  // Assert
 setTimeout(() => {
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
    done();
  }, 0);
});

  it('should call location.back on goBack()', () => {
    component.goBack();
    expect(locationSpy.back).toHaveBeenCalled();
  });
});