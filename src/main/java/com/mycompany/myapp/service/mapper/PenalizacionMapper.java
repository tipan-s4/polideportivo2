package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Penalizacion;
import com.mycompany.myapp.service.dto.PenalizacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Penalizacion} and its DTO {@link PenalizacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PenalizacionMapper extends EntityMapper<PenalizacionDTO, Penalizacion> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PenalizacionDTO toDtoId(Penalizacion penalizacion);
}
