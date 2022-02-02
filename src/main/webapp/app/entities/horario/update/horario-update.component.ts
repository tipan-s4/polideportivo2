import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IHorario, Horario } from '../horario.model';
import { HorarioService } from '../service/horario.service';
import { IInstalacion } from 'app/entities/instalacion/instalacion.model';
import { InstalacionService } from 'app/entities/instalacion/service/instalacion.service';

@Component({
  selector: 'jhi-horario-update',
  templateUrl: './horario-update.component.html',
})
export class HorarioUpdateComponent implements OnInit {
  isSaving = false;

  instalacionsSharedCollection: IInstalacion[] = [];

  editForm = this.fb.group({
    id: [],
    dia: [],
    hora: [],
    instalacion: [],
  });

  constructor(
    protected horarioService: HorarioService,
    protected instalacionService: InstalacionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ horario }) => {
      this.updateForm(horario);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const horario = this.createFromForm();
    if (horario.id !== undefined) {
      this.subscribeToSaveResponse(this.horarioService.update(horario));
    } else {
      this.subscribeToSaveResponse(this.horarioService.create(horario));
    }
  }

  trackInstalacionById(index: number, item: IInstalacion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHorario>>): void {
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

  protected updateForm(horario: IHorario): void {
    this.editForm.patchValue({
      id: horario.id,
      dia: horario.dia,
      hora: horario.hora,
      instalacion: horario.instalacion,
    });

    this.instalacionsSharedCollection = this.instalacionService.addInstalacionToCollectionIfMissing(
      this.instalacionsSharedCollection,
      horario.instalacion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instalacionService
      .query()
      .pipe(map((res: HttpResponse<IInstalacion[]>) => res.body ?? []))
      .pipe(
        map((instalacions: IInstalacion[]) =>
          this.instalacionService.addInstalacionToCollectionIfMissing(instalacions, this.editForm.get('instalacion')!.value)
        )
      )
      .subscribe((instalacions: IInstalacion[]) => (this.instalacionsSharedCollection = instalacions));
  }

  protected createFromForm(): IHorario {
    return {
      ...new Horario(),
      id: this.editForm.get(['id'])!.value,
      dia: this.editForm.get(['dia'])!.value,
      hora: this.editForm.get(['hora'])!.value,
      instalacion: this.editForm.get(['instalacion'])!.value,
    };
  }
}
