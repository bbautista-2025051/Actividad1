package com.brayanbautista.kinalapp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name= "detalle_venta")
public class DetalleVenta {
    @Id
    @Column(name ="codigo_venta")
    private int codigoVenta;
    @Column
    private int cantidad;
    @Column(precision = 10, scale = 2)
    private BigDecimal PrecioUnitario;
    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Ventas_codigo_venta", referencedColumnName = "codigo_venta")
    private Venta venta;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Productos_codigo_producto", referencedColumnName = "codigo_producto")
    private Producto producto;

    public DetalleVenta() {
    }

    public DetalleVenta(int codigoVenta, int cantidad, BigDecimal precioUnitario, Venta venta, BigDecimal subtotal, Producto producto) {
        this.codigoVenta = codigoVenta;
        this.cantidad = cantidad;
        PrecioUnitario = precioUnitario;
        this.venta = venta;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    public int getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(int codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return PrecioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        PrecioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
