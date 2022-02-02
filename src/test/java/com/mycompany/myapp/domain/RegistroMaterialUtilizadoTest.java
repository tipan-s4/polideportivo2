package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegistroMaterialUtilizadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistroMaterialUtilizado.class);
        RegistroMaterialUtilizado registroMaterialUtilizado1 = new RegistroMaterialUtilizado();
        registroMaterialUtilizado1.setId(1L);
        RegistroMaterialUtilizado registroMaterialUtilizado2 = new RegistroMaterialUtilizado();
        registroMaterialUtilizado2.setId(registroMaterialUtilizado1.getId());
        assertThat(registroMaterialUtilizado1).isEqualTo(registroMaterialUtilizado2);
        registroMaterialUtilizado2.setId(2L);
        assertThat(registroMaterialUtilizado1).isNotEqualTo(registroMaterialUtilizado2);
        registroMaterialUtilizado1.setId(null);
        assertThat(registroMaterialUtilizado1).isNotEqualTo(registroMaterialUtilizado2);
    }
}
