import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegistroMaterialUtilizado } from '../registro-material-utilizado.model';
import { RegistroMaterialUtilizadoService } from '../service/registro-material-utilizado.service';

@Component({
  templateUrl: './registro-material-utilizado-delete-dialog.component.html',
})
export class RegistroMaterialUtilizadoDeleteDialogComponent {
  registroMaterialUtilizado?: IRegistroMaterialUtilizado;

  constructor(protected registroMaterialUtilizadoService: RegistroMaterialUtilizadoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.registroMaterialUtilizadoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
