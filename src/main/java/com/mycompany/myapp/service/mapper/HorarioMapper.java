package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Horario;
import com.mycompany.myapp.service.dto.HorarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Horario} and its DTO {@link HorarioDTO}.
 */
@Mapper(componentModel = "spring", uses = { InstalacionMapper.class })
public interface HorarioMapper extends EntityMapper<HorarioDTO, Horario> {
    @Mapping(target = "instalacion", source = "instalacion", qualifiedByName = "id")
    HorarioDTO toDto(Horario s);
}
