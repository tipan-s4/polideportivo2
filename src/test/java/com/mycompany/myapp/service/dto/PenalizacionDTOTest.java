package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PenalizacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PenalizacionDTO.class);
        PenalizacionDTO penalizacionDTO1 = new PenalizacionDTO();
        penalizacionDTO1.setId(1L);
        PenalizacionDTO penalizacionDTO2 = new PenalizacionDTO();
        assertThat(penalizacionDTO1).isNotEqualTo(penalizacionDTO2);
        penalizacionDTO2.setId(penalizacionDTO1.getId());
        assertThat(penalizacionDTO1).isEqualTo(penalizacionDTO2);
        penalizacionDTO2.setId(2L);
        assertThat(penalizacionDTO1).isNotEqualTo(penalizacionDTO2);
        penalizacionDTO1.setId(null);
        assertThat(penalizacionDTO1).isNotEqualTo(penalizacionDTO2);
    }
}
