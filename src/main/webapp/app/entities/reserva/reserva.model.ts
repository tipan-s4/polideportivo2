import dayjs from 'dayjs/esm';
import { IRegistroMaterialUtilizado } from 'app/entities/registro-material-utilizado/registro-material-utilizado.model';
import { IInstalacion } from 'app/entities/instalacion/instalacion.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IReserva {
  id?: number;
  fecha?: dayjs.Dayjs | null;
  hora?: number | null;
  tipoPago?: string | null;
  total?: number | null;
  registros?: IRegistroMaterialUtilizado | null;
  instalacion?: IInstalacion | null;
  cliente?: ICliente | null;
}

export class Reserva implements IReserva {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs | null,
    public hora?: number | null,
    public tipoPago?: string | null,
    public total?: number | null,
    public registros?: IRegistroMaterialUtilizado | null,
    public instalacion?: IInstalacion | null,
    public cliente?: ICliente | null
  ) {}
}

export function getReservaIdentifier(reserva: IReserva): number | undefined {
  return reserva.id;
}
