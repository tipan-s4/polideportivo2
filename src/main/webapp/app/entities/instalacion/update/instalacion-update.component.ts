import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IInstalacion, Instalacion } from '../instalacion.model';
import { InstalacionService } from '../service/instalacion.service';

@Component({
  selector: 'jhi-instalacion-update',
  templateUrl: './instalacion-update.component.html',
})
export class InstalacionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    precioPorHora: [],
    disponible: [],
  });

  constructor(protected instalacionService: InstalacionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ instalacion }) => {
      this.updateForm(instalacion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const instalacion = this.createFromForm();
    if (instalacion.id !== undefined) {
      this.subscribeToSaveResponse(this.instalacionService.update(instalacion));
    } else {
      this.subscribeToSaveResponse(this.instalacionService.create(instalacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstalacion>>): void {
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

  protected updateForm(instalacion: IInstalacion): void {
    this.editForm.patchValue({
      id: instalacion.id,
      nombre: instalacion.nombre,
      precioPorHora: instalacion.precioPorHora,
      disponible: instalacion.disponible,
    });
  }

  protected createFromForm(): IInstalacion {
    return {
      ...new Instalacion(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      precioPorHora: this.editForm.get(['precioPorHora'])!.value,
      disponible: this.editForm.get(['disponible'])!.value,
    };
  }
}
