import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPenalizacion } from '../penalizacion.model';

@Component({
  selector: 'jhi-penalizacion-detail',
  templateUrl: './penalizacion-detail.component.html',
})
export class PenalizacionDetailComponent implements OnInit {
  penalizacion: IPenalizacion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ penalizacion }) => {
      this.penalizacion = penalizacion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
