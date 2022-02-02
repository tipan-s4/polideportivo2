package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Reserva;
import com.mycompany.myapp.service.dto.ReservaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reserva} and its DTO {@link ReservaDTO}.
 */
@Mapper(componentModel = "spring", uses = { RegistroMaterialUtilizadoMapper.class, InstalacionMapper.class, ClienteMapper.class })
public interface ReservaMapper extends EntityMapper<ReservaDTO, Reserva> {
    @Mapping(target = "registros", source = "registros", qualifiedByName = "id")
    @Mapping(target = "instalacion", source = "instalacion", qualifiedByName = "id")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "id")
    ReservaDTO toDto(Reserva s);
}
