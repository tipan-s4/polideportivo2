import { IRegistroMaterialUtilizado } from 'app/entities/registro-material-utilizado/registro-material-utilizado.model';
import { IInstalacion } from 'app/entities/instalacion/instalacion.model';

export interface IMaterial {
  id?: number;
  nombre?: string | null;
  cantidadReservada?: number | null;
  cantidadDisponible?: number | null;
  registros?: IRegistroMaterialUtilizado[] | null;
  instalaciones?: IInstalacion | null;
}

export class Material implements IMaterial {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public cantidadReservada?: number | null,
    public cantidadDisponible?: number | null,
    public registros?: IRegistroMaterialUtilizado[] | null,
    public instalaciones?: IInstalacion | null
  ) {}
}

export function getMaterialIdentifier(material: IMaterial): number | undefined {
  return material.id;
}
