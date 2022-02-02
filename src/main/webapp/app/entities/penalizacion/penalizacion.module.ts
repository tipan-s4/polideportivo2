import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PenalizacionComponent } from './list/penalizacion.component';
import { PenalizacionDetailComponent } from './detail/penalizacion-detail.component';
import { PenalizacionUpdateComponent } from './update/penalizacion-update.component';
import { PenalizacionDeleteDialogComponent } from './delete/penalizacion-delete-dialog.component';
import { PenalizacionRoutingModule } from './route/penalizacion-routing.module';

@NgModule({
  imports: [SharedModule, PenalizacionRoutingModule],
  declarations: [PenalizacionComponent, PenalizacionDetailComponent, PenalizacionUpdateComponent, PenalizacionDeleteDialogComponent],
  entryComponents: [PenalizacionDeleteDialogComponent],
})
export class PenalizacionModule {}
