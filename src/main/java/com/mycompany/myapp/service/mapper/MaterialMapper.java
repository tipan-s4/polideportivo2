package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.service.dto.MaterialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Material} and its DTO {@link MaterialDTO}.
 */
@Mapper(componentModel = "spring", uses = { InstalacionMapper.class })
public interface MaterialMapper extends EntityMapper<MaterialDTO, Material> {
    @Mapping(target = "instalaciones", source = "instalaciones", qualifiedByName = "id")
    MaterialDTO toDto(Material s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialDTO toDtoId(Material material);
}
