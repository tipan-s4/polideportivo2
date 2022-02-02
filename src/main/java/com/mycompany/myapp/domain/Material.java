package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Material.
 */
@Entity
@Table(name = "material")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad_reservada")
    private Integer cantidadReservada;

    @Column(name = "cantidad_disponible")
    private Integer cantidadDisponible;

    @OneToMany(mappedBy = "material")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reserva", "material" }, allowSetters = true)
    private Set<RegistroMaterialUtilizado> registros = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "horarios", "materiales", "reservas" }, allowSetters = true)
    private Instalacion instalaciones;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Material id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Material nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidadReservada() {
        return this.cantidadReservada;
    }

    public Material cantidadReservada(Integer cantidadReservada) {
        this.setCantidadReservada(cantidadReservada);
        return this;
    }

    public void setCantidadReservada(Integer cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }

    public Integer getCantidadDisponible() {
        return this.cantidadDisponible;
    }

    public Material cantidadDisponible(Integer cantidadDisponible) {
        this.setCantidadDisponible(cantidadDisponible);
        return this;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Set<RegistroMaterialUtilizado> getRegistros() {
        return this.registros;
    }

    public void setRegistros(Set<RegistroMaterialUtilizado> registroMaterialUtilizados) {
        if (this.registros != null) {
            this.registros.forEach(i -> i.setMaterial(null));
        }
        if (registroMaterialUtilizados != null) {
            registroMaterialUtilizados.forEach(i -> i.setMaterial(this));
        }
        this.registros = registroMaterialUtilizados;
    }

    public Material registros(Set<RegistroMaterialUtilizado> registroMaterialUtilizados) {
        this.setRegistros(registroMaterialUtilizados);
        return this;
    }

    public Material addRegistro(RegistroMaterialUtilizado registroMaterialUtilizado) {
        this.registros.add(registroMaterialUtilizado);
        registroMaterialUtilizado.setMaterial(this);
        return this;
    }

    public Material removeRegistro(RegistroMaterialUtilizado registroMaterialUtilizado) {
        this.registros.remove(registroMaterialUtilizado);
        registroMaterialUtilizado.setMaterial(null);
        return this;
    }

    public Instalacion getInstalaciones() {
        return this.instalaciones;
    }

    public void setInstalaciones(Instalacion instalacion) {
        this.instalaciones = instalacion;
    }

    public Material instalaciones(Instalacion instalacion) {
        this.setInstalaciones(instalacion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Material)) {
            return false;
        }
        return id != null && id.equals(((Material) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Material{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", cantidadReservada=" + getCantidadReservada() +
            ", cantidadDisponible=" + getCantidadDisponible() +
            "}";
    }
}
