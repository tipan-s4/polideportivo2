package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Instalacion;
import com.mycompany.myapp.repository.InstalacionRepository;
import com.mycompany.myapp.service.InstalacionService;
import com.mycompany.myapp.service.dto.InstalacionDTO;
import com.mycompany.myapp.service.mapper.InstalacionMapper;
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
 * Service Implementation for managing {@link Instalacion}.
 */
@Service
@Transactional
public class InstalacionServiceImpl implements InstalacionService {

    private final Logger log = LoggerFactory.getLogger(InstalacionServiceImpl.class);

    private final InstalacionRepository instalacionRepository;

    private final InstalacionMapper instalacionMapper;

    public InstalacionServiceImpl(InstalacionRepository instalacionRepository, InstalacionMapper instalacionMapper) {
        this.instalacionRepository = instalacionRepository;
        this.instalacionMapper = instalacionMapper;
    }

    @Override
    public InstalacionDTO save(InstalacionDTO instalacionDTO) {
        log.debug("Request to save Instalacion : {}", instalacionDTO);
        Instalacion instalacion = instalacionMapper.toEntity(instalacionDTO);
        instalacion = instalacionRepository.save(instalacion);
        return instalacionMapper.toDto(instalacion);
    }

    @Override
    public Optional<InstalacionDTO> partialUpdate(InstalacionDTO instalacionDTO) {
        log.debug("Request to partially update Instalacion : {}", instalacionDTO);

        return instalacionRepository
            .findById(instalacionDTO.getId())
            .map(existingInstalacion -> {
                instalacionMapper.partialUpdate(existingInstalacion, instalacionDTO);

                return existingInstalacion;
            })
            .map(instalacionRepository::save)
            .map(instalacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstalacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Instalacions");
        return instalacionRepository.findAll(pageable).map(instalacionMapper::toDto);
    }

    /**
     *  Get all the instalacions where Reservas is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InstalacionDTO> findAllWhereReservasIsNull() {
        log.debug("Request to get all instalacions where Reservas is null");
        return StreamSupport
            .stream(instalacionRepository.findAll().spliterator(), false)
            .filter(instalacion -> instalacion.getReservas() == null)
            .map(instalacionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InstalacionDTO> findOne(Long id) {
        log.debug("Request to get Instalacion : {}", id);
        return instalacionRepository.findById(id).map(instalacionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Instalacion : {}", id);
        instalacionRepository.deleteById(id);
    }
}
