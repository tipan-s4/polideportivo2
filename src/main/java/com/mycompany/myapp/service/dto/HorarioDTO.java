package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Horario} entity.
 */
public class HorarioDTO implements Serializable {

    private Long id;

    private LocalDate dia;

    private Integer hora;

    private InstalacionDTO instalacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public InstalacionDTO getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(InstalacionDTO instalacion) {
        this.instalacion = instalacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HorarioDTO)) {
            return false;
        }

        HorarioDTO horarioDTO = (HorarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, horarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HorarioDTO{" +
            "id=" + getId() +
            ", dia='" + getDia() + "'" +
            ", hora=" + getHora() +
            ", instalacion=" + getInstalacion() +
            "}";
    }
}
