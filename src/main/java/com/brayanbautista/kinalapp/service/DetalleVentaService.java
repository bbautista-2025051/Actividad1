package com.brayanbautista.kinalapp.service;

import com.brayanbautista.kinalapp.entity.DetalleVenta;
import com.brayanbautista.kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalle) {
        validarDetalle(detalle);
        if (detalle.getSubtotal() == null) {
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }
        return detalleVentaRepository.save(detalle);
    }

    @Override
    public DetalleVenta actualizar(Long id, DetalleVenta detalle) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new RuntimeException("Detalle de venta no encontrado con ID: " + id);
        }
        detalle.setCodigoDetalleVenta(id);
        validarDetalle(detalle);
        if (detalle.getSubtotal() == null) {
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }
        return detalleVentaRepository.save(detalle);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorId(Long id) {
        return detalleVentaRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new RuntimeException("Detalle de venta no encontrado con ID: " + id);
        }
        detalleVentaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return detalleVentaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> obtenerPorVenta(Long ventaId) {
        return detalleVentaRepository.findByVenta_CodigoVenta(ventaId);
    }

    private void validarDetalle(DetalleVenta detalle) {
        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor que cero");
        }
        if (detalle.getProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (detalle.getVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }
    }
}