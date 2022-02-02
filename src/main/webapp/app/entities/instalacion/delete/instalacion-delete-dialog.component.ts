import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInstalacion } from '../instalacion.model';
import { InstalacionService } from '../service/instalacion.service';

@Component({
  templateUrl: './instalacion-delete-dialog.component.html',
})
export class InstalacionDeleteDialogComponent {
  instalacion?: IInstalacion;

  constructor(protected instalacionService: InstalacionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.instalacionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
