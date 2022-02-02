package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.RegistroMaterialUtilizado;
import com.mycompany.myapp.repository.RegistroMaterialUtilizadoRepository;
import com.mycompany.myapp.service.RegistroMaterialUtilizadoService;
import com.mycompany.myapp.service.dto.RegistroMaterialUtilizadoDTO;
import com.mycompany.myapp.service.mapper.RegistroMaterialUtilizadoMapper;
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
 * Service Implementation for managing {@link RegistroMaterialUtilizado}.
 */
@Service
@Transactional
public class RegistroMaterialUtilizadoServiceImpl implements RegistroMaterialUtilizadoService {

    private final Logger log = LoggerFactory.getLogger(RegistroMaterialUtilizadoServiceImpl.class);

    private final RegistroMaterialUtilizadoRepository registroMaterialUtilizadoRepository;

    private final RegistroMaterialUtilizadoMapper registroMaterialUtilizadoMapper;

    public RegistroMaterialUtilizadoServiceImpl(
        RegistroMaterialUtilizadoRepository registroMaterialUtilizadoRepository,
        RegistroMaterialUtilizadoMapper registroMaterialUtilizadoMapper
    ) {
        this.registroMaterialUtilizadoRepository = registroMaterialUtilizadoRepository;
        this.registroMaterialUtilizadoMapper = registroMaterialUtilizadoMapper;
    }

    @Override
    public RegistroMaterialUtilizadoDTO save(RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO) {
        log.debug("Request to save RegistroMaterialUtilizado : {}", registroMaterialUtilizadoDTO);
        RegistroMaterialUtilizado registroMaterialUtilizado = registroMaterialUtilizadoMapper.toEntity(registroMaterialUtilizadoDTO);
        registroMaterialUtilizado = registroMaterialUtilizadoRepository.save(registroMaterialUtilizado);
        return registroMaterialUtilizadoMapper.toDto(registroMaterialUtilizado);
    }

    @Override
    public Optional<RegistroMaterialUtilizadoDTO> partialUpdate(RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO) {
        log.debug("Request to partially update RegistroMaterialUtilizado : {}", registroMaterialUtilizadoDTO);

        return registroMaterialUtilizadoRepository
            .findById(registroMaterialUtilizadoDTO.getId())
            .map(existingRegistroMaterialUtilizado -> {
                registroMaterialUtilizadoMapper.partialUpdate(existingRegistroMaterialUtilizado, registroMaterialUtilizadoDTO);

                return existingRegistroMaterialUtilizado;
            })
            .map(registroMaterialUtilizadoRepository::save)
            .map(registroMaterialUtilizadoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegistroMaterialUtilizadoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RegistroMaterialUtilizados");
        return registroMaterialUtilizadoRepository.findAll(pageable).map(registroMaterialUtilizadoMapper::toDto);
    }

    /**
     *  Get all the registroMaterialUtilizados where Reserva is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RegistroMaterialUtilizadoDTO> findAllWhereReservaIsNull() {
        log.debug("Request to get all registroMaterialUtilizados where Reserva is null");
        return StreamSupport
            .stream(registroMaterialUtilizadoRepository.findAll().spliterator(), false)
            .filter(registroMaterialUtilizado -> registroMaterialUtilizado.getReserva() == null)
            .map(registroMaterialUtilizadoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegistroMaterialUtilizadoDTO> findOne(Long id) {
        log.debug("Request to get RegistroMaterialUtilizado : {}", id);
        return registroMaterialUtilizadoRepository.findById(id).map(registroMaterialUtilizadoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RegistroMaterialUtilizado : {}", id);
        registroMaterialUtilizadoRepository.deleteById(id);
    }
}
