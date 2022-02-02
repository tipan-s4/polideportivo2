package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.HorarioDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Horario}.
 */
public interface HorarioService {
    /**
     * Save a horario.
     *
     * @param horarioDTO the entity to save.
     * @return the persisted entity.
     */
    HorarioDTO save(HorarioDTO horarioDTO);

    /**
     * Partially updates a horario.
     *
     * @param horarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HorarioDTO> partialUpdate(HorarioDTO horarioDTO);

    /**
     * Get all the horarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HorarioDTO> findAll(Pageable pageable);

    /**
     * Get the "id" horario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HorarioDTO> findOne(Long id);

    /**
     * Delete the "id" horario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
