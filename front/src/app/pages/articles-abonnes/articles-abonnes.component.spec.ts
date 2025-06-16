import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticlesAbonnesComponent } from './articles-abonnes.component';

describe('ArticlesAbonnesComponent', () => {
  let component: ArticlesAbonnesComponent;
  let fixture: ComponentFixture<ArticlesAbonnesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArticlesAbonnesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArticlesAbonnesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
