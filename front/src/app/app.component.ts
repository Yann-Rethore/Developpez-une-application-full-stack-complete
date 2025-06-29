import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';
  isMobile = false;

  constructor(private router: Router) {
    this.checkScreen();
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreen();
  }

  checkScreen() {
    // Détecte mobile/tablette ou portrait
    this.isMobile = window.innerWidth <= 768 || window.innerHeight > window.innerWidth;
  }

  showHeader(): boolean {
    const hiddenRoutes = ['/', '/register', '/login'];
    return !hiddenRoutes.includes(this.router.url);
  }

  showPublicHeader(): boolean {
    const publicRoutes = ['/register', '/login'];
    // Affiche le header public seulement si on est sur desktop
    return publicRoutes.includes(this.router.url) && !this.isMobile;
  }
}
