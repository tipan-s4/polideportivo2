package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.PenalizacionRepository;
import com.mycompany.myapp.service.PenalizacionService;
import com.mycompany.myapp.service.dto.PenalizacionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Penalizacion}.
 */
@RestController
@RequestMapping("/api")
public class PenalizacionResource {

    private final Logger log = LoggerFactory.getLogger(PenalizacionResource.class);

    private static final String ENTITY_NAME = "penalizacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PenalizacionService penalizacionService;

    private final PenalizacionRepository penalizacionRepository;

    public PenalizacionResource(PenalizacionService penalizacionService, PenalizacionRepository penalizacionRepository) {
        this.penalizacionService = penalizacionService;
        this.penalizacionRepository = penalizacionRepository;
    }

    /**
     * {@code POST  /penalizacions} : Create a new penalizacion.
     *
     * @param penalizacionDTO the penalizacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new penalizacionDTO, or with status {@code 400 (Bad Request)} if the penalizacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/penalizacions")
    public ResponseEntity<PenalizacionDTO> createPenalizacion(@RequestBody PenalizacionDTO penalizacionDTO) throws URISyntaxException {
        log.debug("REST request to save Penalizacion : {}", penalizacionDTO);
        if (penalizacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new penalizacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PenalizacionDTO result = penalizacionService.save(penalizacionDTO);
        return ResponseEntity
            .created(new URI("/api/penalizacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /penalizacions/:id} : Updates an existing penalizacion.
     *
     * @param id the id of the penalizacionDTO to save.
     * @param penalizacionDTO the penalizacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated penalizacionDTO,
     * or with status {@code 400 (Bad Request)} if the penalizacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the penalizacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/penalizacions/{id}")
    public ResponseEntity<PenalizacionDTO> updatePenalizacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PenalizacionDTO penalizacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Penalizacion : {}, {}", id, penalizacionDTO);
        if (penalizacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, penalizacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!penalizacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PenalizacionDTO result = penalizacionService.save(penalizacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, penalizacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /penalizacions/:id} : Partial updates given fields of an existing penalizacion, field will ignore if it is null
     *
     * @param id the id of the penalizacionDTO to save.
     * @param penalizacionDTO the penalizacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated penalizacionDTO,
     * or with status {@code 400 (Bad Request)} if the penalizacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the penalizacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the penalizacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/penalizacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PenalizacionDTO> partialUpdatePenalizacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PenalizacionDTO penalizacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Penalizacion partially : {}, {}", id, penalizacionDTO);
        if (penalizacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, penalizacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!penalizacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PenalizacionDTO> result = penalizacionService.partialUpdate(penalizacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, penalizacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /penalizacions} : get all the penalizacions.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of penalizacions in body.
     */
    @GetMapping("/penalizacions")
    public ResponseEntity<List<PenalizacionDTO>> getAllPenalizacions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("cliente-is-null".equals(filter)) {
            log.debug("REST request to get all Penalizacions where cliente is null");
            return new ResponseEntity<>(penalizacionService.findAllWhereClienteIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Penalizacions");
        Page<PenalizacionDTO> page = penalizacionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /penalizacions/:id} : get the "id" penalizacion.
     *
     * @param id the id of the penalizacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the penalizacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/penalizacions/{id}")
    public ResponseEntity<PenalizacionDTO> getPenalizacion(@PathVariable Long id) {
        log.debug("REST request to get Penalizacion : {}", id);
        Optional<PenalizacionDTO> penalizacionDTO = penalizacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(penalizacionDTO);
    }

    /**
     * {@code DELETE  /penalizacions/:id} : delete the "id" penalizacion.
     *
     * @param id the id of the penalizacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/penalizacions/{id}")
    public ResponseEntity<Void> deletePenalizacion(@PathVariable Long id) {
        log.debug("REST request to delete Penalizacion : {}", id);
        penalizacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
