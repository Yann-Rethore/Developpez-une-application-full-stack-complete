import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Subject, Observable, of, switchMap, catchError, map, startWith, filter } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  submitted = false;

  private submit$ = new Subject<void>();
  success$!: Observable<boolean>;

  constructor(
    private fb: FormBuilder, 
    private authService: AuthService, 
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      identifier: ['', Validators.required],
      password: ['', Validators.required]
    });

  this.success$ = this.submit$.pipe(
      switchMap(() => {
        this.submitted = true;
        if (this.loginForm.valid) {
          return this.authService.login(this.loginForm.value).pipe(
            map((response) => {
              localStorage.setItem('authToken', response.token ?? response);
              return true;
            }),
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
      this.router.navigate(['/article/abonnes']);
    });
  }

  onSubmit() {
    this.submit$.next();
  }
}