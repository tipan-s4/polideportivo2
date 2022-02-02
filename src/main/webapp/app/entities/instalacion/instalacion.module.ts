import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InstalacionComponent } from './list/instalacion.component';
import { InstalacionDetailComponent } from './detail/instalacion-detail.component';
import { InstalacionUpdateComponent } from './update/instalacion-update.component';
import { InstalacionDeleteDialogComponent } from './delete/instalacion-delete-dialog.component';
import { InstalacionRoutingModule } from './route/instalacion-routing.module';

@NgModule({
  imports: [SharedModule, InstalacionRoutingModule],
  declarations: [InstalacionComponent, InstalacionDetailComponent, InstalacionUpdateComponent, InstalacionDeleteDialogComponent],
  entryComponents: [InstalacionDeleteDialogComponent],
})
export class InstalacionModule {}
