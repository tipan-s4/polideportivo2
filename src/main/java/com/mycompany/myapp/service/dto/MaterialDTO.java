package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Material} entity.
 */
public class MaterialDTO implements Serializable {

    private Long id;

    private String nombre;

    private Integer cantidadReservada;

    private Integer cantidadDisponible;

    private InstalacionDTO instalaciones;

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

    public Integer getCantidadReservada() {
        return cantidadReservada;
    }

    public void setCantidadReservada(Integer cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public InstalacionDTO getInstalaciones() {
        return instalaciones;
    }

    public void setInstalaciones(InstalacionDTO instalaciones) {
        this.instalaciones = instalaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialDTO)) {
            return false;
        }

        MaterialDTO materialDTO = (MaterialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, materialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", cantidadReservada=" + getCantidadReservada() +
            ", cantidadDisponible=" + getCantidadDisponible() +
            ", instalaciones=" + getInstalaciones() +
            "}";
    }
}
