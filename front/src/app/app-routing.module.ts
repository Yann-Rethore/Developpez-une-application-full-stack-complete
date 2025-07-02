import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ThemeComponent } from './pages/theme/theme.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ArticleComponent } from './pages/article/article.component';
import { ArticlesAbonnesComponent } from './pages/articles-abonnes/articles-abonnes.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';
import { AuthGuard } from './guards/auth.guard';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { ErrorComponent } from './pages/error/error.component';


// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [

  { path: '', component: HomeComponent }, // Accueil accessible à tous
  { path: 'login', component: LoginComponent }, // Login accessible à tous
  { path: 'register', component: RegisterComponent }, // Register accessible à tous

  // Toutes les autres routes protégées
  { path: 'themes', component: ThemeComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'article', component: ArticleComponent, canActivate: [AuthGuard] },
  { path: 'article/abonnes', component: ArticlesAbonnesComponent, canActivate: [AuthGuard] }, // Dynamic route for article details
  { path: 'article/:id', component: ArticleDetailComponent, canActivate: [AuthGuard] },
  { path: 'not-found', component: NotFoundComponent },
  { path: '**', redirectTo: 'not-found' },
  { path: 'error', component: ErrorComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
