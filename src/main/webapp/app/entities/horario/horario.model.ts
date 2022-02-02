import dayjs from 'dayjs/esm';
import { IInstalacion } from 'app/entities/instalacion/instalacion.model';

export interface IHorario {
  id?: number;
  dia?: dayjs.Dayjs | null;
  hora?: number | null;
  instalacion?: IInstalacion | null;
}

export class Horario implements IHorario {
  constructor(public id?: number, public dia?: dayjs.Dayjs | null, public hora?: number | null, public instalacion?: IInstalacion | null) {}
}

export function getHorarioIdentifier(horario: IHorario): number | undefined {
  return horario.id;
}
