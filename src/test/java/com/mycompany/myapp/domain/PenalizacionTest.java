package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PenalizacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Penalizacion.class);
        Penalizacion penalizacion1 = new Penalizacion();
        penalizacion1.setId(1L);
        Penalizacion penalizacion2 = new Penalizacion();
        penalizacion2.setId(penalizacion1.getId());
        assertThat(penalizacion1).isEqualTo(penalizacion2);
        penalizacion2.setId(2L);
        assertThat(penalizacion1).isNotEqualTo(penalizacion2);
        penalizacion1.setId(null);
        assertThat(penalizacion1).isNotEqualTo(penalizacion2);
    }
}
