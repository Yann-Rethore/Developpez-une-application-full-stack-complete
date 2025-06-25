import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { UserProfileDTO, UserProfileUpdateDTO } from '../../interfaces/user-profile.dto';
import { ProfileService } from '../../services/profile.service';
import { Subject, merge, switchMap, tap, takeUntil, startWith } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Validateur personnalisé pour vérifier la complexité du mot de passe.
 * Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.
 */
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

/**
 * Composant de gestion du profil utilisateur.
 * Permet d'afficher, de modifier le profil et de se désabonner de thèmes.
 */
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {
  /** Formulaire de modification du profil */
  profileForm: FormGroup;

  /** Subject pour déclencher le rechargement du profil */
  profile$ = new Subject<void>();
  /** Données du profil utilisateur courant */
  profile: UserProfileDTO | null = null;
  /** Subject pour déclencher la mise à jour du profil */
  private update$ = new Subject<UserProfileUpdateDTO>();
  /** Subject pour déclencher le rechargement après désabonnement */
  private unsubscribe$ = new Subject<number>();
  /** Subject pour gérer la destruction des subscriptions */
  private destroy$ = new Subject<void>();

  /**
   * Constructeur injectant les services nécessaires et initialisant le formulaire.
   * @param fb FormBuilder pour créer le formulaire
   * @param profileService Service pour gérer le profil utilisateur
   * @param router Service de navigation
   * @param authService Service d'authentification
   */
  constructor(
    private fb: FormBuilder, 
    private profileService: ProfileService,
    private router: Router,
    private authService: AuthService
  ) {
    // Initialisation du formulaire avec les champs requis et le validateur de mot de passe
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordComplexityValidator]]
    });
  }

  /**
   * Initialise le composant et charge le profil utilisateur.
   * Recharge le profil à chaque mise à jour ou désabonnement.
   */
  ngOnInit(): void {
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

  /**
   * Soumet le formulaire de modification du profil.
   * Si la modification réussit, déconnecte l'utilisateur et le redirige vers la page d'accueil.
   */
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

  /**
   * Désabonne l'utilisateur d'un thème et recharge le profil.
   * @param topicId Identifiant du thème à désabonner
   */
  unsubscribe(topicId: number) {
    this.profileService.updateProfile({ desabonnements: [topicId] })
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.unsubscribe$.next(topicId); // Déclenche le rechargement du profil
      });
  }

  /**
   * Nettoie les subscriptions lors de la destruction du composant.
   */
  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}