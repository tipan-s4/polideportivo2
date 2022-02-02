import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPenalizacion, Penalizacion } from '../penalizacion.model';
import { PenalizacionService } from '../service/penalizacion.service';

@Component({
  selector: 'jhi-penalizacion-update',
  templateUrl: './penalizacion-update.component.html',
})
export class PenalizacionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    motivo: [],
    totalAPagar: [],
  });

  constructor(protected penalizacionService: PenalizacionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ penalizacion }) => {
      this.updateForm(penalizacion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const penalizacion = this.createFromForm();
    if (penalizacion.id !== undefined) {
      this.subscribeToSaveResponse(this.penalizacionService.update(penalizacion));
    } else {
      this.subscribeToSaveResponse(this.penalizacionService.create(penalizacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPenalizacion>>): void {
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

  protected updateForm(penalizacion: IPenalizacion): void {
    this.editForm.patchValue({
      id: penalizacion.id,
      motivo: penalizacion.motivo,
      totalAPagar: penalizacion.totalAPagar,
    });
  }

  protected createFromForm(): IPenalizacion {
    return {
      ...new Penalizacion(),
      id: this.editForm.get(['id'])!.value,
      motivo: this.editForm.get(['motivo'])!.value,
      totalAPagar: this.editForm.get(['totalAPagar'])!.value,
    };
  }
}
