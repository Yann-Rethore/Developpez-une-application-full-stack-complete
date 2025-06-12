import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  submitted = false;
  error = false;

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.loginForm = this.fb.group({
      identifier: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
  this.submitted = true;
  if (this.loginForm.valid) {
    this.authService.login(this.loginForm.value).subscribe({
      next: (response) => {
        // Supposons que le token est dans response.token
        localStorage.setItem('authToken', response.token);
        this.error = false;
        // Redirection ou autre logique ici
      },
      error: () => this.error = true
    });
  }
}}