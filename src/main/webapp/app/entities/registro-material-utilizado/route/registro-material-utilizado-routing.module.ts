import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RegistroMaterialUtilizadoComponent } from '../list/registro-material-utilizado.component';
import { RegistroMaterialUtilizadoDetailComponent } from '../detail/registro-material-utilizado-detail.component';
import { RegistroMaterialUtilizadoUpdateComponent } from '../update/registro-material-utilizado-update.component';
import { RegistroMaterialUtilizadoRoutingResolveService } from './registro-material-utilizado-routing-resolve.service';

const registroMaterialUtilizadoRoute: Routes = [
  {
    path: '',
    component: RegistroMaterialUtilizadoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegistroMaterialUtilizadoDetailComponent,
    resolve: {
      registroMaterialUtilizado: RegistroMaterialUtilizadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegistroMaterialUtilizadoUpdateComponent,
    resolve: {
      registroMaterialUtilizado: RegistroMaterialUtilizadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegistroMaterialUtilizadoUpdateComponent,
    resolve: {
      registroMaterialUtilizado: RegistroMaterialUtilizadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(registroMaterialUtilizadoRoute)],
  exports: [RouterModule],
})
export class RegistroMaterialUtilizadoRoutingModule {}
