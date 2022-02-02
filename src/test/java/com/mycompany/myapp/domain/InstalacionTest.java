package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstalacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instalacion.class);
        Instalacion instalacion1 = new Instalacion();
        instalacion1.setId(1L);
        Instalacion instalacion2 = new Instalacion();
        instalacion2.setId(instalacion1.getId());
        assertThat(instalacion1).isEqualTo(instalacion2);
        instalacion2.setId(2L);
        assertThat(instalacion1).isNotEqualTo(instalacion2);
        instalacion1.setId(null);
        assertThat(instalacion1).isNotEqualTo(instalacion2);
    }
}
