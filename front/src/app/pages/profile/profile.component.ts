import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UserProfileDTO, UserProfileUpdateDTO } from '../../interfaces/user-profile.dto';
import { ProfileService } from '../../services/profile.service';
import { Observable, Subject, merge, of, switchMap, tap, startWith } from 'rxjs';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  profileForm: FormGroup;
  profile$!: Observable<UserProfileDTO>;

  // Triggers pour les actions
  private updateProfile$ = new Subject<UserProfileUpdateDTO>();
  private unsubscribe$ = new Subject<number>();

  constructor(private fb: FormBuilder, private profileService: ProfileService) {
    this.profileForm = this.fb.group({
      username: [''],
      email: [''],
      password: ['']
    });
  }

  ngOnInit(): void {
    // Stream principal du profil utilisateur
    this.profile$ = merge(
      of(null), // Pour déclencher le chargement initial
      this.updateProfile$.pipe(
        switchMap(updates => this.profileService.updateProfile(updates).pipe(switchMap(() => of(null))))
      ),
      this.unsubscribe$.pipe(
        switchMap(topicId => this.profileService.updateProfile({ desabonnements: [topicId] }).pipe(switchMap(() => of(null))))
      )
    ).pipe(
      switchMap(() => this.profileService.getProfile()),
      tap(profile => {
        this.profileForm.patchValue({
          username: profile.username,
          email: profile.email,
          password: ''
        });
      })
    );
  }

  onSubmit() {
    const formValue = this.profileForm.value;
    const updates: UserProfileUpdateDTO = {};
    if (formValue.username) updates.username = formValue.username;
    if (formValue.email) updates.email = formValue.email;
    if (formValue.password) updates.password = formValue.password;
    this.updateProfile$.next(updates);
  }

  unsubscribe(topicId: number) {
    this.unsubscribe$.next(topicId);
  }
  
}