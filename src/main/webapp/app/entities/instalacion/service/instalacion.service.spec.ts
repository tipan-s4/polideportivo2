import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInstalacion, Instalacion } from '../instalacion.model';

import { InstalacionService } from './instalacion.service';

describe('Instalacion Service', () => {
  let service: InstalacionService;
  let httpMock: HttpTestingController;
  let elemDefault: IInstalacion;
  let expectedResult: IInstalacion | IInstalacion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InstalacionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      precioPorHora: 0,
      disponible: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Instalacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Instalacion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Instalacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          precioPorHora: 1,
          disponible: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Instalacion', () => {
      const patchObject = Object.assign(
        {
          disponible: true,
        },
        new Instalacion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Instalacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          precioPorHora: 1,
          disponible: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Instalacion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInstalacionToCollectionIfMissing', () => {
      it('should add a Instalacion to an empty array', () => {
        const instalacion: IInstalacion = { id: 123 };
        expectedResult = service.addInstalacionToCollectionIfMissing([], instalacion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instalacion);
      });

      it('should not add a Instalacion to an array that contains it', () => {
        const instalacion: IInstalacion = { id: 123 };
        const instalacionCollection: IInstalacion[] = [
          {
            ...instalacion,
          },
          { id: 456 },
        ];
        expectedResult = service.addInstalacionToCollectionIfMissing(instalacionCollection, instalacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Instalacion to an array that doesn't contain it", () => {
        const instalacion: IInstalacion = { id: 123 };
        const instalacionCollection: IInstalacion[] = [{ id: 456 }];
        expectedResult = service.addInstalacionToCollectionIfMissing(instalacionCollection, instalacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instalacion);
      });

      it('should add only unique Instalacion to an array', () => {
        const instalacionArray: IInstalacion[] = [{ id: 123 }, { id: 456 }, { id: 75376 }];
        const instalacionCollection: IInstalacion[] = [{ id: 123 }];
        expectedResult = service.addInstalacionToCollectionIfMissing(instalacionCollection, ...instalacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const instalacion: IInstalacion = { id: 123 };
        const instalacion2: IInstalacion = { id: 456 };
        expectedResult = service.addInstalacionToCollectionIfMissing([], instalacion, instalacion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instalacion);
        expect(expectedResult).toContain(instalacion2);
      });

      it('should accept null and undefined values', () => {
        const instalacion: IInstalacion = { id: 123 };
        expectedResult = service.addInstalacionToCollectionIfMissing([], null, instalacion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instalacion);
      });

      it('should return initial array if no Instalacion is added', () => {
        const instalacionCollection: IInstalacion[] = [{ id: 123 }];
        expectedResult = service.addInstalacionToCollectionIfMissing(instalacionCollection, undefined, null);
        expect(expectedResult).toEqual(instalacionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
