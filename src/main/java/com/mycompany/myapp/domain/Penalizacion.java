package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Penalizacion.
 */
@Entity
@Table(name = "penalizacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Penalizacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "total_a_pagar")
    private Double totalAPagar;

    @JsonIgnoreProperties(value = { "penalizacion", "reservas" }, allowSetters = true)
    @OneToOne(mappedBy = "penalizacion")
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Penalizacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public Penalizacion motivo(String motivo) {
        this.setMotivo(motivo);
        return this;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Double getTotalAPagar() {
        return this.totalAPagar;
    }

    public Penalizacion totalAPagar(Double totalAPagar) {
        this.setTotalAPagar(totalAPagar);
        return this;
    }

    public void setTotalAPagar(Double totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        if (this.cliente != null) {
            this.cliente.setPenalizacion(null);
        }
        if (cliente != null) {
            cliente.setPenalizacion(this);
        }
        this.cliente = cliente;
    }

    public Penalizacion cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Penalizacion)) {
            return false;
        }
        return id != null && id.equals(((Penalizacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Penalizacion{" +
            "id=" + getId() +
            ", motivo='" + getMotivo() + "'" +
            ", totalAPagar=" + getTotalAPagar() +
            "}";
    }
}
