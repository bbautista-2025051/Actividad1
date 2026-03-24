package com.brayanbautista.kinalapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table(name = "ventas")
public class Ventas {
    @Id
    @Column(name = "codigo_ventas")
    private int CodigoVentas;
    @Column
    private Date FechaVentas;
    @Column
    private Float total;
    @Column
    private int Estado;
    
}
