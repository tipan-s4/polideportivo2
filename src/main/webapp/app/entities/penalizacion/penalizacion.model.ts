import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IPenalizacion {
  id?: number;
  motivo?: string | null;
  totalAPagar?: number | null;
  cliente?: ICliente | null;
}

export class Penalizacion implements IPenalizacion {
  constructor(public id?: number, public motivo?: string | null, public totalAPagar?: number | null, public cliente?: ICliente | null) {}
}

export function getPenalizacionIdentifier(penalizacion: IPenalizacion): number | undefined {
  return penalizacion.id;
}
