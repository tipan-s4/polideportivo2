package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Horario.
 */
@Entity
@Table(name = "horario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Horario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dia")
    private LocalDate dia;

    @Column(name = "hora")
    private Integer hora;

    @ManyToOne
    @JsonIgnoreProperties(value = { "horarios", "materiales", "reservas" }, allowSetters = true)
    private Instalacion instalacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Horario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDia() {
        return this.dia;
    }

    public Horario dia(LocalDate dia) {
        this.setDia(dia);
        return this;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public Integer getHora() {
        return this.hora;
    }

    public Horario hora(Integer hora) {
        this.setHora(hora);
        return this;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Instalacion getInstalacion() {
        return this.instalacion;
    }

    public void setInstalacion(Instalacion instalacion) {
        this.instalacion = instalacion;
    }

    public Horario instalacion(Instalacion instalacion) {
        this.setInstalacion(instalacion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Horario)) {
            return false;
        }
        return id != null && id.equals(((Horario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Horario{" +
            "id=" + getId() +
            ", dia='" + getDia() + "'" +
            ", hora=" + getHora() +
            "}";
    }
}
