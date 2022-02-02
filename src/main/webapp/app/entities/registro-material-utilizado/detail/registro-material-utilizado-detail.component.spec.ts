import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RegistroMaterialUtilizadoDetailComponent } from './registro-material-utilizado-detail.component';

describe('RegistroMaterialUtilizado Management Detail Component', () => {
  let comp: RegistroMaterialUtilizadoDetailComponent;
  let fixture: ComponentFixture<RegistroMaterialUtilizadoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistroMaterialUtilizadoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ registroMaterialUtilizado: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RegistroMaterialUtilizadoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RegistroMaterialUtilizadoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load registroMaterialUtilizado on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.registroMaterialUtilizado).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
