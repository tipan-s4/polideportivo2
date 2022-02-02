import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RegistroMaterialUtilizadoComponent } from './list/registro-material-utilizado.component';
import { RegistroMaterialUtilizadoDetailComponent } from './detail/registro-material-utilizado-detail.component';
import { RegistroMaterialUtilizadoUpdateComponent } from './update/registro-material-utilizado-update.component';
import { RegistroMaterialUtilizadoDeleteDialogComponent } from './delete/registro-material-utilizado-delete-dialog.component';
import { RegistroMaterialUtilizadoRoutingModule } from './route/registro-material-utilizado-routing.module';

@NgModule({
  imports: [SharedModule, RegistroMaterialUtilizadoRoutingModule],
  declarations: [
    RegistroMaterialUtilizadoComponent,
    RegistroMaterialUtilizadoDetailComponent,
    RegistroMaterialUtilizadoUpdateComponent,
    RegistroMaterialUtilizadoDeleteDialogComponent,
  ],
  entryComponents: [RegistroMaterialUtilizadoDeleteDialogComponent],
})
export class RegistroMaterialUtilizadoModule {}
