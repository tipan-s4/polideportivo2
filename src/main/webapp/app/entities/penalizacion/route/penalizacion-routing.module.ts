import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PenalizacionComponent } from '../list/penalizacion.component';
import { PenalizacionDetailComponent } from '../detail/penalizacion-detail.component';
import { PenalizacionUpdateComponent } from '../update/penalizacion-update.component';
import { PenalizacionRoutingResolveService } from './penalizacion-routing-resolve.service';

const penalizacionRoute: Routes = [
  {
    path: '',
    component: PenalizacionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PenalizacionDetailComponent,
    resolve: {
      penalizacion: PenalizacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PenalizacionUpdateComponent,
    resolve: {
      penalizacion: PenalizacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PenalizacionUpdateComponent,
    resolve: {
      penalizacion: PenalizacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(penalizacionRoute)],
  exports: [RouterModule],
})
export class PenalizacionRoutingModule {}
