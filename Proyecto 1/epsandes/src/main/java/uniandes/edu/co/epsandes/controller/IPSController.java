package uniandes.edu.co.epsandes.controller;

import uniandes.edu.co.epsandes.modelo.IPS;
import uniandes.edu.co.epsandes.servicio.IPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ips")
public class IPSController {

    private static final Logger logger = LoggerFactory.getLogger(IPSController.class);

    @Autowired
    private IPSService ipsService;

    // RF1 - Registrar IPS
    @PostMapping
    public ResponseEntity<IPS> registrarIPS(@RequestBody IPS ips) {
        logger.debug("POST /api/ips - Body: {}", ips);
        try {
            IPS nuevaIPS = ipsService.registrarIPS(ips);
            logger.info("IPS creada: {}", nuevaIPS);
            return new ResponseEntity<>(nuevaIPS, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al registrar IPS: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener todas las IPS
    @GetMapping
    public ResponseEntity<List<IPS>> obtenerTodasIPS() {
        List<IPS> ipsList = ipsService.obtenerTodasIPS();
        return new ResponseEntity<>(ipsList, HttpStatus.OK);
    }

    // Obtener IPS por NIT
    @GetMapping("/{nit}")
    public ResponseEntity<IPS> obtenerIPSPorNit(@PathVariable Long nit) {
        try {
            return ipsService.obtenerIPSPorNit(nit)
                    .map(ips -> new ResponseEntity<>(ips, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener IPS por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<IPS> obtenerIPSPorNombre(@PathVariable String nombre) {
        return ipsService.obtenerIPSPorNombre(nombre)
                .map(ips -> new ResponseEntity<>(ips, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Actualizar IPS
    @PutMapping("/{nit}")
    public ResponseEntity<IPS> actualizarIPS(@PathVariable Long nit, @RequestBody IPS ips) {
        try {
            if (!ipsService.obtenerIPSPorNit(nit).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            IPS ipsActualizada = ipsService.actualizarIPS(nit, ips);
            return new ResponseEntity<>(ipsActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar IPS
    @DeleteMapping("/{nit}")
    public ResponseEntity<Void> eliminarIPS(@PathVariable Long nit) {
        try {
            ipsService.eliminarIPS(nit);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener IPS que prestan un servicio específico
    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<List<IPS>> obtenerIPSPorServicio(@PathVariable Long servicioId) {
        List<IPS> ipsList = ipsService.obtenerIPSPorServicio(servicioId);
        return new ResponseEntity<>(ipsList, HttpStatus.OK);
    }
}