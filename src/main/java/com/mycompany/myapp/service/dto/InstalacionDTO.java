package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Instalacion} entity.
 */
public class InstalacionDTO implements Serializable {

    private Long id;

    private String nombre;

    private Double precioPorHora;

    private Boolean disponible;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioPorHora() {
        return precioPorHora;
    }

    public void setPrecioPorHora(Double precioPorHora) {
        this.precioPorHora = precioPorHora;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstalacionDTO)) {
            return false;
        }

        InstalacionDTO instalacionDTO = (InstalacionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, instalacionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstalacionDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", precioPorHora=" + getPrecioPorHora() +
            ", disponible='" + getDisponible() + "'" +
            "}";
    }
}
