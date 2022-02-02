import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPenalizacion, getPenalizacionIdentifier } from '../penalizacion.model';

export type EntityResponseType = HttpResponse<IPenalizacion>;
export type EntityArrayResponseType = HttpResponse<IPenalizacion[]>;

@Injectable({ providedIn: 'root' })
export class PenalizacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/penalizacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(penalizacion: IPenalizacion): Observable<EntityResponseType> {
    return this.http.post<IPenalizacion>(this.resourceUrl, penalizacion, { observe: 'response' });
  }

  update(penalizacion: IPenalizacion): Observable<EntityResponseType> {
    return this.http.put<IPenalizacion>(`${this.resourceUrl}/${getPenalizacionIdentifier(penalizacion) as number}`, penalizacion, {
      observe: 'response',
    });
  }

  partialUpdate(penalizacion: IPenalizacion): Observable<EntityResponseType> {
    return this.http.patch<IPenalizacion>(`${this.resourceUrl}/${getPenalizacionIdentifier(penalizacion) as number}`, penalizacion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPenalizacion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPenalizacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPenalizacionToCollectionIfMissing(
    penalizacionCollection: IPenalizacion[],
    ...penalizacionsToCheck: (IPenalizacion | null | undefined)[]
  ): IPenalizacion[] {
    const penalizacions: IPenalizacion[] = penalizacionsToCheck.filter(isPresent);
    if (penalizacions.length > 0) {
      const penalizacionCollectionIdentifiers = penalizacionCollection.map(
        penalizacionItem => getPenalizacionIdentifier(penalizacionItem)!
      );
      const penalizacionsToAdd = penalizacions.filter(penalizacionItem => {
        const penalizacionIdentifier = getPenalizacionIdentifier(penalizacionItem);
        if (penalizacionIdentifier == null || penalizacionCollectionIdentifiers.includes(penalizacionIdentifier)) {
          return false;
        }
        penalizacionCollectionIdentifiers.push(penalizacionIdentifier);
        return true;
      });
      return [...penalizacionsToAdd, ...penalizacionCollection];
    }
    return penalizacionCollection;
  }
}
