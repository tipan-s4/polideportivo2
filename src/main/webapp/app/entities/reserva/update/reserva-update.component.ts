import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReserva, Reserva } from '../reserva.model';
import { ReservaService } from '../service/reserva.service';
import { IRegistroMaterialUtilizado } from 'app/entities/registro-material-utilizado/registro-material-utilizado.model';
import { RegistroMaterialUtilizadoService } from 'app/entities/registro-material-utilizado/service/registro-material-utilizado.service';
import { IInstalacion } from 'app/entities/instalacion/instalacion.model';
import { InstalacionService } from 'app/entities/instalacion/service/instalacion.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

@Component({
  selector: 'jhi-reserva-update',
  templateUrl: './reserva-update.component.html',
})
export class ReservaUpdateComponent implements OnInit {
  isSaving = false;

  registrosCollection: IRegistroMaterialUtilizado[] = [];
  instalacionsCollection: IInstalacion[] = [];
  clientesSharedCollection: ICliente[] = [];

  editForm = this.fb.group({
    id: [],
    fecha: [],
    hora: [],
    tipoPago: [],
    total: [],
    registros: [],
    instalacion: [],
    cliente: [],
  });

  constructor(
    protected reservaService: ReservaService,
    protected registroMaterialUtilizadoService: RegistroMaterialUtilizadoService,
    protected instalacionService: InstalacionService,
    protected clienteService: ClienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reserva }) => {
      this.updateForm(reserva);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reserva = this.createFromForm();
    if (reserva.id !== undefined) {
      this.subscribeToSaveResponse(this.reservaService.update(reserva));
    } else {
      this.subscribeToSaveResponse(this.reservaService.create(reserva));
    }
  }

  trackRegistroMaterialUtilizadoById(index: number, item: IRegistroMaterialUtilizado): number {
    return item.id!;
  }

  trackInstalacionById(index: number, item: IInstalacion): number {
    return item.id!;
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReserva>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(reserva: IReserva): void {
    this.editForm.patchValue({
      id: reserva.id,
      fecha: reserva.fecha,
      hora: reserva.hora,
      tipoPago: reserva.tipoPago,
      total: reserva.total,
      registros: reserva.registros,
      instalacion: reserva.instalacion,
      cliente: reserva.cliente,
    });

    this.registrosCollection = this.registroMaterialUtilizadoService.addRegistroMaterialUtilizadoToCollectionIfMissing(
      this.registrosCollection,
      reserva.registros
    );
    this.instalacionsCollection = this.instalacionService.addInstalacionToCollectionIfMissing(
      this.instalacionsCollection,
      reserva.instalacion
    );
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, reserva.cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.registroMaterialUtilizadoService
      .query({ filter: 'reserva-is-null' })
      .pipe(map((res: HttpResponse<IRegistroMaterialUtilizado[]>) => res.body ?? []))
      .pipe(
        map((registroMaterialUtilizados: IRegistroMaterialUtilizado[]) =>
          this.registroMaterialUtilizadoService.addRegistroMaterialUtilizadoToCollectionIfMissing(
            registroMaterialUtilizados,
            this.editForm.get('registros')!.value
          )
        )
      )
      .subscribe((registroMaterialUtilizados: IRegistroMaterialUtilizado[]) => (this.registrosCollection = registroMaterialUtilizados));

    this.instalacionService
      .query({ filter: 'reservas-is-null' })
      .pipe(map((res: HttpResponse<IInstalacion[]>) => res.body ?? []))
      .pipe(
        map((instalacions: IInstalacion[]) =>
          this.instalacionService.addInstalacionToCollectionIfMissing(instalacions, this.editForm.get('instalacion')!.value)
        )
      )
      .subscribe((instalacions: IInstalacion[]) => (this.instalacionsCollection = instalacions));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }

  protected createFromForm(): IReserva {
    return {
      ...new Reserva(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      hora: this.editForm.get(['hora'])!.value,
      tipoPago: this.editForm.get(['tipoPago'])!.value,
      total: this.editForm.get(['total'])!.value,
      registros: this.editForm.get(['registros'])!.value,
      instalacion: this.editForm.get(['instalacion'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
    };
  }
}
