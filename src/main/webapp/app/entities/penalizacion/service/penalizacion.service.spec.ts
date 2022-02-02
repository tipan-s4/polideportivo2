import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPenalizacion, Penalizacion } from '../penalizacion.model';

import { PenalizacionService } from './penalizacion.service';

describe('Penalizacion Service', () => {
  let service: PenalizacionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPenalizacion;
  let expectedResult: IPenalizacion | IPenalizacion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PenalizacionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      motivo: 'AAAAAAA',
      totalAPagar: 0,
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

    it('should create a Penalizacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Penalizacion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Penalizacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          motivo: 'BBBBBB',
          totalAPagar: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Penalizacion', () => {
      const patchObject = Object.assign({}, new Penalizacion());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Penalizacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          motivo: 'BBBBBB',
          totalAPagar: 1,
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

    it('should delete a Penalizacion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPenalizacionToCollectionIfMissing', () => {
      it('should add a Penalizacion to an empty array', () => {
        const penalizacion: IPenalizacion = { id: 123 };
        expectedResult = service.addPenalizacionToCollectionIfMissing([], penalizacion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(penalizacion);
      });

      it('should not add a Penalizacion to an array that contains it', () => {
        const penalizacion: IPenalizacion = { id: 123 };
        const penalizacionCollection: IPenalizacion[] = [
          {
            ...penalizacion,
          },
          { id: 456 },
        ];
        expectedResult = service.addPenalizacionToCollectionIfMissing(penalizacionCollection, penalizacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Penalizacion to an array that doesn't contain it", () => {
        const penalizacion: IPenalizacion = { id: 123 };
        const penalizacionCollection: IPenalizacion[] = [{ id: 456 }];
        expectedResult = service.addPenalizacionToCollectionIfMissing(penalizacionCollection, penalizacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(penalizacion);
      });

      it('should add only unique Penalizacion to an array', () => {
        const penalizacionArray: IPenalizacion[] = [{ id: 123 }, { id: 456 }, { id: 26614 }];
        const penalizacionCollection: IPenalizacion[] = [{ id: 123 }];
        expectedResult = service.addPenalizacionToCollectionIfMissing(penalizacionCollection, ...penalizacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const penalizacion: IPenalizacion = { id: 123 };
        const penalizacion2: IPenalizacion = { id: 456 };
        expectedResult = service.addPenalizacionToCollectionIfMissing([], penalizacion, penalizacion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(penalizacion);
        expect(expectedResult).toContain(penalizacion2);
      });

      it('should accept null and undefined values', () => {
        const penalizacion: IPenalizacion = { id: 123 };
        expectedResult = service.addPenalizacionToCollectionIfMissing([], null, penalizacion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(penalizacion);
      });

      it('should return initial array if no Penalizacion is added', () => {
        const penalizacionCollection: IPenalizacion[] = [{ id: 123 }];
        expectedResult = service.addPenalizacionToCollectionIfMissing(penalizacionCollection, undefined, null);
        expect(expectedResult).toEqual(penalizacionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
