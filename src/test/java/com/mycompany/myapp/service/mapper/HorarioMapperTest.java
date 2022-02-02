package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HorarioMapperTest {

    private HorarioMapper horarioMapper;

    @BeforeEach
    public void setUp() {
        horarioMapper = new HorarioMapperImpl();
    }
}
