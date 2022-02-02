package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.InstalacionRepository;
import com.mycompany.myapp.service.InstalacionService;
import com.mycompany.myapp.service.dto.InstalacionDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Instalacion}.
 */
@RestController
@RequestMapping("/api")
public class InstalacionResource {

    private final Logger log = LoggerFactory.getLogger(InstalacionResource.class);

    private static final String ENTITY_NAME = "instalacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstalacionService instalacionService;

    private final InstalacionRepository instalacionRepository;

    public InstalacionResource(InstalacionService instalacionService, InstalacionRepository instalacionRepository) {
        this.instalacionService = instalacionService;
        this.instalacionRepository = instalacionRepository;
    }

    /**
     * {@code POST  /instalacions} : Create a new instalacion.
     *
     * @param instalacionDTO the instalacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instalacionDTO, or with status {@code 400 (Bad Request)} if the instalacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instalacions")
    public ResponseEntity<InstalacionDTO> createInstalacion(@RequestBody InstalacionDTO instalacionDTO) throws URISyntaxException {
        log.debug("REST request to save Instalacion : {}", instalacionDTO);
        if (instalacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new instalacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstalacionDTO result = instalacionService.save(instalacionDTO);
        return ResponseEntity
            .created(new URI("/api/instalacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /instalacions/:id} : Updates an existing instalacion.
     *
     * @param id the id of the instalacionDTO to save.
     * @param instalacionDTO the instalacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instalacionDTO,
     * or with status {@code 400 (Bad Request)} if the instalacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instalacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instalacions/{id}")
    public ResponseEntity<InstalacionDTO> updateInstalacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InstalacionDTO instalacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Instalacion : {}, {}", id, instalacionDTO);
        if (instalacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instalacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instalacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InstalacionDTO result = instalacionService.save(instalacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instalacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /instalacions/:id} : Partial updates given fields of an existing instalacion, field will ignore if it is null
     *
     * @param id the id of the instalacionDTO to save.
     * @param instalacionDTO the instalacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instalacionDTO,
     * or with status {@code 400 (Bad Request)} if the instalacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the instalacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the instalacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/instalacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InstalacionDTO> partialUpdateInstalacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InstalacionDTO instalacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Instalacion partially : {}, {}", id, instalacionDTO);
        if (instalacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instalacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instalacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InstalacionDTO> result = instalacionService.partialUpdate(instalacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instalacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /instalacions} : get all the instalacions.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instalacions in body.
     */
    @GetMapping("/instalacions")
    public ResponseEntity<List<InstalacionDTO>> getAllInstalacions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("reservas-is-null".equals(filter)) {
            log.debug("REST request to get all Instalacions where reservas is null");
            return new ResponseEntity<>(instalacionService.findAllWhereReservasIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Instalacions");
        Page<InstalacionDTO> page = instalacionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /instalacions/:id} : get the "id" instalacion.
     *
     * @param id the id of the instalacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instalacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instalacions/{id}")
    public ResponseEntity<InstalacionDTO> getInstalacion(@PathVariable Long id) {
        log.debug("REST request to get Instalacion : {}", id);
        Optional<InstalacionDTO> instalacionDTO = instalacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instalacionDTO);
    }

    /**
     * {@code DELETE  /instalacions/:id} : delete the "id" instalacion.
     *
     * @param id the id of the instalacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instalacions/{id}")
    public ResponseEntity<Void> deleteInstalacion(@PathVariable Long id) {
        log.debug("REST request to delete Instalacion : {}", id);
        instalacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
