package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ReservaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Reserva}.
 */
public interface ReservaService {
    /**
     * Save a reserva.
     *
     * @param reservaDTO the entity to save.
     * @return the persisted entity.
     */
    ReservaDTO save(ReservaDTO reservaDTO);

    /**
     * Partially updates a reserva.
     *
     * @param reservaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReservaDTO> partialUpdate(ReservaDTO reservaDTO);

    /**
     * Get all the reservas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReservaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reserva.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReservaDTO> findOne(Long id);

    /**
     * Delete the "id" reserva.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
