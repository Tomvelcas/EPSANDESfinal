package uniandes.edu.co.epsandes.controller;

import uniandes.edu.co.epsandes.modelo.OrdenDeServicio;
import uniandes.edu.co.epsandes.servicio.OrdenDeServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenDeServicioController {

    private static final Logger logger = LoggerFactory.getLogger(OrdenDeServicioController.class);

    @Autowired
    private OrdenDeServicioService ordenService;

    // RF6 - Registrar una orden de servicio
    @PostMapping
    public ResponseEntity<?> registrarOrden(@RequestBody OrdenDeServicio orden) {
        logger.debug("POST /api/ordenes - Body: {}", orden);
        try {
            OrdenDeServicio nuevaOrden = ordenService.registrarOrden(orden);
            // Create a simplified response
            Map<String, Object> response = new HashMap<>();
            response.put("idOrden", nuevaOrden.getIdOrden());
            response.put("estadoOrden", nuevaOrden.getEstadoOrden());
            response.put("fechaHora", nuevaOrden.getFechaHora());
            response.put("medicoId", nuevaOrden.getMedico().getNumeroDocumento());
            response.put("afiliadoId", nuevaOrden.getAfiliado().getNumeroDocumento());
            
            logger.info("Orden de servicio creada: {}", nuevaOrden);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al registrar orden de servicio: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // Asignar servicios a una orden
    @PostMapping("/{ordenId}/servicios")
    public ResponseEntity<Void> asignarServiciosAOrden(
            @PathVariable Long ordenId,
            @RequestBody List<Long> serviciosIds) {
        try {
            ordenService.asignarServiciosAOrden(ordenId, serviciosIds);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todas las órdenes
    @GetMapping
    public ResponseEntity<List<OrdenDeServicio>> obtenerTodasOrdenes() {
        List<OrdenDeServicio> ordenes = ordenService.obtenerTodasOrdenes();
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    // Obtener orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrdenDeServicio> obtenerOrdenPorId(@PathVariable Long id) {
        try {
            return ordenService.obtenerOrdenPorId(id)
                    .map(orden -> new ResponseEntity<>(orden, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener órdenes por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenDeServicio>> obtenerOrdenesPorEstado(@PathVariable String estado) {
        List<OrdenDeServicio> ordenes = ordenService.obtenerOrdenesPorEstado(estado);
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    // Obtener órdenes por afiliado
    @GetMapping("/afiliado/{numeroDocumento}")
    public ResponseEntity<List<OrdenDeServicio>> obtenerOrdenesPorAfiliado(@PathVariable Long numeroDocumento) {
        List<OrdenDeServicio> ordenes = ordenService.obtenerOrdenesPorAfiliado(numeroDocumento);
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    // Obtener órdenes vigentes de un afiliado
    @GetMapping("/afiliado/{numeroDocumento}/vigentes")
    public ResponseEntity<List<OrdenDeServicio>> obtenerOrdenesVigentesPorAfiliado(@PathVariable Long numeroDocumento) {
        List<OrdenDeServicio> ordenes = ordenService.obtenerOrdenesVigentesPorAfiliado(numeroDocumento);
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    // Actualizar estado de la orden
    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenDeServicio> actualizarEstadoOrden(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        try {
            if (!ordenService.obtenerOrdenPorId(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            OrdenDeServicio ordenActualizada = ordenService.actualizarEstadoOrden(id, nuevoEstado);
            return new ResponseEntity<>(ordenActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cancelar orden
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<OrdenDeServicio> cancelarOrden(@PathVariable Long id) {
        try {
            if (!ordenService.obtenerOrdenPorId(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            OrdenDeServicio ordenCancelada = ordenService.cancelarOrden(id);
            return new ResponseEntity<>(ordenCancelada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Completar orden
    @PutMapping("/{id}/completar")
    public ResponseEntity<OrdenDeServicio> completarOrden(@PathVariable Long id) {
        try {
            if (!ordenService.obtenerOrdenPorId(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            OrdenDeServicio ordenCompletada = ordenService.completarOrden(id);
            return new ResponseEntity<>(ordenCompletada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar orden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        try {
            ordenService.eliminarOrden(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}