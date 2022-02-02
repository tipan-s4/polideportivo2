import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaterialDetailComponent } from './material-detail.component';

describe('Material Management Detail Component', () => {
  let comp: MaterialDetailComponent;
  let fixture: ComponentFixture<MaterialDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MaterialDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ material: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MaterialDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MaterialDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load material on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.material).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
