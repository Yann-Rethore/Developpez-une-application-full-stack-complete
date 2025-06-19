import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Subject, Observable, of, switchMap, catchError, startWith, map, filter } from 'rxjs';
import { AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

function passwordComplexityValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value || '';
  console.log('Password value:', value); // <-- Ajout du log
  const hasMinLength = value.length >= 8;
  const hasUpperCase = /[A-Z]/.test(value);
  const hasLowerCase = /[a-z]/.test(value);
  const hasNumber = /\d/.test(value);
  const hasSpecialChar = /[^A-Za-z0-9]/.test(value);

  return hasMinLength && hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar
    ? null
    : { passwordComplexity: true };
}


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  submitted = false;

  // Observable pour le statut de l'inscription
  private submit$ = new Subject<void>();
  success$!: Observable<boolean | null>;

  constructor(
    private fb: FormBuilder, 
    private authService: AuthService,
    private router: Router,
    private location: Location
  ) {
    this.registerForm = this.fb.group({
  username: ['', Validators.required],
  email: ['', [Validators.required, Validators.email]],
  password: ['', [Validators.required, passwordComplexityValidator]] 
});

    this.success$ = this.submit$.pipe(
      switchMap(() => {
        this.submitted = true;
        if (this.registerForm.valid) {
          return this.authService.register(this.registerForm.value).pipe(
            map(() => true),
            catchError(() => of(false))
          );
        }
        return of(false);
      }),
      startWith(false)
    );

    this.success$.pipe(
      filter(success => success === true)
    ).subscribe(() => {
      this.router.navigate(['/login']);
    });
  }

  onSubmit() {
    this.submit$.next();
  }

  goBack() {
    this.location.back();
  }
}