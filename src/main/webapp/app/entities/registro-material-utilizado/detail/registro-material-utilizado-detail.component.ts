import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegistroMaterialUtilizado } from '../registro-material-utilizado.model';

@Component({
  selector: 'jhi-registro-material-utilizado-detail',
  templateUrl: './registro-material-utilizado-detail.component.html',
})
export class RegistroMaterialUtilizadoDetailComponent implements OnInit {
  registroMaterialUtilizado: IRegistroMaterialUtilizado | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registroMaterialUtilizado }) => {
      this.registroMaterialUtilizado = registroMaterialUtilizado;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
