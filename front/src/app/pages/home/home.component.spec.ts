import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomeComponent } from './home.component';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let routerSpy: jasmine.SpyObj<Router>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['isLoggedIn']);

    await TestBed.configureTestingModule({
      declarations: [HomeComponent],
      imports: [HttpClientTestingModule],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to /article/abonnes if user is logged in', () => {
    authServiceSpy.isLoggedIn.and.returnValue(true);
    component.ngOnInit();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/article/abonnes']);
  });

  it('should not navigate if user is not logged in', () => {
    authServiceSpy.isLoggedIn.and.returnValue(false);
    component.ngOnInit();
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });
});
