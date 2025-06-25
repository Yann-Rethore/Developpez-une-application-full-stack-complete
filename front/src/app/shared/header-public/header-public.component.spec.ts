import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderPublicComponent } from './header-public.component';

describe('HeaderPublicComponent', () => {
  let component: HeaderPublicComponent;
  let fixture: ComponentFixture<HeaderPublicComponent>;

  beforeEach(async () => {
    // Configuration du module de test avec le composant à tester
    await TestBed.configureTestingModule({
      declarations: [HeaderPublicComponent]
    }).compileComponents();

    // Création de l'instance du composant à tester
    fixture = TestBed.createComponent(HeaderPublicComponent);
    component = fixture.componentInstance;
  });

  // Test de création du composant
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test de l'appel de ngOnInit (même si aucune logique n'est présente)
  it('should call ngOnInit without error', () => {
    expect(() => component.ngOnInit()).not.toThrow();
  });
});