import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { Router } from '@angular/router';

class RouterMock {
  url = '/';
  navigate = jasmine.createSpy('navigate');
}

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let routerMock: RouterMock;


beforeEach(async () => {
    routerMock = new RouterMock();

    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      providers: [
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

  fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
});

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return false for showHeader() on public routes', () => {
    routerMock.url = '/';
    expect(component.showHeader()).toBeFalse();
    routerMock.url = '/login';
    expect(component.showHeader()).toBeFalse();
    routerMock.url = '/register';
    expect(component.showHeader()).toBeFalse();
  });

  it('should return true for showHeader() on private routes', () => {
    routerMock.url = '/dashboard';
    expect(component.showHeader()).toBeTrue();
    routerMock.url = '/autre';
    expect(component.showHeader()).toBeTrue();
  });

  it('should return true for showPublicHeader() on public routes', () => {
    routerMock.url = '/';
    expect(component.showPublicHeader()).toBeTrue();
    routerMock.url = '/login';
    expect(component.showPublicHeader()).toBeTrue();
    routerMock.url = '/register';
    expect(component.showPublicHeader()).toBeTrue();
  });

  it('should return false for showPublicHeader() on private routes', () => {
    routerMock.url = '/dashboard';
    expect(component.showPublicHeader()).toBeFalse();
    routerMock.url = '/autre';
    expect(component.showPublicHeader()).toBeFalse();
  });
});