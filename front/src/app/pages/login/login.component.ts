import { Component , OnInit, OnDestroy} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Subject, Observable, of, switchMap, catchError, map, startWith, filter, takeUntil } from 'rxjs';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm: FormGroup;
  submitted = false;
  private submit$ = new Subject<void>();
  success$!: Observable<boolean>;
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder, 
    private authService: AuthService, 
    private router: Router,
    private location: Location
  ) {
    this.loginForm = this.fb.group({
      identifier: ['', Validators.required],
      password: ['', Validators.required]
    });
  }
  
  ngOnInit(): void {
  this.success$ = this.submit$.pipe(
      switchMap(() => {
        this.submitted = true;
        if (this.loginForm.valid) {
          return this.authService.login(this.loginForm.value).pipe(
            map((response) => {
              localStorage.setItem(
                'authToken',
                response.token !== undefined && response.token !== null
                ? response.token
                : JSON.stringify(response)
              );
              return true;
            }),
            catchError(() => of(false))
          );
        }
        return of(false);
      }),
      startWith(false),
      takeUntil(this.destroy$)

    );

    this.success$.pipe(
      filter(success => success === true),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.router.navigate(['/article/abonnes']);
    });
  }

  onSubmit() {
    this.submit$.next();
  }

  goBack() {
    this.location.back();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}