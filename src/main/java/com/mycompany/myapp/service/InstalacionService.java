package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.InstalacionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Instalacion}.
 */
public interface InstalacionService {
    /**
     * Save a instalacion.
     *
     * @param instalacionDTO the entity to save.
     * @return the persisted entity.
     */
    InstalacionDTO save(InstalacionDTO instalacionDTO);

    /**
     * Partially updates a instalacion.
     *
     * @param instalacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InstalacionDTO> partialUpdate(InstalacionDTO instalacionDTO);

    /**
     * Get all the instalacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InstalacionDTO> findAll(Pageable pageable);
    /**
     * Get all the InstalacionDTO where Reservas is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InstalacionDTO> findAllWhereReservasIsNull();

    /**
     * Get the "id" instalacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InstalacionDTO> findOne(Long id);

    /**
     * Delete the "id" instalacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
