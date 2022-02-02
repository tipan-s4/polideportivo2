package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Reserva} entity.
 */
public class ReservaDTO implements Serializable {

    private Long id;

    private LocalDate fecha;

    private Integer hora;

    private String tipoPago;

    private Integer total;

    private RegistroMaterialUtilizadoDTO registros;

    private InstalacionDTO instalacion;

    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public RegistroMaterialUtilizadoDTO getRegistros() {
        return registros;
    }

    public void setRegistros(RegistroMaterialUtilizadoDTO registros) {
        this.registros = registros;
    }

    public InstalacionDTO getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(InstalacionDTO instalacion) {
        this.instalacion = instalacion;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservaDTO)) {
            return false;
        }

        ReservaDTO reservaDTO = (ReservaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reservaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservaDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", hora=" + getHora() +
            ", tipoPago='" + getTipoPago() + "'" +
            ", total=" + getTotal() +
            ", registros=" + getRegistros() +
            ", instalacion=" + getInstalacion() +
            ", cliente=" + getCliente() +
            "}";
    }
}
