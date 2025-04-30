package uniandes.edu.co.epsandes.controller;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uniandes.edu.co.epsandes.modelo.AgendarCita;
import uniandes.edu.co.epsandes.modelo.PrestacionServicio;
import uniandes.edu.co.epsandes.servicio.AgendarCitaService;

@RestController
@RequestMapping("/api/citas")
public class AgendarCitaController {

    private static final Logger logger = LoggerFactory.getLogger(AgendarCitaController.class);

    @Autowired
    private AgendarCitaService citaService;

    // RF7.1 - Consultar la agenda de disponibilidad de un servicio
    @GetMapping("/disponibilidad/{servicioId}")
    public ResponseEntity<List<Object[]>> consultarDisponibilidadServicio(@PathVariable Long servicioId) {
        List<Object[]> disponibilidad = citaService.consultarDisponibilidadServicio(servicioId);
        return new ResponseEntity<>(disponibilidad, HttpStatus.OK);
    }

    // RF7.2 - Agendar un servicio de salud
    @PostMapping("/general")
    public ResponseEntity<?> agendarServicioGeneral(@RequestBody AgendarCita cita) {
        logger.debug("POST /api/citas/general - Body: {}", cita);
        try {
            AgendarCita nuevaCita = citaService.agendarServicio(cita);
            
            // Create a simplified response
            Map<String, Object> response = new HashMap<>();
            response.put("idCita", nuevaCita.getIdCita());
            response.put("fechaHora", nuevaCita.getFechaHora());
            response.put("afiliadoId", nuevaCita.getAfiliado().getNumeroDocumento());
            response.put("medicoId", nuevaCita.getMedico().getNumeroDocumento());
            response.put("servicioId", nuevaCita.getServicioDeSalud().getIdServicio());
            if (nuevaCita.getOrdenDeServicio() != null) {
                response.put("ordenId", nuevaCita.getOrdenDeServicio().getIdOrden());
            }
            
            logger.info("Cita agendada: {}", nuevaCita);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al agendar cita: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // RF8 - Registrar la prestación de un servicio de salud
    @PostMapping("/{citaId}/prestacion")
    public ResponseEntity<PrestacionServicio> registrarPrestacionServicio(
            @PathVariable Long citaId,
            @RequestParam Long ipsNit) {
        try {
            if (!citaService.obtenerCitaPorId(citaId).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            PrestacionServicio prestacion = citaService.registrarPrestacionServicio(citaId, ipsNit);
            return new ResponseEntity<>(prestacion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //RF9 - Agendar un servicio de salud por afiliado- RF7 transaccional
    @PostMapping("/agendar")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<?> agendarServicioPorAfiliado(@RequestBody AgendarCita cita) {
        logger.debug("POST /api/citas/agendar - Body: {}", cita);
        try {
            AgendarCita nuevaCita = citaService.agendarServicioPorAfiliado(cita);

            // Create a simplified response
            Map<String, Object> response = new HashMap<>();
            response.put("idCita", nuevaCita.getIdCita());
            response.put("fechaHora", nuevaCita.getFechaHora());
            response.put("afiliadoId", nuevaCita.getAfiliado().getNumeroDocumento());
            response.put("medicoId", nuevaCita.getMedico().getNumeroDocumento());
            response.put("servicioId", nuevaCita.getServicioDeSalud().getIdServicio());
            if (nuevaCita.getOrdenDeServicio() != null) {
                response.put("ordenId", nuevaCita.getOrdenDeServicio().getIdOrden());
            }

            logger.info("Cita agendada exitosamente: {}", nuevaCita);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al agendar cita: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // Obtener todas las citas
    @GetMapping
    public ResponseEntity<List<AgendarCita>> obtenerTodasCitas() {
        List<AgendarCita> citas = citaService.obtenerTodasCitas();
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    // Obtener cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<AgendarCita> obtenerCitaPorId(@PathVariable Long id) {
        try {
            return citaService.obtenerCitaPorId(id)
                    .map(cita -> new ResponseEntity<>(cita, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener citas por afiliado
    @GetMapping("/afiliado/{numeroDocumento}")
    public ResponseEntity<List<AgendarCita>> obtenerCitasPorAfiliado(@PathVariable Long numeroDocumento) {
        List<AgendarCita> citas = citaService.obtenerCitasPorAfiliado(numeroDocumento);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    // Obtener citas por médico
    @GetMapping("/medico/{numeroDocumento}")
    public ResponseEntity<List<AgendarCita>> obtenerCitasPorMedico(@PathVariable Long numeroDocumento) {
        List<AgendarCita> citas = citaService.obtenerCitasPorMedico(numeroDocumento);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    // Eliminar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        try {
            citaService.eliminarCita(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Consultar disponibilidad de servicio con aislamiento SERIALIZABLE
    @GetMapping("/disponibilidad/serializable")
    public ResponseEntity<List<Object[]>> consultarDisponibilidadServicioSerializable(
            @RequestParam Long servicioId,
            @RequestParam Long medicoId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            List<Object[]> disponibilidad = citaService.DisponibilidadServicio(servicioId, medicoId, fechaInicio, fechaFin);
            return new ResponseEntity<>(disponibilidad, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al consultar disponibilidad (Serializable): {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Consultar disponibilidad de servicio con aislamiento READ_COMMITTED
    @GetMapping("/disponibilidad/read-committed")
    public ResponseEntity<List<Object[]>> consultarDisponibilidadServicioReadCommitted(
            @RequestParam Long servicioId,
            @RequestParam Long medicoId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            List<Object[]> disponibilidad = citaService.DisponibilidadServicioRead(servicioId, medicoId, fechaInicio, fechaFin);
            return new ResponseEntity<>(disponibilidad, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al consultar disponibilidad (Read Committed): {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}