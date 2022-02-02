import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClienteService } from '../service/cliente.service';
import { ICliente, Cliente } from '../cliente.model';
import { IPenalizacion } from 'app/entities/penalizacion/penalizacion.model';
import { PenalizacionService } from 'app/entities/penalizacion/service/penalizacion.service';

import { ClienteUpdateComponent } from './cliente-update.component';

describe('Cliente Management Update Component', () => {
  let comp: ClienteUpdateComponent;
  let fixture: ComponentFixture<ClienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clienteService: ClienteService;
  let penalizacionService: PenalizacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ClienteUpdateComponent],
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
      .overrideTemplate(ClienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clienteService = TestBed.inject(ClienteService);
    penalizacionService = TestBed.inject(PenalizacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call penalizacion query and add missing value', () => {
      const cliente: ICliente = { id: 456 };
      const penalizacion: IPenalizacion = { id: 68405 };
      cliente.penalizacion = penalizacion;

      const penalizacionCollection: IPenalizacion[] = [{ id: 47628 }];
      jest.spyOn(penalizacionService, 'query').mockReturnValue(of(new HttpResponse({ body: penalizacionCollection })));
      const expectedCollection: IPenalizacion[] = [penalizacion, ...penalizacionCollection];
      jest.spyOn(penalizacionService, 'addPenalizacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(penalizacionService.query).toHaveBeenCalled();
      expect(penalizacionService.addPenalizacionToCollectionIfMissing).toHaveBeenCalledWith(penalizacionCollection, penalizacion);
      expect(comp.penalizacionsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cliente: ICliente = { id: 456 };
      const penalizacion: IPenalizacion = { id: 87081 };
      cliente.penalizacion = penalizacion;

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cliente));
      expect(comp.penalizacionsCollection).toContain(penalizacion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(clienteService.update).toHaveBeenCalledWith(cliente);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cliente>>();
      const cliente = new Cliente();
      jest.spyOn(clienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(clienteService.create).toHaveBeenCalledWith(cliente);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clienteService.update).toHaveBeenCalledWith(cliente);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPenalizacionById', () => {
      it('Should return tracked Penalizacion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPenalizacionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
