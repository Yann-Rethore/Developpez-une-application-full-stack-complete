import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { UserProfileDTO, UserProfileUpdateDTO } from '../../interfaces/user-profile.dto';
import { ProfileService } from '../../services/profile.service';
import { Subject, merge, switchMap, tap, takeUntil, startWith } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

function passwordComplexityValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value || '';
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
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {
  profileForm: FormGroup;

  profile$ = new Subject<void>();
  profile: UserProfileDTO | null = null;
  private update$ = new Subject<UserProfileUpdateDTO>();

  private unsubscribe$ = new Subject<number>();
  private destroy$ = new Subject<void>();

 constructor(private fb: FormBuilder, 
  private profileService: ProfileService,
  private router: Router,
  private authService: AuthService) {
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordComplexityValidator]]
    });
  }

  ngOnInit(): void {
    // Flux réactif pour charger le profil à chaque update ou désabonnement
    merge(
      this.profile$.pipe(startWith(void 0)),
      this.update$,
      this.unsubscribe$
    ).pipe(
      switchMap(() => this.profileService.getProfile()),
      tap(profile => {
        this.profile = profile;
        this.profileForm.patchValue({
          username: profile.username,
          email: profile.email,
          password: ''
        });
      }),
      takeUntil(this.destroy$)
    ).subscribe();
  }

   onSubmit() {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }
    const formValue = this.profileForm.value;
    const updates: UserProfileUpdateDTO = {
      username: formValue.username,
      email: formValue.email,
      password: formValue.password
    };
    this.profileService.updateProfile(updates)
    .pipe(takeUntil(this.destroy$))
    .subscribe(() => {
      this.authService.logout();
      this.router.navigate(['/']);
    });
  }

  unsubscribe(topicId: number) {
    this.profileService.updateProfile({ desabonnements: [topicId] })
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.unsubscribe$.next(topicId); // Déclenche le rechargement du profil
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}