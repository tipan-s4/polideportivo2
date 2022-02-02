package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Reserva;
import com.mycompany.myapp.repository.ReservaRepository;
import com.mycompany.myapp.service.ReservaService;
import com.mycompany.myapp.service.dto.ReservaDTO;
import com.mycompany.myapp.service.mapper.ReservaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reserva}.
 */
@Service
@Transactional
public class ReservaServiceImpl implements ReservaService {

    private final Logger log = LoggerFactory.getLogger(ReservaServiceImpl.class);

    private final ReservaRepository reservaRepository;

    private final ReservaMapper reservaMapper;

    public ReservaServiceImpl(ReservaRepository reservaRepository, ReservaMapper reservaMapper) {
        this.reservaRepository = reservaRepository;
        this.reservaMapper = reservaMapper;
    }

    @Override
    public ReservaDTO save(ReservaDTO reservaDTO) {
        log.debug("Request to save Reserva : {}", reservaDTO);
        Reserva reserva = reservaMapper.toEntity(reservaDTO);
        reserva = reservaRepository.save(reserva);
        return reservaMapper.toDto(reserva);
    }

    @Override
    public Optional<ReservaDTO> partialUpdate(ReservaDTO reservaDTO) {
        log.debug("Request to partially update Reserva : {}", reservaDTO);

        return reservaRepository
            .findById(reservaDTO.getId())
            .map(existingReserva -> {
                reservaMapper.partialUpdate(existingReserva, reservaDTO);

                return existingReserva;
            })
            .map(reservaRepository::save)
            .map(reservaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reservas");
        return reservaRepository.findAll(pageable).map(reservaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReservaDTO> findOne(Long id) {
        log.debug("Request to get Reserva : {}", id);
        return reservaRepository.findById(id).map(reservaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reserva : {}", id);
        reservaRepository.deleteById(id);
    }
}
