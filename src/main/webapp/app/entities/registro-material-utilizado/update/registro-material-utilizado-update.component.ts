import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRegistroMaterialUtilizado, RegistroMaterialUtilizado } from '../registro-material-utilizado.model';
import { RegistroMaterialUtilizadoService } from '../service/registro-material-utilizado.service';
import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';

@Component({
  selector: 'jhi-registro-material-utilizado-update',
  templateUrl: './registro-material-utilizado-update.component.html',
})
export class RegistroMaterialUtilizadoUpdateComponent implements OnInit {
  isSaving = false;

  materialsSharedCollection: IMaterial[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    cantidad: [],
    fecha: [],
    material: [],
  });

  constructor(
    protected registroMaterialUtilizadoService: RegistroMaterialUtilizadoService,
    protected materialService: MaterialService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registroMaterialUtilizado }) => {
      this.updateForm(registroMaterialUtilizado);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const registroMaterialUtilizado = this.createFromForm();
    if (registroMaterialUtilizado.id !== undefined) {
      this.subscribeToSaveResponse(this.registroMaterialUtilizadoService.update(registroMaterialUtilizado));
    } else {
      this.subscribeToSaveResponse(this.registroMaterialUtilizadoService.create(registroMaterialUtilizado));
    }
  }

  trackMaterialById(index: number, item: IMaterial): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegistroMaterialUtilizado>>): void {
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

  protected updateForm(registroMaterialUtilizado: IRegistroMaterialUtilizado): void {
    this.editForm.patchValue({
      id: registroMaterialUtilizado.id,
      nombre: registroMaterialUtilizado.nombre,
      cantidad: registroMaterialUtilizado.cantidad,
      fecha: registroMaterialUtilizado.fecha,
      material: registroMaterialUtilizado.material,
    });

    this.materialsSharedCollection = this.materialService.addMaterialToCollectionIfMissing(
      this.materialsSharedCollection,
      registroMaterialUtilizado.material
    );
  }

  protected loadRelationshipsOptions(): void {
    this.materialService
      .query()
      .pipe(map((res: HttpResponse<IMaterial[]>) => res.body ?? []))
      .pipe(
        map((materials: IMaterial[]) =>
          this.materialService.addMaterialToCollectionIfMissing(materials, this.editForm.get('material')!.value)
        )
      )
      .subscribe((materials: IMaterial[]) => (this.materialsSharedCollection = materials));
  }

  protected createFromForm(): IRegistroMaterialUtilizado {
    return {
      ...new RegistroMaterialUtilizado(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      material: this.editForm.get(['material'])!.value,
    };
  }
}
