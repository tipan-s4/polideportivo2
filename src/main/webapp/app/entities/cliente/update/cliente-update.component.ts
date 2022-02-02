import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICliente, Cliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { IPenalizacion } from 'app/entities/penalizacion/penalizacion.model';
import { PenalizacionService } from 'app/entities/penalizacion/service/penalizacion.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;

  penalizacionsCollection: IPenalizacion[] = [];

  editForm = this.fb.group({
    id: [],
    dni: [],
    nombre: [],
    apellidos: [],
    telefono: [],
    direccion: [],
    edad: [],
    penalizacion: [],
  });

  constructor(
    protected clienteService: ClienteService,
    protected penalizacionService: PenalizacionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.updateForm(cliente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  trackPenalizacionById(index: number, item: IPenalizacion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      id: cliente.id,
      dni: cliente.dni,
      nombre: cliente.nombre,
      apellidos: cliente.apellidos,
      telefono: cliente.telefono,
      direccion: cliente.direccion,
      edad: cliente.edad,
      penalizacion: cliente.penalizacion,
    });

    this.penalizacionsCollection = this.penalizacionService.addPenalizacionToCollectionIfMissing(
      this.penalizacionsCollection,
      cliente.penalizacion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.penalizacionService
      .query({ filter: 'cliente-is-null' })
      .pipe(map((res: HttpResponse<IPenalizacion[]>) => res.body ?? []))
      .pipe(
        map((penalizacions: IPenalizacion[]) =>
          this.penalizacionService.addPenalizacionToCollectionIfMissing(penalizacions, this.editForm.get('penalizacion')!.value)
        )
      )
      .subscribe((penalizacions: IPenalizacion[]) => (this.penalizacionsCollection = penalizacions));
  }

  protected createFromForm(): ICliente {
    return {
      ...new Cliente(),
      id: this.editForm.get(['id'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      edad: this.editForm.get(['edad'])!.value,
      penalizacion: this.editForm.get(['penalizacion'])!.value,
    };
  }
}
