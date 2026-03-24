package com.brayanbautista.kinalapp.controller;

import com.brayanbautista.kinalapp.entity.Cliente;
import com.brayanbautista.kinalapp.repository.ClienteRepository;
import com.brayanbautista.kinalapp.service.ClienteService;
import com.brayanbautista.kinalapp.service.IClientesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = el servicio de controller + @ResponseBody
@RequestMapping("/clientes")
//Todas las rutas en este controlador deben empezar con /clientes
public class ClienteController {
    //Inyectamos el servicio y no el repositorio
    //El controlador solo debe tener conexion con el servicio
    private final IClientesService clienteService;

    //Como buena practica la Inyeccion de dependecias debe hacerse por el Constructor
    public ClienteController(IClientesService clienteService) {
        this.clienteService = clienteService;
    }

    //Responde a peticiones GET
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Cliente>>listar(){
        List<Cliente> clientes=clienteService.listarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(clientes);
        //200 ok con la lista de clientes
    }

    //{dpi} es una variable de ruta(valor a buscar)
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI(@PathVariable String dpi){
        //@PathVariable toma el valor de la URL y lo asigna al dpi
        return clienteService.buscarPorDPI(dpi)
                //Si el Optional tiene valor, devuelve 200 ok con el cliente
                .map(ResponseEntity::ok)
                //Si el optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    //Post crea un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente){
        //RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Cliente
        //<?> significa "tipo generico" puede ser un Cliente o un String
        try{
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //Intentamos guardar el cliente pero puede lanzar una excepcion
            //de IllegalArgumentException
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
            //201 CREATED(mucho mas especifico que el 2200 para la creacion de uyn cliente)
        }catch(IllegalArgumentException e) {
            //si hay error de validacion
            return ResponseEntity.badRequest().body(e.getMessage());
            //400 BAD REQUEST con el mensaje de error
        }
    }

    //DELETE elimina un Cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable String dpi){
        //ResponseEntity<Void>: no devuelve cuerpo en la respuesta
        try{
            if(!clienteService.existePorDPI(dpi)){
                return ResponseEntity.notFound().build();
                //404 NOT FOUND
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
            //204 NOT CONTENT (se ejecuto correctamente y no devuelve cuerpo)
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }
    }

    //Actualizar cliente a traves de DPI
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi, @RequestBody Cliente cliente){
        try{
            if(!clienteService.existePorDPI(dpi)){
                //Verifica si existe antes de actualizar
                //404 Not Found
                return ResponseEntity.notFound().build();
            }
            //Actualizar el cliente pero puede lanzar una excepcion
            Cliente clienteActualizar = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizar);
            //200 ok con el cliente ya actualizado
        }catch(IllegalArgumentException e){
            //Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e){
            //Posiblemente cualquier otro error como: Cliente no encontrado, etc
            //Error 404 not found
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/estado/{valor}")
    public ResponseEntity<List<Cliente>> listarPorEstado(@PathVariable int valor) {
        return ResponseEntity.ok(clienteService.obtenerPorEstado(valor));
    }

}
