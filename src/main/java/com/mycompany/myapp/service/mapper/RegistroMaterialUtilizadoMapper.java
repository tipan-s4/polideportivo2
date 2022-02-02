package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.RegistroMaterialUtilizado;
import com.mycompany.myapp.service.dto.RegistroMaterialUtilizadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RegistroMaterialUtilizado} and its DTO {@link RegistroMaterialUtilizadoDTO}.
 */
@Mapper(componentModel = "spring", uses = { MaterialMapper.class })
public interface RegistroMaterialUtilizadoMapper extends EntityMapper<RegistroMaterialUtilizadoDTO, RegistroMaterialUtilizado> {
    @Mapping(target = "material", source = "material", qualifiedByName = "id")
    RegistroMaterialUtilizadoDTO toDto(RegistroMaterialUtilizado s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegistroMaterialUtilizadoDTO toDtoId(RegistroMaterialUtilizado registroMaterialUtilizado);
}
