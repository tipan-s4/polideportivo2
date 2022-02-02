package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RegistroMaterialUtilizado;
import com.mycompany.myapp.repository.RegistroMaterialUtilizadoRepository;
import com.mycompany.myapp.service.dto.RegistroMaterialUtilizadoDTO;
import com.mycompany.myapp.service.mapper.RegistroMaterialUtilizadoMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RegistroMaterialUtilizadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegistroMaterialUtilizadoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/registro-material-utilizados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegistroMaterialUtilizadoRepository registroMaterialUtilizadoRepository;

    @Autowired
    private RegistroMaterialUtilizadoMapper registroMaterialUtilizadoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegistroMaterialUtilizadoMockMvc;

    private RegistroMaterialUtilizado registroMaterialUtilizado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroMaterialUtilizado createEntity(EntityManager em) {
        RegistroMaterialUtilizado registroMaterialUtilizado = new RegistroMaterialUtilizado()
            .nombre(DEFAULT_NOMBRE)
            .cantidad(DEFAULT_CANTIDAD)
            .fecha(DEFAULT_FECHA);
        return registroMaterialUtilizado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroMaterialUtilizado createUpdatedEntity(EntityManager em) {
        RegistroMaterialUtilizado registroMaterialUtilizado = new RegistroMaterialUtilizado()
            .nombre(UPDATED_NOMBRE)
            .cantidad(UPDATED_CANTIDAD)
            .fecha(UPDATED_FECHA);
        return registroMaterialUtilizado;
    }

    @BeforeEach
    public void initTest() {
        registroMaterialUtilizado = createEntity(em);
    }

    @Test
    @Transactional
    void createRegistroMaterialUtilizado() throws Exception {
        int databaseSizeBeforeCreate = registroMaterialUtilizadoRepository.findAll().size();
        // Create the RegistroMaterialUtilizado
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = registroMaterialUtilizadoMapper.toDto(registroMaterialUtilizado);
        restRegistroMaterialUtilizadoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroMaterialUtilizadoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeCreate + 1);
        RegistroMaterialUtilizado testRegistroMaterialUtilizado = registroMaterialUtilizadoList.get(
            registroMaterialUtilizadoList.size() - 1
        );
        assertThat(testRegistroMaterialUtilizado.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testRegistroMaterialUtilizado.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testRegistroMaterialUtilizado.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createRegistroMaterialUtilizadoWithExistingId() throws Exception {
        // Create the RegistroMaterialUtilizado with an existing ID
        registroMaterialUtilizado.setId(1L);
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = registroMaterialUtilizadoMapper.toDto(registroMaterialUtilizado);

        int databaseSizeBeforeCreate = registroMaterialUtilizadoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistroMaterialUtilizadoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroMaterialUtilizadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRegistroMaterialUtilizados() throws Exception {
        // Initialize the database
        registroMaterialUtilizadoRepository.saveAndFlush(registroMaterialUtilizado);

        // Get all the registroMaterialUtilizadoList
        restRegistroMaterialUtilizadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registroMaterialUtilizado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getRegistroMaterialUtilizado() throws Exception {
        // Initialize the database
        registroMaterialUtilizadoRepository.saveAndFlush(registroMaterialUtilizado);

        // Get the registroMaterialUtilizado
        restRegistroMaterialUtilizadoMockMvc
            .perform(get(ENTITY_API_URL_ID, registroMaterialUtilizado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registroMaterialUtilizado.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRegistroMaterialUtilizado() throws Exception {
        // Get the registroMaterialUtilizado
        restRegistroMaterialUtilizadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRegistroMaterialUtilizado() throws Exception {
        // Initialize the database
        registroMaterialUtilizadoRepository.saveAndFlush(registroMaterialUtilizado);

        int databaseSizeBeforeUpdate = registroMaterialUtilizadoRepository.findAll().size();

        // Update the registroMaterialUtilizado
        RegistroMaterialUtilizado updatedRegistroMaterialUtilizado = registroMaterialUtilizadoRepository
            .findById(registroMaterialUtilizado.getId())
            .get();
        // Disconnect from session so that the updates on updatedRegistroMaterialUtilizado are not directly saved in db
        em.detach(updatedRegistroMaterialUtilizado);
        updatedRegistroMaterialUtilizado.nombre(UPDATED_NOMBRE).cantidad(UPDATED_CANTIDAD).fecha(UPDATED_FECHA);
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = registroMaterialUtilizadoMapper.toDto(updatedRegistroMaterialUtilizado);

        restRegistroMaterialUtilizadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registroMaterialUtilizadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroMaterialUtilizadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeUpdate);
        RegistroMaterialUtilizado testRegistroMaterialUtilizado = registroMaterialUtilizadoList.get(
            registroMaterialUtilizadoList.size() - 1
        );
        assertThat(testRegistroMaterialUtilizado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testRegistroMaterialUtilizado.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testRegistroMaterialUtilizado.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingRegistroMaterialUtilizado() throws Exception {
        int databaseSizeBeforeUpdate = registroMaterialUtilizadoRepository.findAll().size();
        registroMaterialUtilizado.setId(count.incrementAndGet());

        // Create the RegistroMaterialUtilizado
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = registroMaterialUtilizadoMapper.toDto(registroMaterialUtilizado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroMaterialUtilizadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registroMaterialUtilizadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroMaterialUtilizadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegistroMaterialUtilizado() throws Exception {
        int databaseSizeBeforeUpdate = registroMaterialUtilizadoRepository.findAll().size();
        registroMaterialUtilizado.setId(count.incrementAndGet());

        // Create the RegistroMaterialUtilizado
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = registroMaterialUtilizadoMapper.toDto(registroMaterialUtilizado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroMaterialUtilizadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroMaterialUtilizadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegistroMaterialUtilizado() throws Exception {
        int databaseSizeBeforeUpdate = registroMaterialUtilizadoRepository.findAll().size();
        registroMaterialUtilizado.setId(count.incrementAndGet());

        // Create the RegistroMaterialUtilizado
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = registroMaterialUtilizadoMapper.toDto(registroMaterialUtilizado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroMaterialUtilizadoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroMaterialUtilizadoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegistroMaterialUtilizadoWithPatch() throws Exception {
        // Initialize the database
        registroMaterialUtilizadoRepository.saveAndFlush(registroMaterialUtilizado);

        int databaseSizeBeforeUpdate = registroMaterialUtilizadoRepository.findAll().size();

        // Update the registroMaterialUtilizado using partial update
        RegistroMaterialUtilizado partialUpdatedRegistroMaterialUtilizado = new RegistroMaterialUtilizado();
        partialUpdatedRegistroMaterialUtilizado.setId(registroMaterialUtilizado.getId());

        partialUpdatedRegistroMaterialUtilizado.nombre(UPDATED_NOMBRE).cantidad(UPDATED_CANTIDAD);

        restRegistroMaterialUtilizadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistroMaterialUtilizado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistroMaterialUtilizado))
            )
            .andExpect(status().isOk());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeUpdate);
        RegistroMaterialUtilizado testRegistroMaterialUtilizado = registroMaterialUtilizadoList.get(
            registroMaterialUtilizadoList.size() - 1
        );
        assertThat(testRegistroMaterialUtilizado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testRegistroMaterialUtilizado.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testRegistroMaterialUtilizado.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateRegistroMaterialUtilizadoWithPatch() throws Exception {
        // Initialize the database
        registroMaterialUtilizadoRepository.saveAndFlush(registroMaterialUtilizado);

        int databaseSizeBeforeUpdate = registroMaterialUtilizadoRepository.findAll().size();

        // Update the registroMaterialUtilizado using partial update
        RegistroMaterialUtilizado partialUpdatedRegistroMaterialUtilizado = new RegistroMaterialUtilizado();
        partialUpdatedRegistroMaterialUtilizado.setId(registroMaterialUtilizado.getId());

        partialUpdatedRegistroMaterialUtilizado.nombre(UPDATED_NOMBRE).cantidad(UPDATED_CANTIDAD).fecha(UPDATED_FECHA);

        restRegistroMaterialUtilizadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistroMaterialUtilizado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistroMaterialUtilizado))
            )
            .andExpect(status().isOk());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeUpdate);
        RegistroMaterialUtilizado testRegistroMaterialUtilizado = registroMaterialUtilizadoList.get(
            registroMaterialUtilizadoList.size() - 1
        );
        assertThat(testRegistroMaterialUtilizado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testRegistroMaterialUtilizado.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testRegistroMaterialUtilizado.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingRegistroMaterialUtilizado() throws Exception {
        int databaseSizeBeforeUpdate = registroMaterialUtilizadoRepository.findAll().size();
        registroMaterialUtilizado.setId(count.incrementAndGet());

        // Create the RegistroMaterialUtilizado
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = registroMaterialUtilizadoMapper.toDto(registroMaterialUtilizado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroMaterialUtilizadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, registroMaterialUtilizadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroMaterialUtilizadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegistroMaterialUtilizado() throws Exception {
        int databaseSizeBeforeUpdate = registroMaterialUtilizadoRepository.findAll().size();
        registroMaterialUtilizado.setId(count.incrementAndGet());

        // Create the RegistroMaterialUtilizado
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = registroMaterialUtilizadoMapper.toDto(registroMaterialUtilizado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroMaterialUtilizadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroMaterialUtilizadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegistroMaterialUtilizado() throws Exception {
        int databaseSizeBeforeUpdate = registroMaterialUtilizadoRepository.findAll().size();
        registroMaterialUtilizado.setId(count.incrementAndGet());

        // Create the RegistroMaterialUtilizado
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = registroMaterialUtilizadoMapper.toDto(registroMaterialUtilizado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroMaterialUtilizadoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroMaterialUtilizadoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegistroMaterialUtilizado in the database
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegistroMaterialUtilizado() throws Exception {
        // Initialize the database
        registroMaterialUtilizadoRepository.saveAndFlush(registroMaterialUtilizado);

        int databaseSizeBeforeDelete = registroMaterialUtilizadoRepository.findAll().size();

        // Delete the registroMaterialUtilizado
        restRegistroMaterialUtilizadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, registroMaterialUtilizado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RegistroMaterialUtilizado> registroMaterialUtilizadoList = registroMaterialUtilizadoRepository.findAll();
        assertThat(registroMaterialUtilizadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
