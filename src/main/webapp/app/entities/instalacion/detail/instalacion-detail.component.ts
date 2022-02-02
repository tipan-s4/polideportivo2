import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInstalacion } from '../instalacion.model';

@Component({
  selector: 'jhi-instalacion-detail',
  templateUrl: './instalacion-detail.component.html',
})
export class InstalacionDetailComponent implements OnInit {
  instalacion: IInstalacion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ instalacion }) => {
      this.instalacion = instalacion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
