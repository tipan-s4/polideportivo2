package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.PenalizacionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Penalizacion}.
 */
public interface PenalizacionService {
    /**
     * Save a penalizacion.
     *
     * @param penalizacionDTO the entity to save.
     * @return the persisted entity.
     */
    PenalizacionDTO save(PenalizacionDTO penalizacionDTO);

    /**
     * Partially updates a penalizacion.
     *
     * @param penalizacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PenalizacionDTO> partialUpdate(PenalizacionDTO penalizacionDTO);

    /**
     * Get all the penalizacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PenalizacionDTO> findAll(Pageable pageable);
    /**
     * Get all the PenalizacionDTO where Cliente is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PenalizacionDTO> findAllWhereClienteIsNull();

    /**
     * Get the "id" penalizacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PenalizacionDTO> findOne(Long id);

    /**
     * Delete the "id" penalizacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
