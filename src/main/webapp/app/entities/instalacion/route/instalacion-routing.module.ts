import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InstalacionComponent } from '../list/instalacion.component';
import { InstalacionDetailComponent } from '../detail/instalacion-detail.component';
import { InstalacionUpdateComponent } from '../update/instalacion-update.component';
import { InstalacionRoutingResolveService } from './instalacion-routing-resolve.service';

const instalacionRoute: Routes = [
  {
    path: '',
    component: InstalacionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InstalacionDetailComponent,
    resolve: {
      instalacion: InstalacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InstalacionUpdateComponent,
    resolve: {
      instalacion: InstalacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InstalacionUpdateComponent,
    resolve: {
      instalacion: InstalacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(instalacionRoute)],
  exports: [RouterModule],
})
export class InstalacionRoutingModule {}
