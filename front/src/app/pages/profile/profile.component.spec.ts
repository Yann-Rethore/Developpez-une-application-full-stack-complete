
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfileComponent } from './profile.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ProfileService } from '../../services/profile.service';
import { of, Subscription } from 'rxjs';

;


describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let profileServiceSpy: jasmine.SpyObj<ProfileService>;

  beforeEach(async () => {
    profileServiceSpy = jasmine.createSpyObj('ProfileService', ['getProfile', 'updateProfile']);

    await TestBed.configureTestingModule({
      declarations: [ProfileComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: ProfileService, useValue: profileServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should patch form with profile data on init', () => {
    const profileMock = {
      username: 'user',
      email: 'mail@test.com',
      password: '',
      abonnements: [] 
  }
    profileServiceSpy.getProfile.and.returnValue(of(profileMock));
    profileServiceSpy.updateProfile.and.returnValue(of(profileMock));

    component.ngOnInit();

    component.profile$.subscribe(profile => {
      expect(component.profileForm.value.username).toBe('user');
      expect(component.profileForm.value.email).toBe('mail@test.com');
    });
  });

  it('should send updates on submit', () => {
    profileServiceSpy.getProfile.and.returnValue(of({ username: '', email: '', password: '', abonnements: [] }));
    profileServiceSpy.updateProfile.and.returnValue(of({}));

    component.ngOnInit();
    component.profileForm.setValue({ username: 'newuser', email: 'new@mail.com', password: 'pass' });
    spyOn(component['updateProfile$'], 'next');

    component.onSubmit();

    expect(component['updateProfile$'].next).toHaveBeenCalledWith({
      username: 'newuser',
      email: 'new@mail.com',
      password: 'pass'
    });
  });

  it('should send unsubscribe on unsubscribe()', () => {
    spyOn(component['unsubscribe$'], 'next');
    component.unsubscribe(42);
    expect(component['unsubscribe$'].next).toHaveBeenCalledWith(42);
  });

  it('should complete destroy$ on ngOnDestroy', () => {
    spyOn(component['destroy$'], 'next');
    spyOn(component['destroy$'], 'complete');
    component.ngOnDestroy();
    expect(component['destroy$'].next).toHaveBeenCalled();
    expect(component['destroy$'].complete).toHaveBeenCalled();
  });



it('should call updateProfile via updateProfile$ stream', (done) => {
  const profileMock = {
    username: 'user',
    email: 'mail@test.com',
    password: '',
    abonnements: []
  };
  profileServiceSpy.getProfile.and.returnValue(of(profileMock));
  profileServiceSpy.updateProfile.and.returnValue(of({}));

  component.ngOnInit();

  let called = false;
  let sub: Subscription;
  sub = component.profile$.subscribe(() => {
    if (!called && profileServiceSpy.updateProfile.calls.any()) {
      expect(profileServiceSpy.updateProfile).toHaveBeenCalledWith(jasmine.objectContaining({
        username: 'newuser',
        email: 'new@mail.com',
        password: 'pass'
      }));
      called = true;
      sub.unsubscribe();
      done();
    }
  });

  component.profileForm.setValue({
    username: 'newuser',
    email: 'new@mail.com',
    password: 'pass'
  });
  component.onSubmit();
});
});