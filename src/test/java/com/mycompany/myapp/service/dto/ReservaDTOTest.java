package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservaDTO.class);
        ReservaDTO reservaDTO1 = new ReservaDTO();
        reservaDTO1.setId(1L);
        ReservaDTO reservaDTO2 = new ReservaDTO();
        assertThat(reservaDTO1).isNotEqualTo(reservaDTO2);
        reservaDTO2.setId(reservaDTO1.getId());
        assertThat(reservaDTO1).isEqualTo(reservaDTO2);
        reservaDTO2.setId(2L);
        assertThat(reservaDTO1).isNotEqualTo(reservaDTO2);
        reservaDTO1.setId(null);
        assertThat(reservaDTO1).isNotEqualTo(reservaDTO2);
    }
}
