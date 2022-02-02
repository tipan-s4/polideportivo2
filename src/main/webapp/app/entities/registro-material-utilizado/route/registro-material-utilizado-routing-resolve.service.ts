import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRegistroMaterialUtilizado, RegistroMaterialUtilizado } from '../registro-material-utilizado.model';
import { RegistroMaterialUtilizadoService } from '../service/registro-material-utilizado.service';

@Injectable({ providedIn: 'root' })
export class RegistroMaterialUtilizadoRoutingResolveService implements Resolve<IRegistroMaterialUtilizado> {
  constructor(protected service: RegistroMaterialUtilizadoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRegistroMaterialUtilizado> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((registroMaterialUtilizado: HttpResponse<RegistroMaterialUtilizado>) => {
          if (registroMaterialUtilizado.body) {
            return of(registroMaterialUtilizado.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RegistroMaterialUtilizado());
  }
}
