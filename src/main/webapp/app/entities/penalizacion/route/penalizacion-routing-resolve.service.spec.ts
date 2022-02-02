import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPenalizacion, Penalizacion } from '../penalizacion.model';
import { PenalizacionService } from '../service/penalizacion.service';

import { PenalizacionRoutingResolveService } from './penalizacion-routing-resolve.service';

describe('Penalizacion routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PenalizacionRoutingResolveService;
  let service: PenalizacionService;
  let resultPenalizacion: IPenalizacion | undefined;

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
    routingResolveService = TestBed.inject(PenalizacionRoutingResolveService);
    service = TestBed.inject(PenalizacionService);
    resultPenalizacion = undefined;
  });

  describe('resolve', () => {
    it('should return IPenalizacion returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPenalizacion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPenalizacion).toEqual({ id: 123 });
    });

    it('should return new IPenalizacion if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPenalizacion = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPenalizacion).toEqual(new Penalizacion());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Penalizacion })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPenalizacion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPenalizacion).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
