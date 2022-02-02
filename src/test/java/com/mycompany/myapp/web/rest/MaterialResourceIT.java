package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.repository.MaterialRepository;
import com.mycompany.myapp.service.dto.MaterialDTO;
import com.mycompany.myapp.service.mapper.MaterialMapper;
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
 * Integration tests for the {@link MaterialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD_RESERVADA = 1;
    private static final Integer UPDATED_CANTIDAD_RESERVADA = 2;

    private static final Integer DEFAULT_CANTIDAD_DISPONIBLE = 1;
    private static final Integer UPDATED_CANTIDAD_DISPONIBLE = 2;

    private static final String ENTITY_API_URL = "/api/materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialMockMvc;

    private Material material;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createEntity(EntityManager em) {
        Material material = new Material()
            .nombre(DEFAULT_NOMBRE)
            .cantidadReservada(DEFAULT_CANTIDAD_RESERVADA)
            .cantidadDisponible(DEFAULT_CANTIDAD_DISPONIBLE);
        return material;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createUpdatedEntity(EntityManager em) {
        Material material = new Material()
            .nombre(UPDATED_NOMBRE)
            .cantidadReservada(UPDATED_CANTIDAD_RESERVADA)
            .cantidadDisponible(UPDATED_CANTIDAD_DISPONIBLE);
        return material;
    }

    @BeforeEach
    public void initTest() {
        material = createEntity(em);
    }

    @Test
    @Transactional
    void createMaterial() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();
        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);
        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isCreated());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate + 1);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMaterial.getCantidadReservada()).isEqualTo(DEFAULT_CANTIDAD_RESERVADA);
        assertThat(testMaterial.getCantidadDisponible()).isEqualTo(DEFAULT_CANTIDAD_DISPONIBLE);
    }

    @Test
    @Transactional
    void createMaterialWithExistingId() throws Exception {
        // Create the Material with an existing ID
        material.setId(1L);
        MaterialDTO materialDTO = materialMapper.toDto(material);

        int databaseSizeBeforeCreate = materialRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterials() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].cantidadReservada").value(hasItem(DEFAULT_CANTIDAD_RESERVADA)))
            .andExpect(jsonPath("$.[*].cantidadDisponible").value(hasItem(DEFAULT_CANTIDAD_DISPONIBLE)));
    }

    @Test
    @Transactional
    void getMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL_ID, material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.cantidadReservada").value(DEFAULT_CANTIDAD_RESERVADA))
            .andExpect(jsonPath("$.cantidadDisponible").value(DEFAULT_CANTIDAD_DISPONIBLE));
    }

    @Test
    @Transactional
    void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material
        Material updatedMaterial = materialRepository.findById(material.getId()).get();
        // Disconnect from session so that the updates on updatedMaterial are not directly saved in db
        em.detach(updatedMaterial);
        updatedMaterial
            .nombre(UPDATED_NOMBRE)
            .cantidadReservada(UPDATED_CANTIDAD_RESERVADA)
            .cantidadDisponible(UPDATED_CANTIDAD_DISPONIBLE);
        MaterialDTO materialDTO = materialMapper.toDto(updatedMaterial);

        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(materialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMaterial.getCantidadReservada()).isEqualTo(UPDATED_CANTIDAD_RESERVADA);
        assertThat(testMaterial.getCantidadDisponible()).isEqualTo(UPDATED_CANTIDAD_DISPONIBLE);
    }

    @Test
    @Transactional
    void putNonExistingMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(count.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(count.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(count.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial.nombre(UPDATED_NOMBRE).cantidadReservada(UPDATED_CANTIDAD_RESERVADA);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMaterial.getCantidadReservada()).isEqualTo(UPDATED_CANTIDAD_RESERVADA);
        assertThat(testMaterial.getCantidadDisponible()).isEqualTo(DEFAULT_CANTIDAD_DISPONIBLE);
    }

    @Test
    @Transactional
    void fullUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial
            .nombre(UPDATED_NOMBRE)
            .cantidadReservada(UPDATED_CANTIDAD_RESERVADA)
            .cantidadDisponible(UPDATED_CANTIDAD_DISPONIBLE);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMaterial.getCantidadReservada()).isEqualTo(UPDATED_CANTIDAD_RESERVADA);
        assertThat(testMaterial.getCantidadDisponible()).isEqualTo(UPDATED_CANTIDAD_DISPONIBLE);
    }

    @Test
    @Transactional
    void patchNonExistingMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(count.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(count.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(count.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(materialDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeDelete = materialRepository.findAll().size();

        // Delete the material
        restMaterialMockMvc
            .perform(delete(ENTITY_API_URL_ID, material.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
