package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Penalizacion;
import com.mycompany.myapp.repository.PenalizacionRepository;
import com.mycompany.myapp.service.dto.PenalizacionDTO;
import com.mycompany.myapp.service.mapper.PenalizacionMapper;
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
 * Integration tests for the {@link PenalizacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PenalizacionResourceIT {

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_A_PAGAR = 1D;
    private static final Double UPDATED_TOTAL_A_PAGAR = 2D;

    private static final String ENTITY_API_URL = "/api/penalizacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PenalizacionRepository penalizacionRepository;

    @Autowired
    private PenalizacionMapper penalizacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPenalizacionMockMvc;

    private Penalizacion penalizacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Penalizacion createEntity(EntityManager em) {
        Penalizacion penalizacion = new Penalizacion().motivo(DEFAULT_MOTIVO).totalAPagar(DEFAULT_TOTAL_A_PAGAR);
        return penalizacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Penalizacion createUpdatedEntity(EntityManager em) {
        Penalizacion penalizacion = new Penalizacion().motivo(UPDATED_MOTIVO).totalAPagar(UPDATED_TOTAL_A_PAGAR);
        return penalizacion;
    }

    @BeforeEach
    public void initTest() {
        penalizacion = createEntity(em);
    }

    @Test
    @Transactional
    void createPenalizacion() throws Exception {
        int databaseSizeBeforeCreate = penalizacionRepository.findAll().size();
        // Create the Penalizacion
        PenalizacionDTO penalizacionDTO = penalizacionMapper.toDto(penalizacion);
        restPenalizacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(penalizacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeCreate + 1);
        Penalizacion testPenalizacion = penalizacionList.get(penalizacionList.size() - 1);
        assertThat(testPenalizacion.getMotivo()).isEqualTo(DEFAULT_MOTIVO);
        assertThat(testPenalizacion.getTotalAPagar()).isEqualTo(DEFAULT_TOTAL_A_PAGAR);
    }

    @Test
    @Transactional
    void createPenalizacionWithExistingId() throws Exception {
        // Create the Penalizacion with an existing ID
        penalizacion.setId(1L);
        PenalizacionDTO penalizacionDTO = penalizacionMapper.toDto(penalizacion);

        int databaseSizeBeforeCreate = penalizacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPenalizacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(penalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPenalizacions() throws Exception {
        // Initialize the database
        penalizacionRepository.saveAndFlush(penalizacion);

        // Get all the penalizacionList
        restPenalizacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(penalizacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)))
            .andExpect(jsonPath("$.[*].totalAPagar").value(hasItem(DEFAULT_TOTAL_A_PAGAR.doubleValue())));
    }

    @Test
    @Transactional
    void getPenalizacion() throws Exception {
        // Initialize the database
        penalizacionRepository.saveAndFlush(penalizacion);

        // Get the penalizacion
        restPenalizacionMockMvc
            .perform(get(ENTITY_API_URL_ID, penalizacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(penalizacion.getId().intValue()))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO))
            .andExpect(jsonPath("$.totalAPagar").value(DEFAULT_TOTAL_A_PAGAR.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPenalizacion() throws Exception {
        // Get the penalizacion
        restPenalizacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPenalizacion() throws Exception {
        // Initialize the database
        penalizacionRepository.saveAndFlush(penalizacion);

        int databaseSizeBeforeUpdate = penalizacionRepository.findAll().size();

        // Update the penalizacion
        Penalizacion updatedPenalizacion = penalizacionRepository.findById(penalizacion.getId()).get();
        // Disconnect from session so that the updates on updatedPenalizacion are not directly saved in db
        em.detach(updatedPenalizacion);
        updatedPenalizacion.motivo(UPDATED_MOTIVO).totalAPagar(UPDATED_TOTAL_A_PAGAR);
        PenalizacionDTO penalizacionDTO = penalizacionMapper.toDto(updatedPenalizacion);

        restPenalizacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, penalizacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(penalizacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeUpdate);
        Penalizacion testPenalizacion = penalizacionList.get(penalizacionList.size() - 1);
        assertThat(testPenalizacion.getMotivo()).isEqualTo(UPDATED_MOTIVO);
        assertThat(testPenalizacion.getTotalAPagar()).isEqualTo(UPDATED_TOTAL_A_PAGAR);
    }

    @Test
    @Transactional
    void putNonExistingPenalizacion() throws Exception {
        int databaseSizeBeforeUpdate = penalizacionRepository.findAll().size();
        penalizacion.setId(count.incrementAndGet());

        // Create the Penalizacion
        PenalizacionDTO penalizacionDTO = penalizacionMapper.toDto(penalizacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPenalizacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, penalizacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(penalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPenalizacion() throws Exception {
        int databaseSizeBeforeUpdate = penalizacionRepository.findAll().size();
        penalizacion.setId(count.incrementAndGet());

        // Create the Penalizacion
        PenalizacionDTO penalizacionDTO = penalizacionMapper.toDto(penalizacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPenalizacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(penalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPenalizacion() throws Exception {
        int databaseSizeBeforeUpdate = penalizacionRepository.findAll().size();
        penalizacion.setId(count.incrementAndGet());

        // Create the Penalizacion
        PenalizacionDTO penalizacionDTO = penalizacionMapper.toDto(penalizacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPenalizacionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(penalizacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePenalizacionWithPatch() throws Exception {
        // Initialize the database
        penalizacionRepository.saveAndFlush(penalizacion);

        int databaseSizeBeforeUpdate = penalizacionRepository.findAll().size();

        // Update the penalizacion using partial update
        Penalizacion partialUpdatedPenalizacion = new Penalizacion();
        partialUpdatedPenalizacion.setId(penalizacion.getId());

        partialUpdatedPenalizacion.motivo(UPDATED_MOTIVO);

        restPenalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPenalizacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPenalizacion))
            )
            .andExpect(status().isOk());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeUpdate);
        Penalizacion testPenalizacion = penalizacionList.get(penalizacionList.size() - 1);
        assertThat(testPenalizacion.getMotivo()).isEqualTo(UPDATED_MOTIVO);
        assertThat(testPenalizacion.getTotalAPagar()).isEqualTo(DEFAULT_TOTAL_A_PAGAR);
    }

    @Test
    @Transactional
    void fullUpdatePenalizacionWithPatch() throws Exception {
        // Initialize the database
        penalizacionRepository.saveAndFlush(penalizacion);

        int databaseSizeBeforeUpdate = penalizacionRepository.findAll().size();

        // Update the penalizacion using partial update
        Penalizacion partialUpdatedPenalizacion = new Penalizacion();
        partialUpdatedPenalizacion.setId(penalizacion.getId());

        partialUpdatedPenalizacion.motivo(UPDATED_MOTIVO).totalAPagar(UPDATED_TOTAL_A_PAGAR);

        restPenalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPenalizacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPenalizacion))
            )
            .andExpect(status().isOk());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeUpdate);
        Penalizacion testPenalizacion = penalizacionList.get(penalizacionList.size() - 1);
        assertThat(testPenalizacion.getMotivo()).isEqualTo(UPDATED_MOTIVO);
        assertThat(testPenalizacion.getTotalAPagar()).isEqualTo(UPDATED_TOTAL_A_PAGAR);
    }

    @Test
    @Transactional
    void patchNonExistingPenalizacion() throws Exception {
        int databaseSizeBeforeUpdate = penalizacionRepository.findAll().size();
        penalizacion.setId(count.incrementAndGet());

        // Create the Penalizacion
        PenalizacionDTO penalizacionDTO = penalizacionMapper.toDto(penalizacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPenalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, penalizacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(penalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPenalizacion() throws Exception {
        int databaseSizeBeforeUpdate = penalizacionRepository.findAll().size();
        penalizacion.setId(count.incrementAndGet());

        // Create the Penalizacion
        PenalizacionDTO penalizacionDTO = penalizacionMapper.toDto(penalizacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPenalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(penalizacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPenalizacion() throws Exception {
        int databaseSizeBeforeUpdate = penalizacionRepository.findAll().size();
        penalizacion.setId(count.incrementAndGet());

        // Create the Penalizacion
        PenalizacionDTO penalizacionDTO = penalizacionMapper.toDto(penalizacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPenalizacionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(penalizacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Penalizacion in the database
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePenalizacion() throws Exception {
        // Initialize the database
        penalizacionRepository.saveAndFlush(penalizacion);

        int databaseSizeBeforeDelete = penalizacionRepository.findAll().size();

        // Delete the penalizacion
        restPenalizacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, penalizacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Penalizacion> penalizacionList = penalizacionRepository.findAll();
        assertThat(penalizacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
