package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reserva.
 */
@Entity
@Table(name = "reserva")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private Integer hora;

    @Column(name = "tipo_pago")
    private String tipoPago;

    @Column(name = "total")
    private Integer total;

    @JsonIgnoreProperties(value = { "reserva", "material" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private RegistroMaterialUtilizado registros;

    @JsonIgnoreProperties(value = { "horarios", "materiales", "reservas" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Instalacion instalacion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "penalizacion", "reservas" }, allowSetters = true)
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reserva id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Reserva fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getHora() {
        return this.hora;
    }

    public Reserva hora(Integer hora) {
        this.setHora(hora);
        return this;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public String getTipoPago() {
        return this.tipoPago;
    }

    public Reserva tipoPago(String tipoPago) {
        this.setTipoPago(tipoPago);
        return this;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Integer getTotal() {
        return this.total;
    }

    public Reserva total(Integer total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public RegistroMaterialUtilizado getRegistros() {
        return this.registros;
    }

    public void setRegistros(RegistroMaterialUtilizado registroMaterialUtilizado) {
        this.registros = registroMaterialUtilizado;
    }

    public Reserva registros(RegistroMaterialUtilizado registroMaterialUtilizado) {
        this.setRegistros(registroMaterialUtilizado);
        return this;
    }

    public Instalacion getInstalacion() {
        return this.instalacion;
    }

    public void setInstalacion(Instalacion instalacion) {
        this.instalacion = instalacion;
    }

    public Reserva instalacion(Instalacion instalacion) {
        this.setInstalacion(instalacion);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Reserva cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reserva)) {
            return false;
        }
        return id != null && id.equals(((Reserva) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reserva{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", hora=" + getHora() +
            ", tipoPago='" + getTipoPago() + "'" +
            ", total=" + getTotal() +
            "}";
    }
}
