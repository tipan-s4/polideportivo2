import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPenalizacion, Penalizacion } from '../penalizacion.model';
import { PenalizacionService } from '../service/penalizacion.service';

@Injectable({ providedIn: 'root' })
export class PenalizacionRoutingResolveService implements Resolve<IPenalizacion> {
  constructor(protected service: PenalizacionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPenalizacion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((penalizacion: HttpResponse<Penalizacion>) => {
          if (penalizacion.body) {
            return of(penalizacion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Penalizacion());
  }
}
