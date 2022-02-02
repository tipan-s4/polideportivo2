package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Instalacion;
import com.mycompany.myapp.repository.InstalacionRepository;
import com.mycompany.myapp.service.dto.InstalacionDTO;
import com.mycompany.myapp.service.mapper.InstalacionMapper;
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
 * Integration tests for the {@link InstalacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InstalacionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRECIO_POR_HORA = 1D;
    private static final Double UPDATED_PRECIO_POR_HORA = 2D;

    private static final Boolean DEFAULT_DISPONIBLE = false;
    private static final Boolean UPDATED_DISPONIBLE = true;

    private static final String ENTITY_API_URL = "/api/instalacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstalacionRepository instalacionRepository;

    @Autowired
    private InstalacionMapper instalacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstalacionMockMvc;

    private Instalacion instalacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instalacion createEntity(EntityManager em) {
        Instalacion instalacion = new Instalacion()
            .nombre(DEFAULT_NOMBRE)
            .precioPorHora(DEFAULT_PRECIO_POR_HORA)
            .disponible(DEFAULT_DISPONIBLE);
        return instalacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instalacion createUpdatedEntity(EntityManager em) {
        Instalacion instalacion = new Instalacion()
            .nombre(UPDATED_NOMBRE)
            .precioPorHora(UPDATED_PRECIO_POR_HORA)
            .disponible(UPDATED_DISPONIBLE);
        return instalacion;
    }

    @BeforeEach
    public void initTest() {
        instalacion = createEntity(em);
    }

    @Test
    @Transactional
    void createInstalacion() throws Exception {
        int databaseSizeBeforeCreate = instalacionRepository.findAll().size();
        // Create the Instalacion
        InstalacionDTO instalacionDTO = instalacionMapper.toDto(instalacion);
        restInstalacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instalacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeCreate + 1);
        Instalacion testInstalacion = instalacionList.get(instalacionList.size() - 1);
        assertThat(testInstalacion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testInstalacion.getPrecioPorHora()).isEqualTo(DEFAULT_PRECIO_POR_HORA);
        assertThat(testInstalacion.getDisponible()).isEqualTo(DEFAULT_DISPONIBLE);
    }

    @Test
    @Transactional
    void createInstalacionWithExistingId() throws Exception {
        // Create the Instalacion with an existing ID
        instalacion.setId(1L);
        InstalacionDTO instalacionDTO = instalacionMapper.toDto(instalacion);

        int databaseSizeBeforeCreate = instalacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstalacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instalacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInstalacions() throws Exception {
        // Initialize the database
        instalacionRepository.saveAndFlush(instalacion);

        // Get all the instalacionList
        restInstalacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instalacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].precioPorHora").value(hasItem(DEFAULT_PRECIO_POR_HORA.doubleValue())))
            .andExpect(jsonPath("$.[*].disponible").value(hasItem(DEFAULT_DISPONIBLE.booleanValue())));
    }

    @Test
    @Transactional
    void getInstalacion() throws Exception {
        // Initialize the database
        instalacionRepository.saveAndFlush(instalacion);

        // Get the instalacion
        restInstalacionMockMvc
            .perform(get(ENTITY_API_URL_ID, instalacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(instalacion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.precioPorHora").value(DEFAULT_PRECIO_POR_HORA.doubleValue()))
            .andExpect(jsonPath("$.disponible").value(DEFAULT_DISPONIBLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingInstalacion() throws Exception {
        // Get the instalacion
        restInstalacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInstalacion() throws Exception {
        // Initialize the database
        instalacionRepository.saveAndFlush(instalacion);

        int databaseSizeBeforeUpdate = instalacionRepository.findAll().size();

        // Update the instalacion
        Instalacion updatedInstalacion = instalacionRepository.findById(instalacion.getId()).get();
        // Disconnect from session so that the updates on updatedInstalacion are not directly saved in db
        em.detach(updatedInstalacion);
        updatedInstalacion.nombre(UPDATED_NOMBRE).precioPorHora(UPDATED_PRECIO_POR_HORA).disponible(UPDATED_DISPONIBLE);
        InstalacionDTO instalacionDTO = instalacionMapper.toDto(updatedInstalacion);

        restInstalacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, instalacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(instalacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeUpdate);
        Instalacion testInstalacion = instalacionList.get(instalacionList.size() - 1);
        assertThat(testInstalacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testInstalacion.getPrecioPorHora()).isEqualTo(UPDATED_PRECIO_POR_HORA);
        assertThat(testInstalacion.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void putNonExistingInstalacion() throws Exception {
        int databaseSizeBeforeUpdate = instalacionRepository.findAll().size();
        instalacion.setId(count.incrementAndGet());

        // Create the Instalacion
        InstalacionDTO instalacionDTO = instalacionMapper.toDto(instalacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstalacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, instalacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(instalacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstalacion() throws Exception {
        int databaseSizeBeforeUpdate = instalacionRepository.findAll().size();
        instalacion.setId(count.incrementAndGet());

        // Create the Instalacion
        InstalacionDTO instalacionDTO = instalacionMapper.toDto(instalacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstalacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(instalacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstalacion() throws Exception {
        int databaseSizeBeforeUpdate = instalacionRepository.findAll().size();
        instalacion.setId(count.incrementAndGet());

        // Create the Instalacion
        InstalacionDTO instalacionDTO = instalacionMapper.toDto(instalacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstalacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instalacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInstalacionWithPatch() throws Exception {
        // Initialize the database
        instalacionRepository.saveAndFlush(instalacion);

        int databaseSizeBeforeUpdate = instalacionRepository.findAll().size();

        // Update the instalacion using partial update
        Instalacion partialUpdatedInstalacion = new Instalacion();
        partialUpdatedInstalacion.setId(instalacion.getId());

        restInstalacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstalacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstalacion))
            )
            .andExpect(status().isOk());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeUpdate);
        Instalacion testInstalacion = instalacionList.get(instalacionList.size() - 1);
        assertThat(testInstalacion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testInstalacion.getPrecioPorHora()).isEqualTo(DEFAULT_PRECIO_POR_HORA);
        assertThat(testInstalacion.getDisponible()).isEqualTo(DEFAULT_DISPONIBLE);
    }

    @Test
    @Transactional
    void fullUpdateInstalacionWithPatch() throws Exception {
        // Initialize the database
        instalacionRepository.saveAndFlush(instalacion);

        int databaseSizeBeforeUpdate = instalacionRepository.findAll().size();

        // Update the instalacion using partial update
        Instalacion partialUpdatedInstalacion = new Instalacion();
        partialUpdatedInstalacion.setId(instalacion.getId());

        partialUpdatedInstalacion.nombre(UPDATED_NOMBRE).precioPorHora(UPDATED_PRECIO_POR_HORA).disponible(UPDATED_DISPONIBLE);

        restInstalacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstalacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstalacion))
            )
            .andExpect(status().isOk());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeUpdate);
        Instalacion testInstalacion = instalacionList.get(instalacionList.size() - 1);
        assertThat(testInstalacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testInstalacion.getPrecioPorHora()).isEqualTo(UPDATED_PRECIO_POR_HORA);
        assertThat(testInstalacion.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void patchNonExistingInstalacion() throws Exception {
        int databaseSizeBeforeUpdate = instalacionRepository.findAll().size();
        instalacion.setId(count.incrementAndGet());

        // Create the Instalacion
        InstalacionDTO instalacionDTO = instalacionMapper.toDto(instalacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstalacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, instalacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(instalacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstalacion() throws Exception {
        int databaseSizeBeforeUpdate = instalacionRepository.findAll().size();
        instalacion.setId(count.incrementAndGet());

        // Create the Instalacion
        InstalacionDTO instalacionDTO = instalacionMapper.toDto(instalacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstalacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(instalacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstalacion() throws Exception {
        int databaseSizeBeforeUpdate = instalacionRepository.findAll().size();
        instalacion.setId(count.incrementAndGet());

        // Create the Instalacion
        InstalacionDTO instalacionDTO = instalacionMapper.toDto(instalacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstalacionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(instalacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instalacion in the database
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInstalacion() throws Exception {
        // Initialize the database
        instalacionRepository.saveAndFlush(instalacion);

        int databaseSizeBeforeDelete = instalacionRepository.findAll().size();

        // Delete the instalacion
        restInstalacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, instalacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Instalacion> instalacionList = instalacionRepository.findAll();
        assertThat(instalacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
