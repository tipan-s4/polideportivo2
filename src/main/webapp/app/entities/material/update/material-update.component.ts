import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMaterial, Material } from '../material.model';
import { MaterialService } from '../service/material.service';
import { IInstalacion } from 'app/entities/instalacion/instalacion.model';
import { InstalacionService } from 'app/entities/instalacion/service/instalacion.service';

@Component({
  selector: 'jhi-material-update',
  templateUrl: './material-update.component.html',
})
export class MaterialUpdateComponent implements OnInit {
  isSaving = false;

  instalacionsSharedCollection: IInstalacion[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    cantidadReservada: [],
    cantidadDisponible: [],
    instalaciones: [],
  });

  constructor(
    protected materialService: MaterialService,
    protected instalacionService: InstalacionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ material }) => {
      this.updateForm(material);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const material = this.createFromForm();
    if (material.id !== undefined) {
      this.subscribeToSaveResponse(this.materialService.update(material));
    } else {
      this.subscribeToSaveResponse(this.materialService.create(material));
    }
  }

  trackInstalacionById(index: number, item: IInstalacion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterial>>): void {
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

  protected updateForm(material: IMaterial): void {
    this.editForm.patchValue({
      id: material.id,
      nombre: material.nombre,
      cantidadReservada: material.cantidadReservada,
      cantidadDisponible: material.cantidadDisponible,
      instalaciones: material.instalaciones,
    });

    this.instalacionsSharedCollection = this.instalacionService.addInstalacionToCollectionIfMissing(
      this.instalacionsSharedCollection,
      material.instalaciones
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instalacionService
      .query()
      .pipe(map((res: HttpResponse<IInstalacion[]>) => res.body ?? []))
      .pipe(
        map((instalacions: IInstalacion[]) =>
          this.instalacionService.addInstalacionToCollectionIfMissing(instalacions, this.editForm.get('instalaciones')!.value)
        )
      )
      .subscribe((instalacions: IInstalacion[]) => (this.instalacionsSharedCollection = instalacions));
  }

  protected createFromForm(): IMaterial {
    return {
      ...new Material(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      cantidadReservada: this.editForm.get(['cantidadReservada'])!.value,
      cantidadDisponible: this.editForm.get(['cantidadDisponible'])!.value,
      instalaciones: this.editForm.get(['instalaciones'])!.value,
    };
  }
}
