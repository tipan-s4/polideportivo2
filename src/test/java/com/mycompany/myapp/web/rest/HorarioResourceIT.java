package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Horario;
import com.mycompany.myapp.repository.HorarioRepository;
import com.mycompany.myapp.service.dto.HorarioDTO;
import com.mycompany.myapp.service.mapper.HorarioMapper;
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
 * Integration tests for the {@link HorarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HorarioResourceIT {

    private static final LocalDate DEFAULT_DIA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DIA = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_HORA = 1;
    private static final Integer UPDATED_HORA = 2;

    private static final String ENTITY_API_URL = "/api/horarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private HorarioMapper horarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHorarioMockMvc;

    private Horario horario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horario createEntity(EntityManager em) {
        Horario horario = new Horario().dia(DEFAULT_DIA).hora(DEFAULT_HORA);
        return horario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horario createUpdatedEntity(EntityManager em) {
        Horario horario = new Horario().dia(UPDATED_DIA).hora(UPDATED_HORA);
        return horario;
    }

    @BeforeEach
    public void initTest() {
        horario = createEntity(em);
    }

    @Test
    @Transactional
    void createHorario() throws Exception {
        int databaseSizeBeforeCreate = horarioRepository.findAll().size();
        // Create the Horario
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);
        restHorarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeCreate + 1);
        Horario testHorario = horarioList.get(horarioList.size() - 1);
        assertThat(testHorario.getDia()).isEqualTo(DEFAULT_DIA);
        assertThat(testHorario.getHora()).isEqualTo(DEFAULT_HORA);
    }

    @Test
    @Transactional
    void createHorarioWithExistingId() throws Exception {
        // Create the Horario with an existing ID
        horario.setId(1L);
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        int databaseSizeBeforeCreate = horarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHorarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHorarios() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList
        restHorarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horario.getId().intValue())))
            .andExpect(jsonPath("$.[*].dia").value(hasItem(DEFAULT_DIA.toString())))
            .andExpect(jsonPath("$.[*].hora").value(hasItem(DEFAULT_HORA)));
    }

    @Test
    @Transactional
    void getHorario() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get the horario
        restHorarioMockMvc
            .perform(get(ENTITY_API_URL_ID, horario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(horario.getId().intValue()))
            .andExpect(jsonPath("$.dia").value(DEFAULT_DIA.toString()))
            .andExpect(jsonPath("$.hora").value(DEFAULT_HORA));
    }

    @Test
    @Transactional
    void getNonExistingHorario() throws Exception {
        // Get the horario
        restHorarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHorario() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();

        // Update the horario
        Horario updatedHorario = horarioRepository.findById(horario.getId()).get();
        // Disconnect from session so that the updates on updatedHorario are not directly saved in db
        em.detach(updatedHorario);
        updatedHorario.dia(UPDATED_DIA).hora(UPDATED_HORA);
        HorarioDTO horarioDTO = horarioMapper.toDto(updatedHorario);

        restHorarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, horarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
        Horario testHorario = horarioList.get(horarioList.size() - 1);
        assertThat(testHorario.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testHorario.getHora()).isEqualTo(UPDATED_HORA);
    }

    @Test
    @Transactional
    void putNonExistingHorario() throws Exception {
        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();
        horario.setId(count.incrementAndGet());

        // Create the Horario
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHorarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, horarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHorario() throws Exception {
        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();
        horario.setId(count.incrementAndGet());

        // Create the Horario
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHorarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHorario() throws Exception {
        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();
        horario.setId(count.incrementAndGet());

        // Create the Horario
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHorarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHorarioWithPatch() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();

        // Update the horario using partial update
        Horario partialUpdatedHorario = new Horario();
        partialUpdatedHorario.setId(horario.getId());

        partialUpdatedHorario.dia(UPDATED_DIA);

        restHorarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHorario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHorario))
            )
            .andExpect(status().isOk());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
        Horario testHorario = horarioList.get(horarioList.size() - 1);
        assertThat(testHorario.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testHorario.getHora()).isEqualTo(DEFAULT_HORA);
    }

    @Test
    @Transactional
    void fullUpdateHorarioWithPatch() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();

        // Update the horario using partial update
        Horario partialUpdatedHorario = new Horario();
        partialUpdatedHorario.setId(horario.getId());

        partialUpdatedHorario.dia(UPDATED_DIA).hora(UPDATED_HORA);

        restHorarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHorario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHorario))
            )
            .andExpect(status().isOk());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
        Horario testHorario = horarioList.get(horarioList.size() - 1);
        assertThat(testHorario.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testHorario.getHora()).isEqualTo(UPDATED_HORA);
    }

    @Test
    @Transactional
    void patchNonExistingHorario() throws Exception {
        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();
        horario.setId(count.incrementAndGet());

        // Create the Horario
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHorarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, horarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHorario() throws Exception {
        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();
        horario.setId(count.incrementAndGet());

        // Create the Horario
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHorarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHorario() throws Exception {
        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();
        horario.setId(count.incrementAndGet());

        // Create the Horario
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHorarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(horarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHorario() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        int databaseSizeBeforeDelete = horarioRepository.findAll().size();

        // Delete the horario
        restHorarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, horario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
