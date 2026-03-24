package com.brayanbautista.kinalapp.service;

import com.brayanbautista.kinalapp.entity.Cliente;
import com.brayanbautista.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService implements IClientesService {

    private final ClienteRepository clienteRepository;

    // Inyección de dependencias por constructor (BUENA PRÁCTICA)
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }


    @Override
    public Cliente guardar(Cliente cliente) {

        validarCliente(cliente);

        // Si no se envía estado, lo ponemos activo por defecto
        if (cliente.getEstado() != 0 && cliente.getEstado() != 1) {
            cliente.setEstado(1);
        }

        return clienteRepository.save(cliente);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        return clienteRepository.findById(dpi);
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {

        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("Cliente no se encontró con DPI: " + dpi);
        }

        // Usamos el DPI de la URL por seguridad
        cliente.setDPICliente(dpi);

        validarCliente(cliente);

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {

        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("El cliente no se encontró con el DPI: " + dpi);
        }

        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        return clienteRepository.existsById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    //Este metodo solo lee datos
    public List<Cliente> obtenerPorEstado(int estado) {
    //devuelve una lista de los objetos de Cliente
    //Recibe un entero
        if (estado != 0 && estado != 1) {
            throw new IllegalArgumentException("Solo puede ser 1 y 0");
        }

        return clienteRepository.findByEstado(estado);
        //consulta al repository
    }


    private void validarCliente(Cliente cliente) {

        if (cliente.getDPICliente() == null || cliente.getDPICliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }

        if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }

        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es un dato obligatorio");
        }
    }
}