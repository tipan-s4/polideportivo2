package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.RegistroMaterialUtilizado} entity.
 */
public class RegistroMaterialUtilizadoDTO implements Serializable {

    private Long id;

    private String nombre;

    private Integer cantidad;

    private LocalDate fecha;

    private MaterialDTO material;

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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public MaterialDTO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialDTO material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistroMaterialUtilizadoDTO)) {
            return false;
        }

        RegistroMaterialUtilizadoDTO registroMaterialUtilizadoDTO = (RegistroMaterialUtilizadoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, registroMaterialUtilizadoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroMaterialUtilizadoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", cantidad=" + getCantidad() +
            ", fecha='" + getFecha() + "'" +
            ", material=" + getMaterial() +
            "}";
    }
}
