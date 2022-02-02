package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MaterialDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Material}.
 */
public interface MaterialService {
    /**
     * Save a material.
     *
     * @param materialDTO the entity to save.
     * @return the persisted entity.
     */
    MaterialDTO save(MaterialDTO materialDTO);

    /**
     * Partially updates a material.
     *
     * @param materialDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaterialDTO> partialUpdate(MaterialDTO materialDTO);

    /**
     * Get all the materials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaterialDTO> findAll(Pageable pageable);

    /**
     * Get the "id" material.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterialDTO> findOne(Long id);

    /**
     * Delete the "id" material.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
