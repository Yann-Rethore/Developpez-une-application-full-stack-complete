import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Subject, Observable, of, switchMap, catchError, startWith, map, filter, takeUntil } from 'rxjs';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

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
 * Composant de gestion de l'inscription utilisateur.
 * Affiche un formulaire d'inscription, valide les champs et redirige vers la page de connexion après succès.
 */
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {
  /** Formulaire d'inscription */
  registerForm: FormGroup;
  /** Indique si le formulaire a été soumis */
  submitted = false;
  /** Subject pour déclencher la soumission du formulaire */
  private submit$ = new Subject<void>();
  /** Observable indiquant le succès de l'inscription */
  success$!: Observable<boolean | null>;
  /** Subject pour gérer la destruction des subscriptions */
  private destroy$ = new Subject<void>();

  /**
   * Constructeur injectant les services nécessaires et initialisant le formulaire.
   * @param fb FormBuilder pour créer le formulaire
   * @param authService Service d'authentification pour l'inscription
   * @param router Service de navigation
   * @param location Service pour revenir en arrière
   */
  constructor(
    private fb: FormBuilder, 
    private authService: AuthService,
    private router: Router,
    private location: Location
  ) {
    // Initialisation du formulaire avec les champs requis et le validateur de mot de passe
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordComplexityValidator]] 
    });
  }

  /**
   * Initialise les observables pour la gestion de l'inscription.
   * Si l'inscription réussit, redirige vers la page de connexion.
   */
  ngOnInit(): void {
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
      startWith(false),
      takeUntil(this.destroy$)
    );

    // Redirige vers la page de connexion en cas de succès
    this.success$.pipe(
      filter(success => success === true),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.router.navigate(['/login']);
    });
  }

  /**
   * Déclenche la soumission du formulaire d'inscription.
   */
  onSubmit() {
    this.submit$.next();
  }

  /**
   * Retourne à la page précédente.
   */
  goBack() {
    this.location.back();
  }

  /**
   * Nettoie les subscriptions lors de la destruction du composant.
   */
  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}