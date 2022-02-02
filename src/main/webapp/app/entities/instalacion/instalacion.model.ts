import { IHorario } from 'app/entities/horario/horario.model';
import { IMaterial } from 'app/entities/material/material.model';
import { IReserva } from 'app/entities/reserva/reserva.model';

export interface IInstalacion {
  id?: number;
  nombre?: string | null;
  precioPorHora?: number | null;
  disponible?: boolean | null;
  horarios?: IHorario[] | null;
  materiales?: IMaterial[] | null;
  reservas?: IReserva | null;
}

export class Instalacion implements IInstalacion {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public precioPorHora?: number | null,
    public disponible?: boolean | null,
    public horarios?: IHorario[] | null,
    public materiales?: IMaterial[] | null,
    public reservas?: IReserva | null
  ) {
    this.disponible = this.disponible ?? false;
  }
}

export function getInstalacionIdentifier(instalacion: IInstalacion): number | undefined {
  return instalacion.id;
}
