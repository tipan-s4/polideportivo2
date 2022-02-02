import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'instalacion',
        data: { pageTitle: 'polideportivo2App.instalacion.home.title' },
        loadChildren: () => import('./instalacion/instalacion.module').then(m => m.InstalacionModule),
      },
      {
        path: 'material',
        data: { pageTitle: 'polideportivo2App.material.home.title' },
        loadChildren: () => import('./material/material.module').then(m => m.MaterialModule),
      },
      {
        path: 'registro-material-utilizado',
        data: { pageTitle: 'polideportivo2App.registroMaterialUtilizado.home.title' },
        loadChildren: () =>
          import('./registro-material-utilizado/registro-material-utilizado.module').then(m => m.RegistroMaterialUtilizadoModule),
      },
      {
        path: 'cliente',
        data: { pageTitle: 'polideportivo2App.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'reserva',
        data: { pageTitle: 'polideportivo2App.reserva.home.title' },
        loadChildren: () => import('./reserva/reserva.module').then(m => m.ReservaModule),
      },
      {
        path: 'penalizacion',
        data: { pageTitle: 'polideportivo2App.penalizacion.home.title' },
        loadChildren: () => import('./penalizacion/penalizacion.module').then(m => m.PenalizacionModule),
      },
      {
        path: 'horario',
        data: { pageTitle: 'polideportivo2App.horario.home.title' },
        loadChildren: () => import('./horario/horario.module').then(m => m.HorarioModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
