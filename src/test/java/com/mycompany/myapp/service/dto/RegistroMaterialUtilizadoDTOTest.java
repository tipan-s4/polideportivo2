package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegistroMaterialUtilizadoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistroMaterialUtilizadoDTO.class);
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO1 = new RegistroMaterialUtilizadoDTO();
        registroMaterialUtilizadoDTO1.setId(1L);
        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO2 = new RegistroMaterialUtilizadoDTO();
        assertThat(registroMaterialUtilizadoDTO1).isNotEqualTo(registroMaterialUtilizadoDTO2);
        registroMaterialUtilizadoDTO2.setId(registroMaterialUtilizadoDTO1.getId());
        assertThat(registroMaterialUtilizadoDTO1).isEqualTo(registroMaterialUtilizadoDTO2);
        registroMaterialUtilizadoDTO2.setId(2L);
        assertThat(registroMaterialUtilizadoDTO1).isNotEqualTo(registroMaterialUtilizadoDTO2);
        registroMaterialUtilizadoDTO1.setId(null);
        assertThat(registroMaterialUtilizadoDTO1).isNotEqualTo(registroMaterialUtilizadoDTO2);
    }
}
