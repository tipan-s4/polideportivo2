import dayjs from 'dayjs/esm';
import { IReserva } from 'app/entities/reserva/reserva.model';
import { IMaterial } from 'app/entities/material/material.model';

export interface IRegistroMaterialUtilizado {
  id?: number;
  nombre?: string | null;
  cantidad?: number | null;
  fecha?: dayjs.Dayjs | null;
  reserva?: IReserva | null;
  material?: IMaterial | null;
}

export class RegistroMaterialUtilizado implements IRegistroMaterialUtilizado {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public cantidad?: number | null,
    public fecha?: dayjs.Dayjs | null,
    public reserva?: IReserva | null,
    public material?: IMaterial | null
  ) {}
}

export function getRegistroMaterialUtilizadoIdentifier(registroMaterialUtilizado: IRegistroMaterialUtilizado): number | undefined {
  return registroMaterialUtilizado.id;
}
