package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dni")
    private String dni;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "telefono")
    private Integer telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "edad")
    private String edad;

    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Penalizacion penalizacion;

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "registros", "instalacion", "cliente" }, allowSetters = true)
    private Set<Reserva> reservas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return this.dni;
    }

    public Cliente dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Cliente apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getTelefono() {
        return this.telefono;
    }

    public Cliente telefono(Integer telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Cliente direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEdad() {
        return this.edad;
    }

    public Cliente edad(String edad) {
        this.setEdad(edad);
        return this;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public Penalizacion getPenalizacion() {
        return this.penalizacion;
    }

    public void setPenalizacion(Penalizacion penalizacion) {
        this.penalizacion = penalizacion;
    }

    public Cliente penalizacion(Penalizacion penalizacion) {
        this.setPenalizacion(penalizacion);
        return this;
    }

    public Set<Reserva> getReservas() {
        return this.reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        if (this.reservas != null) {
            this.reservas.forEach(i -> i.setCliente(null));
        }
        if (reservas != null) {
            reservas.forEach(i -> i.setCliente(this));
        }
        this.reservas = reservas;
    }

    public Cliente reservas(Set<Reserva> reservas) {
        this.setReservas(reservas);
        return this;
    }

    public Cliente addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setCliente(this);
        return this;
    }

    public Cliente removeReserva(Reserva reserva) {
        this.reservas.remove(reserva);
        reserva.setCliente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", dni='" + getDni() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", telefono=" + getTelefono() +
            ", direccion='" + getDireccion() + "'" +
            ", edad='" + getEdad() + "'" +
            "}";
    }
}
