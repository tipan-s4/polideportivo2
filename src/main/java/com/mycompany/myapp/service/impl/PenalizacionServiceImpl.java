package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Penalizacion;
import com.mycompany.myapp.repository.PenalizacionRepository;
import com.mycompany.myapp.service.PenalizacionService;
import com.mycompany.myapp.service.dto.PenalizacionDTO;
import com.mycompany.myapp.service.mapper.PenalizacionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Penalizacion}.
 */
@Service
@Transactional
public class PenalizacionServiceImpl implements PenalizacionService {

    private final Logger log = LoggerFactory.getLogger(PenalizacionServiceImpl.class);

    private final PenalizacionRepository penalizacionRepository;

    private final PenalizacionMapper penalizacionMapper;

    public PenalizacionServiceImpl(PenalizacionRepository penalizacionRepository, PenalizacionMapper penalizacionMapper) {
        this.penalizacionRepository = penalizacionRepository;
        this.penalizacionMapper = penalizacionMapper;
    }

    @Override
    public PenalizacionDTO save(PenalizacionDTO penalizacionDTO) {
        log.debug("Request to save Penalizacion : {}", penalizacionDTO);
        Penalizacion penalizacion = penalizacionMapper.toEntity(penalizacionDTO);
        penalizacion = penalizacionRepository.save(penalizacion);
        return penalizacionMapper.toDto(penalizacion);
    }

    @Override
    public Optional<PenalizacionDTO> partialUpdate(PenalizacionDTO penalizacionDTO) {
        log.debug("Request to partially update Penalizacion : {}", penalizacionDTO);

        return penalizacionRepository
            .findById(penalizacionDTO.getId())
            .map(existingPenalizacion -> {
                penalizacionMapper.partialUpdate(existingPenalizacion, penalizacionDTO);

                return existingPenalizacion;
            })
            .map(penalizacionRepository::save)
            .map(penalizacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PenalizacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Penalizacions");
        return penalizacionRepository.findAll(pageable).map(penalizacionMapper::toDto);
    }

    /**
     *  Get all the penalizacions where Cliente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PenalizacionDTO> findAllWhereClienteIsNull() {
        log.debug("Request to get all penalizacions where Cliente is null");
        return StreamSupport
            .stream(penalizacionRepository.findAll().spliterator(), false)
            .filter(penalizacion -> penalizacion.getCliente() == null)
            .map(penalizacionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PenalizacionDTO> findOne(Long id) {
        log.debug("Request to get Penalizacion : {}", id);
        return penalizacionRepository.findById(id).map(penalizacionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Penalizacion : {}", id);
        penalizacionRepository.deleteById(id);
    }
}
