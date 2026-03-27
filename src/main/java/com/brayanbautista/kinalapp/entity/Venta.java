package com.brayanbautista.kinalapp.entity;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name= "Ventas")
public class Venta {
    @Id
    @Column(name = "codigo_venta")
    private int CodigoVenta;
    @Column
    private Long FechaVenta;
    @Column(precision = 10, scale = 2)
    private BigDecimal total;
    @Column
    private int Estado;

    @ManyToOne
    @JoinColumn(name = "Clientes_dpi_cliente", referencedColumnName = "dpi_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "Usuarios_codigo_usuario", referencedColumnName = "codigo_usuario")
    private Usuario usuario;

    public Venta() {
    }

    public Venta(int codigoVenta, Long fechaVenta, BigDecimal total, int estado, Cliente cliente, Usuario usuario) {
        CodigoVenta = codigoVenta;
        FechaVenta = fechaVenta;
        this.total = total;
        Estado = estado;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    public int getCodigoVenta() {
        return CodigoVenta;
    }

    public void setCodigoVenta(int codigoVenta) {
        CodigoVenta = codigoVenta;
    }

    public Long getFechaVenta() {
        return FechaVenta;
    }

    public void setFechaVenta(Long fechaVenta) {
        FechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
