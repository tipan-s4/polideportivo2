import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InstalacionDetailComponent } from './instalacion-detail.component';

describe('Instalacion Management Detail Component', () => {
  let comp: InstalacionDetailComponent;
  let fixture: ComponentFixture<InstalacionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InstalacionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ instalacion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InstalacionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InstalacionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load instalacion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.instalacion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
