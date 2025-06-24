import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderPublicComponent } from './header-public.component';

describe('HeaderPublicComponent', () => {
  let component: HeaderPublicComponent;
  let fixture: ComponentFixture<HeaderPublicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HeaderPublicComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderPublicComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call ngOnInit without error', () => {
    expect(() => component.ngOnInit()).not.toThrow();
  });
});
