import { Component , OnInit, OnDestroy} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Subject, Observable, of, switchMap, catchError, map, startWith, filter, takeUntil } from 'rxjs';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

/**
 * Composant de gestion de la connexion utilisateur.
 * Affiche un formulaire de connexion et redirige l'utilisateur après authentification.
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  /** Formulaire de connexion */
  loginForm: FormGroup;
  /** Indique si le formulaire a été soumis */
  submitted = false;
  /** Subject pour déclencher la soumission du formulaire */
  private submit$ = new Subject<void>();
  /** Observable indiquant le succès de la connexion */
  success$!: Observable<boolean>;
  /** Subject pour gérer la destruction des subscriptions */
  private destroy$ = new Subject<void>();

  /**
   * Constructeur injectant les services nécessaires et initialisant le formulaire.
   * @param fb FormBuilder pour créer le formulaire
   * @param authService Service d'authentification
   * @param router Service de navigation
   * @param location Service pour revenir en arrière
   */
  constructor(
    private fb: FormBuilder, 
    private authService: AuthService, 
    private router: Router,
    private location: Location
  ) {
    // Initialisation du formulaire avec les champs requis
    this.loginForm = this.fb.group({
      identifier: ['', Validators.required],
      password: ['', Validators.required]
    });
  }
  
  /**
   * Initialise les observables pour la gestion de la connexion.
   * Si la connexion réussit, redirige vers la page des articles abonnés.
   */
  ngOnInit(): void {
    this.success$ = this.submit$.pipe(
      switchMap(() => {
        this.submitted = true;
        if (this.loginForm.valid) {
          return this.authService.login(this.loginForm.value).pipe(
            map((response) => {
              // Stocke le token d'authentification dans le localStorage
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

    // Redirige vers la page des articles abonnés en cas de succès
    this.success$.pipe(
      filter(success => success === true),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.router.navigate(['/article/abonnes']);
    });
  }

  /**
   * Déclenche la soumission du formulaire de connexion.
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