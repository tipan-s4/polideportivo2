import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstalacion, Instalacion } from '../instalacion.model';
import { InstalacionService } from '../service/instalacion.service';

@Injectable({ providedIn: 'root' })
export class InstalacionRoutingResolveService implements Resolve<IInstalacion> {
  constructor(protected service: InstalacionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInstalacion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((instalacion: HttpResponse<Instalacion>) => {
          if (instalacion.body) {
            return of(instalacion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Instalacion());
  }
}
