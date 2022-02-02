import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RegistroMaterialUtilizadoService } from '../service/registro-material-utilizado.service';
import { IRegistroMaterialUtilizado, RegistroMaterialUtilizado } from '../registro-material-utilizado.model';
import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';

import { RegistroMaterialUtilizadoUpdateComponent } from './registro-material-utilizado-update.component';

describe('RegistroMaterialUtilizado Management Update Component', () => {
  let comp: RegistroMaterialUtilizadoUpdateComponent;
  let fixture: ComponentFixture<RegistroMaterialUtilizadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let registroMaterialUtilizadoService: RegistroMaterialUtilizadoService;
  let materialService: MaterialService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RegistroMaterialUtilizadoUpdateComponent],
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
      .overrideTemplate(RegistroMaterialUtilizadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegistroMaterialUtilizadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    registroMaterialUtilizadoService = TestBed.inject(RegistroMaterialUtilizadoService);
    materialService = TestBed.inject(MaterialService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Material query and add missing value', () => {
      const registroMaterialUtilizado: IRegistroMaterialUtilizado = { id: 456 };
      const material: IMaterial = { id: 97222 };
      registroMaterialUtilizado.material = material;

      const materialCollection: IMaterial[] = [{ id: 69739 }];
      jest.spyOn(materialService, 'query').mockReturnValue(of(new HttpResponse({ body: materialCollection })));
      const additionalMaterials = [material];
      const expectedCollection: IMaterial[] = [...additionalMaterials, ...materialCollection];
      jest.spyOn(materialService, 'addMaterialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ registroMaterialUtilizado });
      comp.ngOnInit();

      expect(materialService.query).toHaveBeenCalled();
      expect(materialService.addMaterialToCollectionIfMissing).toHaveBeenCalledWith(materialCollection, ...additionalMaterials);
      expect(comp.materialsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const registroMaterialUtilizado: IRegistroMaterialUtilizado = { id: 456 };
      const material: IMaterial = { id: 31368 };
      registroMaterialUtilizado.material = material;

      activatedRoute.data = of({ registroMaterialUtilizado });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(registroMaterialUtilizado));
      expect(comp.materialsSharedCollection).toContain(material);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RegistroMaterialUtilizado>>();
      const registroMaterialUtilizado = { id: 123 };
      jest.spyOn(registroMaterialUtilizadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroMaterialUtilizado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registroMaterialUtilizado }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(registroMaterialUtilizadoService.update).toHaveBeenCalledWith(registroMaterialUtilizado);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RegistroMaterialUtilizado>>();
      const registroMaterialUtilizado = new RegistroMaterialUtilizado();
      jest.spyOn(registroMaterialUtilizadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroMaterialUtilizado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: registroMaterialUtilizado }));
      saveSubject.complete();

      // THEN
      expect(registroMaterialUtilizadoService.create).toHaveBeenCalledWith(registroMaterialUtilizado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RegistroMaterialUtilizado>>();
      const registroMaterialUtilizado = { id: 123 };
      jest.spyOn(registroMaterialUtilizadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ registroMaterialUtilizado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(registroMaterialUtilizadoService.update).toHaveBeenCalledWith(registroMaterialUtilizado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMaterialById', () => {
      it('Should return tracked Material primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMaterialById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
