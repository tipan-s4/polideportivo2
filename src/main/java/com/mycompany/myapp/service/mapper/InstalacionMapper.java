package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Instalacion;
import com.mycompany.myapp.service.dto.InstalacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Instalacion} and its DTO {@link InstalacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InstalacionMapper extends EntityMapper<InstalacionDTO, Instalacion> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InstalacionDTO toDtoId(Instalacion instalacion);
}
