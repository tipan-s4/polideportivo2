import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPenalizacion } from '../penalizacion.model';
import { PenalizacionService } from '../service/penalizacion.service';

@Component({
  templateUrl: './penalizacion-delete-dialog.component.html',
})
export class PenalizacionDeleteDialogComponent {
  penalizacion?: IPenalizacion;

  constructor(protected penalizacionService: PenalizacionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.penalizacionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
