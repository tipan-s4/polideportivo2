import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReservaService } from '../service/reserva.service';
import { IReserva, Reserva } from '../reserva.model';
import { IRegistroMaterialUtilizado } from 'app/entities/registro-material-utilizado/registro-material-utilizado.model';
import { RegistroMaterialUtilizadoService } from 'app/entities/registro-material-utilizado/service/registro-material-utilizado.service';
import { IInstalacion } from 'app/entities/instalacion/instalacion.model';
import { InstalacionService } from 'app/entities/instalacion/service/instalacion.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

import { ReservaUpdateComponent } from './reserva-update.component';

describe('Reserva Management Update Component', () => {
  let comp: ReservaUpdateComponent;
  let fixture: ComponentFixture<ReservaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reservaService: ReservaService;
  let registroMaterialUtilizadoService: RegistroMaterialUtilizadoService;
  let instalacionService: InstalacionService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReservaUpdateComponent],
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
      .overrideTemplate(ReservaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReservaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reservaService = TestBed.inject(ReservaService);
    registroMaterialUtilizadoService = TestBed.inject(RegistroMaterialUtilizadoService);
    instalacionService = TestBed.inject(InstalacionService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call registros query and add missing value', () => {
      const reserva: IReserva = { id: 456 };
      const registros: IRegistroMaterialUtilizado = { id: 20394 };
      reserva.registros = registros;

      const registrosCollection: IRegistroMaterialUtilizado[] = [{ id: 60936 }];
      jest.spyOn(registroMaterialUtilizadoService, 'query').mockReturnValue(of(new HttpResponse({ body: registrosCollection })));
      const expectedCollection: IRegistroMaterialUtilizado[] = [registros, ...registrosCollection];
      jest.spyOn(registroMaterialUtilizadoService, 'addRegistroMaterialUtilizadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reserva });
      comp.ngOnInit();

      expect(registroMaterialUtilizadoService.query).toHaveBeenCalled();
      expect(registroMaterialUtilizadoService.addRegistroMaterialUtilizadoToCollectionIfMissing).toHaveBeenCalledWith(
        registrosCollection,
        registros
      );
      expect(comp.registrosCollection).toEqual(expectedCollection);
    });

    it('Should call instalacion query and add missing value', () => {
      const reserva: IReserva = { id: 456 };
      const instalacion: IInstalacion = { id: 50407 };
      reserva.instalacion = instalacion;

      const instalacionCollection: IInstalacion[] = [{ id: 76834 }];
      jest.spyOn(instalacionService, 'query').mockReturnValue(of(new HttpResponse({ body: instalacionCollection })));
      const expectedCollection: IInstalacion[] = [instalacion, ...instalacionCollection];
      jest.spyOn(instalacionService, 'addInstalacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reserva });
      comp.ngOnInit();

      expect(instalacionService.query).toHaveBeenCalled();
      expect(instalacionService.addInstalacionToCollectionIfMissing).toHaveBeenCalledWith(instalacionCollection, instalacion);
      expect(comp.instalacionsCollection).toEqual(expectedCollection);
    });

    it('Should call Cliente query and add missing value', () => {
      const reserva: IReserva = { id: 456 };
      const cliente: ICliente = { id: 8967 };
      reserva.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 59986 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reserva });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(clienteCollection, ...additionalClientes);
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reserva: IReserva = { id: 456 };
      const registros: IRegistroMaterialUtilizado = { id: 86501 };
      reserva.registros = registros;
      const instalacion: IInstalacion = { id: 46320 };
      reserva.instalacion = instalacion;
      const cliente: ICliente = { id: 16056 };
      reserva.cliente = cliente;

      activatedRoute.data = of({ reserva });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(reserva));
      expect(comp.registrosCollection).toContain(registros);
      expect(comp.instalacionsCollection).toContain(instalacion);
      expect(comp.clientesSharedCollection).toContain(cliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Reserva>>();
      const reserva = { id: 123 };
      jest.spyOn(reservaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reserva });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reserva }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reservaService.update).toHaveBeenCalledWith(reserva);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Reserva>>();
      const reserva = new Reserva();
      jest.spyOn(reservaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reserva });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reserva }));
      saveSubject.complete();

      // THEN
      expect(reservaService.create).toHaveBeenCalledWith(reserva);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Reserva>>();
      const reserva = { id: 123 };
      jest.spyOn(reservaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reserva });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reservaService.update).toHaveBeenCalledWith(reserva);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRegistroMaterialUtilizadoById', () => {
      it('Should return tracked RegistroMaterialUtilizado primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRegistroMaterialUtilizadoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInstalacionById', () => {
      it('Should return tracked Instalacion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInstalacionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackClienteById', () => {
      it('Should return tracked Cliente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClienteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
