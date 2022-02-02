package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Instalacion.
 */
@Entity
@Table(name = "instalacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Instalacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio_por_hora")
    private Double precioPorHora;

    @Column(name = "disponible")
    private Boolean disponible;

    @OneToMany(mappedBy = "instalacion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "instalacion" }, allowSetters = true)
    private Set<Horario> horarios = new HashSet<>();

    @OneToMany(mappedBy = "instalaciones")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "registros", "instalaciones" }, allowSetters = true)
    private Set<Material> materiales = new HashSet<>();

    @JsonIgnoreProperties(value = { "registros", "instalacion", "cliente" }, allowSetters = true)
    @OneToOne(mappedBy = "instalacion")
    private Reserva reservas;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Instalacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Instalacion nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioPorHora() {
        return this.precioPorHora;
    }

    public Instalacion precioPorHora(Double precioPorHora) {
        this.setPrecioPorHora(precioPorHora);
        return this;
    }

    public void setPrecioPorHora(Double precioPorHora) {
        this.precioPorHora = precioPorHora;
    }

    public Boolean getDisponible() {
        return this.disponible;
    }

    public Instalacion disponible(Boolean disponible) {
        this.setDisponible(disponible);
        return this;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Set<Horario> getHorarios() {
        return this.horarios;
    }

    public void setHorarios(Set<Horario> horarios) {
        if (this.horarios != null) {
            this.horarios.forEach(i -> i.setInstalacion(null));
        }
        if (horarios != null) {
            horarios.forEach(i -> i.setInstalacion(this));
        }
        this.horarios = horarios;
    }

    public Instalacion horarios(Set<Horario> horarios) {
        this.setHorarios(horarios);
        return this;
    }

    public Instalacion addHorario(Horario horario) {
        this.horarios.add(horario);
        horario.setInstalacion(this);
        return this;
    }

    public Instalacion removeHorario(Horario horario) {
        this.horarios.remove(horario);
        horario.setInstalacion(null);
        return this;
    }

    public Set<Material> getMateriales() {
        return this.materiales;
    }

    public void setMateriales(Set<Material> materials) {
        if (this.materiales != null) {
            this.materiales.forEach(i -> i.setInstalaciones(null));
        }
        if (materials != null) {
            materials.forEach(i -> i.setInstalaciones(this));
        }
        this.materiales = materials;
    }

    public Instalacion materiales(Set<Material> materials) {
        this.setMateriales(materials);
        return this;
    }

    public Instalacion addMateriales(Material material) {
        this.materiales.add(material);
        material.setInstalaciones(this);
        return this;
    }

    public Instalacion removeMateriales(Material material) {
        this.materiales.remove(material);
        material.setInstalaciones(null);
        return this;
    }

    public Reserva getReservas() {
        return this.reservas;
    }

    public void setReservas(Reserva reserva) {
        if (this.reservas != null) {
            this.reservas.setInstalacion(null);
        }
        if (reserva != null) {
            reserva.setInstalacion(this);
        }
        this.reservas = reserva;
    }

    public Instalacion reservas(Reserva reserva) {
        this.setReservas(reserva);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instalacion)) {
            return false;
        }
        return id != null && id.equals(((Instalacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Instalacion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", precioPorHora=" + getPrecioPorHora() +
            ", disponible='" + getDisponible() + "'" +
            "}";
    }
}
