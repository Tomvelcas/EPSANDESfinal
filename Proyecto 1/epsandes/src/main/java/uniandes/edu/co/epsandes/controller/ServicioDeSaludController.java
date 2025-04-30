package uniandes.edu.co.epsandes.controller;

import uniandes.edu.co.epsandes.modelo.ServicioDeSalud;
import uniandes.edu.co.epsandes.servicio.ServicioDeSaludService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;

@RestController
@RequestMapping("/api/servicios")
public class ServicioDeSaludController {

    private static final Logger logger = LoggerFactory.getLogger(ServicioDeSaludController.class);

    @Autowired
    private ServicioDeSaludService servicioService;

    // RF2 - Registrar un servicio de salud
    @PostMapping
    public ResponseEntity<ServicioDeSalud> registrarServicio(@RequestBody ServicioDeSalud servicio) {
        logger.debug("POST /api/servicios - Body: {}", servicio);
        try {
            ServicioDeSalud nuevoServicio = servicioService.registrarServicio(servicio);
            logger.info("Servicio de salud creado: {}", nuevoServicio);
            return new ResponseEntity<>(nuevoServicio, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al registrar servicio de salud: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // RF3 - Asignar un servicio de salud a una IPS
    @PostMapping("/{servicioId}/ips/{ipsNit}")
    public ResponseEntity<Void> asignarServicioAIPS(
            @PathVariable Long servicioId, 
            @PathVariable Long ipsNit) {
        logger.debug("POST /api/servicios/{}/ips/{}", servicioId, ipsNit);
        try {
            servicioService.asignarServicioAIPS(servicioId, ipsNit);
            logger.info("Servicio {} asignado a IPS {}", servicioId, ipsNit);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error al asignar servicio a IPS: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todos los servicios
    @GetMapping
    public ResponseEntity<List<ServicioDeSalud>> obtenerTodosServicios() {
        List<ServicioDeSalud> servicios = servicioService.obtenerTodosServicios();
        return new ResponseEntity<>(servicios, HttpStatus.OK);
    }

    // Obtener servicio por ID
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDeSalud> obtenerServicioPorId(@PathVariable Long id) {
        try {
            return servicioService.obtenerServicioPorId(id)
                    .map(servicio -> new ResponseEntity<>(servicio, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener servicios por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ServicioDeSalud>> obtenerServiciosPorTipo(@PathVariable String tipo) {
        List<ServicioDeSalud> servicios = servicioService.obtenerServiciosPorTipo(tipo);
        return new ResponseEntity<>(servicios, HttpStatus.OK);
    }

    // Actualizar servicio
    @PutMapping("/{id}")
    public ResponseEntity<ServicioDeSalud> actualizarServicio(
            @PathVariable Long id, 
            @RequestBody ServicioDeSalud servicio) {
        try {
            if (!servicioService.obtenerServicioPorId(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ServicioDeSalud servicioActualizado = servicioService.actualizarServicio(id, servicio);
            return new ResponseEntity<>(servicioActualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar servicio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        try {
            servicioService.eliminarServicio(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // RFC2 - Obtener los 20 servicios más solicitados
    @GetMapping("/mas-solicitados")
    public ResponseEntity<?> obtenerServiciosMasSolicitados(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            List<ServicioDeSalud> servicios = servicioService.obtenerServiciosMasSolicitados(fechaInicio, fechaFin);
            
            // Create a simplified response
            List<Map<String, Object>> response = servicios.stream()
                .map(servicio -> {
                    Map<String, Object> servicioMap = new HashMap<>();
                    servicioMap.put("idServicio", servicio.getIdServicio());
                    servicioMap.put("nombre", servicio.getNombre());
                    servicioMap.put("descripcion", servicio.getDescripcion());
                    servicioMap.put("tipo", servicio.getTipo());
                    return servicioMap;
                })
                .collect(Collectors.toList());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                Collections.singletonMap("error", e.getMessage()), 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}