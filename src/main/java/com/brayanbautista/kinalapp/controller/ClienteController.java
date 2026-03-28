package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Cliente;
import com.brayanbautista.kinalapp.service.IClientesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final IClientesService clienteService;

    public ClienteController(IClientesService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI(@PathVariable String dpi) {
        return clienteService.buscarPorDPI(dpi)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.guardar(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable String dpi) {
        try {
            if (!clienteService.existePorDPI(dpi)) {
                return ResponseEntity.notFound().build();
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi, @RequestBody Cliente cliente) {
        try {
            if (!clienteService.existePorDPI(dpi)) {
                return ResponseEntity.notFound().build();
            }
            Cliente clienteActualizado = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{valor}")
    public ResponseEntity<List<Cliente>> listarPorEstado(@PathVariable int valor) {
        try {
            return ResponseEntity.ok(clienteService.obtenerPorEstado(valor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}