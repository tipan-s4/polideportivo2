import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InstalacionService } from '../service/instalacion.service';
import { IInstalacion, Instalacion } from '../instalacion.model';

import { InstalacionUpdateComponent } from './instalacion-update.component';

describe('Instalacion Management Update Component', () => {
  let comp: InstalacionUpdateComponent;
  let fixture: ComponentFixture<InstalacionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let instalacionService: InstalacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InstalacionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InstalacionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InstalacionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    instalacionService = TestBed.inject(InstalacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const instalacion: IInstalacion = { id: 456 };

      activatedRoute.data = of({ instalacion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(instalacion));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Instalacion>>();
      const instalacion = { id: 123 };
      jest.spyOn(instalacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instalacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: instalacion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(instalacionService.update).toHaveBeenCalledWith(instalacion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Instalacion>>();
      const instalacion = new Instalacion();
      jest.spyOn(instalacionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instalacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: instalacion }));
      saveSubject.complete();

      // THEN
      expect(instalacionService.create).toHaveBeenCalledWith(instalacion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Instalacion>>();
      const instalacion = { id: 123 };
      jest.spyOn(instalacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ instalacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(instalacionService.update).toHaveBeenCalledWith(instalacion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
