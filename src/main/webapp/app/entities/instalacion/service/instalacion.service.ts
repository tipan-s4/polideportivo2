import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInstalacion, getInstalacionIdentifier } from '../instalacion.model';

export type EntityResponseType = HttpResponse<IInstalacion>;
export type EntityArrayResponseType = HttpResponse<IInstalacion[]>;

@Injectable({ providedIn: 'root' })
export class InstalacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/instalacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(instalacion: IInstalacion): Observable<EntityResponseType> {
    return this.http.post<IInstalacion>(this.resourceUrl, instalacion, { observe: 'response' });
  }

  update(instalacion: IInstalacion): Observable<EntityResponseType> {
    return this.http.put<IInstalacion>(`${this.resourceUrl}/${getInstalacionIdentifier(instalacion) as number}`, instalacion, {
      observe: 'response',
    });
  }

  partialUpdate(instalacion: IInstalacion): Observable<EntityResponseType> {
    return this.http.patch<IInstalacion>(`${this.resourceUrl}/${getInstalacionIdentifier(instalacion) as number}`, instalacion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInstalacion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInstalacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInstalacionToCollectionIfMissing(
    instalacionCollection: IInstalacion[],
    ...instalacionsToCheck: (IInstalacion | null | undefined)[]
  ): IInstalacion[] {
    const instalacions: IInstalacion[] = instalacionsToCheck.filter(isPresent);
    if (instalacions.length > 0) {
      const instalacionCollectionIdentifiers = instalacionCollection.map(instalacionItem => getInstalacionIdentifier(instalacionItem)!);
      const instalacionsToAdd = instalacions.filter(instalacionItem => {
        const instalacionIdentifier = getInstalacionIdentifier(instalacionItem);
        if (instalacionIdentifier == null || instalacionCollectionIdentifiers.includes(instalacionIdentifier)) {
          return false;
        }
        instalacionCollectionIdentifiers.push(instalacionIdentifier);
        return true;
      });
      return [...instalacionsToAdd, ...instalacionCollection];
    }
    return instalacionCollection;
  }
}
