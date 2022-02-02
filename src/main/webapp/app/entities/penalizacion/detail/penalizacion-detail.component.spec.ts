import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PenalizacionDetailComponent } from './penalizacion-detail.component';

describe('Penalizacion Management Detail Component', () => {
  let comp: PenalizacionDetailComponent;
  let fixture: ComponentFixture<PenalizacionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PenalizacionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ penalizacion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PenalizacionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PenalizacionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load penalizacion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.penalizacion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
