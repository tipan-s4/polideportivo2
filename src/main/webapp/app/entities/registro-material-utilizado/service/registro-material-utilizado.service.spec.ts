import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRegistroMaterialUtilizado, RegistroMaterialUtilizado } from '../registro-material-utilizado.model';

import { RegistroMaterialUtilizadoService } from './registro-material-utilizado.service';

describe('RegistroMaterialUtilizado Service', () => {
  let service: RegistroMaterialUtilizadoService;
  let httpMock: HttpTestingController;
  let elemDefault: IRegistroMaterialUtilizado;
  let expectedResult: IRegistroMaterialUtilizado | IRegistroMaterialUtilizado[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RegistroMaterialUtilizadoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      cantidad: 0,
      fecha: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RegistroMaterialUtilizado', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.create(new RegistroMaterialUtilizado()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RegistroMaterialUtilizado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          cantidad: 1,
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RegistroMaterialUtilizado', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          cantidad: 1,
        },
        new RegistroMaterialUtilizado()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RegistroMaterialUtilizado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          cantidad: 1,
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RegistroMaterialUtilizado', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRegistroMaterialUtilizadoToCollectionIfMissing', () => {
      it('should add a RegistroMaterialUtilizado to an empty array', () => {
        const registroMaterialUtilizado: IRegistroMaterialUtilizado = { id: 123 };
        expectedResult = service.addRegistroMaterialUtilizadoToCollectionIfMissing([], registroMaterialUtilizado);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registroMaterialUtilizado);
      });

      it('should not add a RegistroMaterialUtilizado to an array that contains it', () => {
        const registroMaterialUtilizado: IRegistroMaterialUtilizado = { id: 123 };
        const registroMaterialUtilizadoCollection: IRegistroMaterialUtilizado[] = [
          {
            ...registroMaterialUtilizado,
          },
          { id: 456 },
        ];
        expectedResult = service.addRegistroMaterialUtilizadoToCollectionIfMissing(
          registroMaterialUtilizadoCollection,
          registroMaterialUtilizado
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RegistroMaterialUtilizado to an array that doesn't contain it", () => {
        const registroMaterialUtilizado: IRegistroMaterialUtilizado = { id: 123 };
        const registroMaterialUtilizadoCollection: IRegistroMaterialUtilizado[] = [{ id: 456 }];
        expectedResult = service.addRegistroMaterialUtilizadoToCollectionIfMissing(
          registroMaterialUtilizadoCollection,
          registroMaterialUtilizado
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registroMaterialUtilizado);
      });

      it('should add only unique RegistroMaterialUtilizado to an array', () => {
        const registroMaterialUtilizadoArray: IRegistroMaterialUtilizado[] = [{ id: 123 }, { id: 456 }, { id: 4257 }];
        const registroMaterialUtilizadoCollection: IRegistroMaterialUtilizado[] = [{ id: 123 }];
        expectedResult = service.addRegistroMaterialUtilizadoToCollectionIfMissing(
          registroMaterialUtilizadoCollection,
          ...registroMaterialUtilizadoArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const registroMaterialUtilizado: IRegistroMaterialUtilizado = { id: 123 };
        const registroMaterialUtilizado2: IRegistroMaterialUtilizado = { id: 456 };
        expectedResult = service.addRegistroMaterialUtilizadoToCollectionIfMissing(
          [],
          registroMaterialUtilizado,
          registroMaterialUtilizado2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(registroMaterialUtilizado);
        expect(expectedResult).toContain(registroMaterialUtilizado2);
      });

      it('should accept null and undefined values', () => {
        const registroMaterialUtilizado: IRegistroMaterialUtilizado = { id: 123 };
        expectedResult = service.addRegistroMaterialUtilizadoToCollectionIfMissing([], null, registroMaterialUtilizado, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(registroMaterialUtilizado);
      });

      it('should return initial array if no RegistroMaterialUtilizado is added', () => {
        const registroMaterialUtilizadoCollection: IRegistroMaterialUtilizado[] = [{ id: 123 }];
        expectedResult = service.addRegistroMaterialUtilizadoToCollectionIfMissing(registroMaterialUtilizadoCollection, undefined, null);
        expect(expectedResult).toEqual(registroMaterialUtilizadoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
