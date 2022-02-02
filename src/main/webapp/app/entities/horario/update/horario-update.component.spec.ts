import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HorarioService } from '../service/horario.service';
import { IHorario, Horario } from '../horario.model';
import { IInstalacion } from 'app/entities/instalacion/instalacion.model';
import { InstalacionService } from 'app/entities/instalacion/service/instalacion.service';

import { HorarioUpdateComponent } from './horario-update.component';

describe('Horario Management Update Component', () => {
  let comp: HorarioUpdateComponent;
  let fixture: ComponentFixture<HorarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let horarioService: HorarioService;
  let instalacionService: InstalacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HorarioUpdateComponent],
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
      .overrideTemplate(HorarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HorarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    horarioService = TestBed.inject(HorarioService);
    instalacionService = TestBed.inject(InstalacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Instalacion query and add missing value', () => {
      const horario: IHorario = { id: 456 };
      const instalacion: IInstalacion = { id: 74492 };
      horario.instalacion = instalacion;

      const instalacionCollection: IInstalacion[] = [{ id: 14370 }];
      jest.spyOn(instalacionService, 'query').mockReturnValue(of(new HttpResponse({ body: instalacionCollection })));
      const additionalInstalacions = [instalacion];
      const expectedCollection: IInstalacion[] = [...additionalInstalacions, ...instalacionCollection];
      jest.spyOn(instalacionService, 'addInstalacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ horario });
      comp.ngOnInit();

      expect(instalacionService.query).toHaveBeenCalled();
      expect(instalacionService.addInstalacionToCollectionIfMissing).toHaveBeenCalledWith(instalacionCollection, ...additionalInstalacions);
      expect(comp.instalacionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const horario: IHorario = { id: 456 };
      const instalacion: IInstalacion = { id: 72714 };
      horario.instalacion = instalacion;

      activatedRoute.data = of({ horario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(horario));
      expect(comp.instalacionsSharedCollection).toContain(instalacion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Horario>>();
      const horario = { id: 123 };
      jest.spyOn(horarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ horario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: horario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(horarioService.update).toHaveBeenCalledWith(horario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Horario>>();
      const horario = new Horario();
      jest.spyOn(horarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ horario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: horario }));
      saveSubject.complete();

      // THEN
      expect(horarioService.create).toHaveBeenCalledWith(horario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Horario>>();
      const horario = { id: 123 };
      jest.spyOn(horarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ horario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(horarioService.update).toHaveBeenCalledWith(horario);
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
