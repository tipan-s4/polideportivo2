package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.RegistroMaterialUtilizadoRepository;
import com.mycompany.myapp.service.RegistroMaterialUtilizadoService;
import com.mycompany.myapp.service.dto.RegistroMaterialUtilizadoDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RegistroMaterialUtilizado}.
 */
@RestController
@RequestMapping("/api")
public class RegistroMaterialUtilizadoResource {

    private final Logger log = LoggerFactory.getLogger(RegistroMaterialUtilizadoResource.class);

    private static final String ENTITY_NAME = "registroMaterialUtilizado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistroMaterialUtilizadoService registroMaterialUtilizadoService;

    private final RegistroMaterialUtilizadoRepository registroMaterialUtilizadoRepository;

    public RegistroMaterialUtilizadoResource(
        RegistroMaterialUtilizadoService registroMaterialUtilizadoService,
        RegistroMaterialUtilizadoRepository registroMaterialUtilizadoRepository
    ) {
        this.registroMaterialUtilizadoService = registroMaterialUtilizadoService;
        this.registroMaterialUtilizadoRepository = registroMaterialUtilizadoRepository;
    }

    /**
     * {@code POST  /registro-material-utilizados} : Create a new registroMaterialUtilizado.
     *
     * @param registroMaterialUtilizadoDTO the registroMaterialUtilizadoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registroMaterialUtilizadoDTO, or with status {@code 400 (Bad Request)} if the registroMaterialUtilizado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registro-material-utilizados")
    public ResponseEntity<RegistroMaterialUtilizadoDTO> createRegistroMaterialUtilizado(
        @RequestBody RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RegistroMaterialUtilizado : {}", registroMaterialUtilizadoDTO);
        if (registroMaterialUtilizadoDTO.getId() != null) {
            throw new BadRequestAlertException("A new registroMaterialUtilizado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegistroMaterialUtilizadoDTO result = registroMaterialUtilizadoService.save(registroMaterialUtilizadoDTO);
        return ResponseEntity
            .created(new URI("/api/registro-material-utilizados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registro-material-utilizados/:id} : Updates an existing registroMaterialUtilizado.
     *
     * @param id the id of the registroMaterialUtilizadoDTO to save.
     * @param registroMaterialUtilizadoDTO the registroMaterialUtilizadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroMaterialUtilizadoDTO,
     * or with status {@code 400 (Bad Request)} if the registroMaterialUtilizadoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registroMaterialUtilizadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registro-material-utilizados/{id}")
    public ResponseEntity<RegistroMaterialUtilizadoDTO> updateRegistroMaterialUtilizado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RegistroMaterialUtilizado : {}, {}", id, registroMaterialUtilizadoDTO);
        if (registroMaterialUtilizadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroMaterialUtilizadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroMaterialUtilizadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RegistroMaterialUtilizadoDTO result = registroMaterialUtilizadoService.save(registroMaterialUtilizadoDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registroMaterialUtilizadoDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /registro-material-utilizados/:id} : Partial updates given fields of an existing registroMaterialUtilizado, field will ignore if it is null
     *
     * @param id the id of the registroMaterialUtilizadoDTO to save.
     * @param registroMaterialUtilizadoDTO the registroMaterialUtilizadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroMaterialUtilizadoDTO,
     * or with status {@code 400 (Bad Request)} if the registroMaterialUtilizadoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the registroMaterialUtilizadoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the registroMaterialUtilizadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/registro-material-utilizados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RegistroMaterialUtilizadoDTO> partialUpdateRegistroMaterialUtilizado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RegistroMaterialUtilizado partially : {}, {}", id, registroMaterialUtilizadoDTO);
        if (registroMaterialUtilizadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroMaterialUtilizadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroMaterialUtilizadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RegistroMaterialUtilizadoDTO> result = registroMaterialUtilizadoService.partialUpdate(registroMaterialUtilizadoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registroMaterialUtilizadoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /registro-material-utilizados} : get all the registroMaterialUtilizados.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registroMaterialUtilizados in body.
     */
    @GetMapping("/registro-material-utilizados")
    public ResponseEntity<List<RegistroMaterialUtilizadoDTO>> getAllRegistroMaterialUtilizados(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("reserva-is-null".equals(filter)) {
            log.debug("REST request to get all RegistroMaterialUtilizados where reserva is null");
            return new ResponseEntity<>(registroMaterialUtilizadoService.findAllWhereReservaIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of RegistroMaterialUtilizados");
        Page<RegistroMaterialUtilizadoDTO> page = registroMaterialUtilizadoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /registro-material-utilizados/:id} : get the "id" registroMaterialUtilizado.
     *
     * @param id the id of the registroMaterialUtilizadoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registroMaterialUtilizadoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registro-material-utilizados/{id}")
    public ResponseEntity<RegistroMaterialUtilizadoDTO> getRegistroMaterialUtilizado(@PathVariable Long id) {
        log.debug("REST request to get RegistroMaterialUtilizado : {}", id);
        Optional<RegistroMaterialUtilizadoDTO> registroMaterialUtilizadoDTO = registroMaterialUtilizadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registroMaterialUtilizadoDTO);
    }

    /**
     * {@code DELETE  /registro-material-utilizados/:id} : delete the "id" registroMaterialUtilizado.
     *
     * @param id the id of the registroMaterialUtilizadoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registro-material-utilizados/{id}")
    public ResponseEntity<Void> deleteRegistroMaterialUtilizado(@PathVariable Long id) {
        log.debug("REST request to delete RegistroMaterialUtilizado : {}", id);
        registroMaterialUtilizadoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
