package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Horario;
import com.mycompany.myapp.repository.HorarioRepository;
import com.mycompany.myapp.service.HorarioService;
import com.mycompany.myapp.service.dto.HorarioDTO;
import com.mycompany.myapp.service.mapper.HorarioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Horario}.
 */
@Service
@Transactional
public class HorarioServiceImpl implements HorarioService {

    private final Logger log = LoggerFactory.getLogger(HorarioServiceImpl.class);

    private final HorarioRepository horarioRepository;

    private final HorarioMapper horarioMapper;

    public HorarioServiceImpl(HorarioRepository horarioRepository, HorarioMapper horarioMapper) {
        this.horarioRepository = horarioRepository;
        this.horarioMapper = horarioMapper;
    }

    @Override
    public HorarioDTO save(HorarioDTO horarioDTO) {
        log.debug("Request to save Horario : {}", horarioDTO);
        Horario horario = horarioMapper.toEntity(horarioDTO);
        horario = horarioRepository.save(horario);
        return horarioMapper.toDto(horario);
    }

    @Override
    public Optional<HorarioDTO> partialUpdate(HorarioDTO horarioDTO) {
        log.debug("Request to partially update Horario : {}", horarioDTO);

        return horarioRepository
            .findById(horarioDTO.getId())
            .map(existingHorario -> {
                horarioMapper.partialUpdate(existingHorario, horarioDTO);

                return existingHorario;
            })
            .map(horarioRepository::save)
            .map(horarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HorarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Horarios");
        return horarioRepository.findAll(pageable).map(horarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HorarioDTO> findOne(Long id) {
        log.debug("Request to get Horario : {}", id);
        return horarioRepository.findById(id).map(horarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Horario : {}", id);
        horarioRepository.deleteById(id);
    }
}
