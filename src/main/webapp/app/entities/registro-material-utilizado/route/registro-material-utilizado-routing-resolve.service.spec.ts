import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRegistroMaterialUtilizado, RegistroMaterialUtilizado } from '../registro-material-utilizado.model';
import { RegistroMaterialUtilizadoService } from '../service/registro-material-utilizado.service';

import { RegistroMaterialUtilizadoRoutingResolveService } from './registro-material-utilizado-routing-resolve.service';

describe('RegistroMaterialUtilizado routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RegistroMaterialUtilizadoRoutingResolveService;
  let service: RegistroMaterialUtilizadoService;
  let resultRegistroMaterialUtilizado: IRegistroMaterialUtilizado | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(RegistroMaterialUtilizadoRoutingResolveService);
    service = TestBed.inject(RegistroMaterialUtilizadoService);
    resultRegistroMaterialUtilizado = undefined;
  });

  describe('resolve', () => {
    it('should return IRegistroMaterialUtilizado returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRegistroMaterialUtilizado = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRegistroMaterialUtilizado).toEqual({ id: 123 });
    });

    it('should return new IRegistroMaterialUtilizado if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRegistroMaterialUtilizado = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRegistroMaterialUtilizado).toEqual(new RegistroMaterialUtilizado());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RegistroMaterialUtilizado })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRegistroMaterialUtilizado = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRegistroMaterialUtilizado).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
