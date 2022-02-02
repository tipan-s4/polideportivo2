package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RegistroMaterialUtilizado.
 */
@Entity
@Table(name = "registro_material_utilizado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RegistroMaterialUtilizado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha")
    private LocalDate fecha;

    @JsonIgnoreProperties(value = { "registros", "instalacion", "cliente" }, allowSetters = true)
    @OneToOne(mappedBy = "registros")
    private Reserva reserva;

    @ManyToOne
    @JsonIgnoreProperties(value = { "registros", "instalaciones" }, allowSetters = true)
    private Material material;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RegistroMaterialUtilizado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public RegistroMaterialUtilizado nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public RegistroMaterialUtilizado cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public RegistroMaterialUtilizado fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Reserva getReserva() {
        return this.reserva;
    }

    public void setReserva(Reserva reserva) {
        if (this.reserva != null) {
            this.reserva.setRegistros(null);
        }
        if (reserva != null) {
            reserva.setRegistros(this);
        }
        this.reserva = reserva;
    }

    public RegistroMaterialUtilizado reserva(Reserva reserva) {
        this.setReserva(reserva);
        return this;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public RegistroMaterialUtilizado material(Material material) {
        this.setMaterial(material);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistroMaterialUtilizado)) {
            return false;
        }
        return id != null && id.equals(((RegistroMaterialUtilizado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroMaterialUtilizado{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", cantidad=" + getCantidad() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
