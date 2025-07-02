import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { ThemeComponent } from './pages/theme/theme.component';
import { RegisterComponent } from './pages/register/register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './pages/login/login.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { HttpErrorInterceptor } from './interceptors/http-error.interceptor';
import { HeaderComponent } from './shared/header/header.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ArticleComponent } from './pages/article/article.component';
import { ArticlesAbonnesComponent } from './pages/articles-abonnes/articles-abonnes.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';
import { MatIconModule } from '@angular/material/icon';
import { HeaderPublicComponent } from './shared/header-public/header-public.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { ErrorComponent } from './pages/error/error.component';




@NgModule({
  declarations: [AppComponent, HomeComponent, ThemeComponent, RegisterComponent, LoginComponent, HeaderComponent, ProfileComponent, ArticleComponent, ArticlesAbonnesComponent, ArticleDetailComponent, HeaderPublicComponent, NotFoundComponent, ErrorComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatIconModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true}
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
