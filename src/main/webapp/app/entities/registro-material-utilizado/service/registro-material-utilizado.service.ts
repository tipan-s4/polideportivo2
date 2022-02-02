import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRegistroMaterialUtilizado, getRegistroMaterialUtilizadoIdentifier } from '../registro-material-utilizado.model';

export type EntityResponseType = HttpResponse<IRegistroMaterialUtilizado>;
export type EntityArrayResponseType = HttpResponse<IRegistroMaterialUtilizado[]>;

@Injectable({ providedIn: 'root' })
export class RegistroMaterialUtilizadoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/registro-material-utilizados');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(registroMaterialUtilizado: IRegistroMaterialUtilizado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroMaterialUtilizado);
    return this.http
      .post<IRegistroMaterialUtilizado>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(registroMaterialUtilizado: IRegistroMaterialUtilizado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroMaterialUtilizado);
    return this.http
      .put<IRegistroMaterialUtilizado>(
        `${this.resourceUrl}/${getRegistroMaterialUtilizadoIdentifier(registroMaterialUtilizado) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(registroMaterialUtilizado: IRegistroMaterialUtilizado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroMaterialUtilizado);
    return this.http
      .patch<IRegistroMaterialUtilizado>(
        `${this.resourceUrl}/${getRegistroMaterialUtilizadoIdentifier(registroMaterialUtilizado) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRegistroMaterialUtilizado>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRegistroMaterialUtilizado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRegistroMaterialUtilizadoToCollectionIfMissing(
    registroMaterialUtilizadoCollection: IRegistroMaterialUtilizado[],
    ...registroMaterialUtilizadosToCheck: (IRegistroMaterialUtilizado | null | undefined)[]
  ): IRegistroMaterialUtilizado[] {
    const registroMaterialUtilizados: IRegistroMaterialUtilizado[] = registroMaterialUtilizadosToCheck.filter(isPresent);
    if (registroMaterialUtilizados.length > 0) {
      const registroMaterialUtilizadoCollectionIdentifiers = registroMaterialUtilizadoCollection.map(
        registroMaterialUtilizadoItem => getRegistroMaterialUtilizadoIdentifier(registroMaterialUtilizadoItem)!
      );
      const registroMaterialUtilizadosToAdd = registroMaterialUtilizados.filter(registroMaterialUtilizadoItem => {
        const registroMaterialUtilizadoIdentifier = getRegistroMaterialUtilizadoIdentifier(registroMaterialUtilizadoItem);
        if (
          registroMaterialUtilizadoIdentifier == null ||
          registroMaterialUtilizadoCollectionIdentifiers.includes(registroMaterialUtilizadoIdentifier)
        ) {
          return false;
        }
        registroMaterialUtilizadoCollectionIdentifiers.push(registroMaterialUtilizadoIdentifier);
        return true;
      });
      return [...registroMaterialUtilizadosToAdd, ...registroMaterialUtilizadoCollection];
    }
    return registroMaterialUtilizadoCollection;
  }

  protected convertDateFromClient(registroMaterialUtilizado: IRegistroMaterialUtilizado): IRegistroMaterialUtilizado {
    return Object.assign({}, registroMaterialUtilizado, {
      fecha: registroMaterialUtilizado.fecha?.isValid() ? registroMaterialUtilizado.fecha.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((registroMaterialUtilizado: IRegistroMaterialUtilizado) => {
        registroMaterialUtilizado.fecha = registroMaterialUtilizado.fecha ? dayjs(registroMaterialUtilizado.fecha) : undefined;
      });
    }
    return res;
  }
}
