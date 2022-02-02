package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.HorarioRepository;
import com.mycompany.myapp.service.HorarioService;
import com.mycompany.myapp.service.dto.HorarioDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Horario}.
 */
@RestController
@RequestMapping("/api")
public class HorarioResource {

    private final Logger log = LoggerFactory.getLogger(HorarioResource.class);

    private static final String ENTITY_NAME = "horario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HorarioService horarioService;

    private final HorarioRepository horarioRepository;

    public HorarioResource(HorarioService horarioService, HorarioRepository horarioRepository) {
        this.horarioService = horarioService;
        this.horarioRepository = horarioRepository;
    }

    /**
     * {@code POST  /horarios} : Create a new horario.
     *
     * @param horarioDTO the horarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new horarioDTO, or with status {@code 400 (Bad Request)} if the horario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/horarios")
    public ResponseEntity<HorarioDTO> createHorario(@RequestBody HorarioDTO horarioDTO) throws URISyntaxException {
        log.debug("REST request to save Horario : {}", horarioDTO);
        if (horarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new horario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HorarioDTO result = horarioService.save(horarioDTO);
        return ResponseEntity
            .created(new URI("/api/horarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /horarios/:id} : Updates an existing horario.
     *
     * @param id the id of the horarioDTO to save.
     * @param horarioDTO the horarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horarioDTO,
     * or with status {@code 400 (Bad Request)} if the horarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the horarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/horarios/{id}")
    public ResponseEntity<HorarioDTO> updateHorario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HorarioDTO horarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Horario : {}, {}", id, horarioDTO);
        if (horarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, horarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!horarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HorarioDTO result = horarioService.save(horarioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, horarioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /horarios/:id} : Partial updates given fields of an existing horario, field will ignore if it is null
     *
     * @param id the id of the horarioDTO to save.
     * @param horarioDTO the horarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horarioDTO,
     * or with status {@code 400 (Bad Request)} if the horarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the horarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the horarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/horarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HorarioDTO> partialUpdateHorario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HorarioDTO horarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Horario partially : {}, {}", id, horarioDTO);
        if (horarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, horarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!horarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HorarioDTO> result = horarioService.partialUpdate(horarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, horarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /horarios} : get all the horarios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of horarios in body.
     */
    @GetMapping("/horarios")
    public ResponseEntity<List<HorarioDTO>> getAllHorarios(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Horarios");
        Page<HorarioDTO> page = horarioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /horarios/:id} : get the "id" horario.
     *
     * @param id the id of the horarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the horarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/horarios/{id}")
    public ResponseEntity<HorarioDTO> getHorario(@PathVariable Long id) {
        log.debug("REST request to get Horario : {}", id);
        Optional<HorarioDTO> horarioDTO = horarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(horarioDTO);
    }

    /**
     * {@code DELETE  /horarios/:id} : delete the "id" horario.
     *
     * @param id the id of the horarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/horarios/{id}")
    public ResponseEntity<Void> deleteHorario(@PathVariable Long id) {
        log.debug("REST request to delete Horario : {}", id);
        horarioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
