package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstalacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstalacionDTO.class);
        InstalacionDTO instalacionDTO1 = new InstalacionDTO();
        instalacionDTO1.setId(1L);
        InstalacionDTO instalacionDTO2 = new InstalacionDTO();
        assertThat(instalacionDTO1).isNotEqualTo(instalacionDTO2);
        instalacionDTO2.setId(instalacionDTO1.getId());
        assertThat(instalacionDTO1).isEqualTo(instalacionDTO2);
        instalacionDTO2.setId(2L);
        assertThat(instalacionDTO1).isNotEqualTo(instalacionDTO2);
        instalacionDTO1.setId(null);
        assertThat(instalacionDTO1).isNotEqualTo(instalacionDTO2);
    }
}
