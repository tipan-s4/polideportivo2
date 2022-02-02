import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MaterialService } from '../service/material.service';
import { IMaterial, Material } from '../material.model';
import { IInstalacion } from 'app/entities/instalacion/instalacion.model';
import { InstalacionService } from 'app/entities/instalacion/service/instalacion.service';

import { MaterialUpdateComponent } from './material-update.component';

describe('Material Management Update Component', () => {
  let comp: MaterialUpdateComponent;
  let fixture: ComponentFixture<MaterialUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialService: MaterialService;
  let instalacionService: InstalacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MaterialUpdateComponent],
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
      .overrideTemplate(MaterialUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialService = TestBed.inject(MaterialService);
    instalacionService = TestBed.inject(InstalacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instalacion query and add missing value', () => {
      const material: IMaterial = { id: 456 };
      const instalaciones: IInstalacion = { id: 68889 };
      material.instalaciones = instalaciones;

      const instalacionCollection: IInstalacion[] = [{ id: 79312 }];
      jest.spyOn(instalacionService, 'query').mockReturnValue(of(new HttpResponse({ body: instalacionCollection })));
      const additionalInstalacions = [instalaciones];
      const expectedCollection: IInstalacion[] = [...additionalInstalacions, ...instalacionCollection];
      jest.spyOn(instalacionService, 'addInstalacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ material });
      comp.ngOnInit();

      expect(instalacionService.query).toHaveBeenCalled();
      expect(instalacionService.addInstalacionToCollectionIfMissing).toHaveBeenCalledWith(instalacionCollection, ...additionalInstalacions);
      expect(comp.instalacionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const material: IMaterial = { id: 456 };
      const instalaciones: IInstalacion = { id: 29581 };
      material.instalaciones = instalaciones;

      activatedRoute.data = of({ material });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(material));
      expect(comp.instalacionsSharedCollection).toContain(instalaciones);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Material>>();
      const material = { id: 123 };
      jest.spyOn(materialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ material });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: material }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialService.update).toHaveBeenCalledWith(material);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Material>>();
      const material = new Material();
      jest.spyOn(materialService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ material });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: material }));
      saveSubject.complete();

      // THEN
      expect(materialService.create).toHaveBeenCalledWith(material);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Material>>();
      const material = { id: 123 };
      jest.spyOn(materialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ material });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialService.update).toHaveBeenCalledWith(material);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInstalacionById', () => {
      it('Should return tracked Instalacion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInstalacionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
