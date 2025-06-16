import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';

  constructor(private router: Router){}

  showHeader(): boolean {
    const hiddenRoutes = ['/', '/register', '/login'];
    return !hiddenRoutes.includes(this.router.url);
  }
  showPublicHeader(): boolean {
    const publicRoutes = ['/', '/register', '/login'];
    return publicRoutes.includes(this.router.url);
  }
}
