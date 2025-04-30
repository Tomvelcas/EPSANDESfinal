package uniandes.edu.co.epsandes.controller;

import uniandes.edu.co.epsandes.modelo.Afiliado;
import uniandes.edu.co.epsandes.servicio.AfiliadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/afiliados")
public class AfiliadoController {

    private static final Logger logger = LoggerFactory.getLogger(AfiliadoController.class);

    @Autowired
    private AfiliadoService afiliadoService;

    // RF5 - Registrar afiliado
    @PostMapping
    public ResponseEntity<Afiliado> registrarAfiliado(@RequestBody Afiliado afiliado) {
        logger.debug("POST /api/afiliados - Body: {}", afiliado);
        try {
            Afiliado nuevoAfiliado = afiliadoService.registrarAfiliado(afiliado);
            logger.info("Afiliado creado: {}", nuevoAfiliado);
            return new ResponseEntity<>(nuevoAfiliado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al registrar afiliado: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener todos los afiliados
    @GetMapping
    public ResponseEntity<List<Afiliado>> obtenerTodosAfiliados() {
        List<Afiliado> afiliados = afiliadoService.obtenerTodosAfiliados();
        return new ResponseEntity<>(afiliados, HttpStatus.OK);
    }

    // Obtener afiliado por número de documento
    @GetMapping("/{numeroDocumento}")
    public ResponseEntity<Afiliado> obtenerAfiliadoPorDocumento(@PathVariable Long numeroDocumento) {
        try {
            return afiliadoService.obtenerAfiliadoPorDocumento(numeroDocumento)
                    .map(afiliado -> new ResponseEntity<>(afiliado, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener afiliados por tipo
    @GetMapping("/tipo/{tipoAfiliado}")
    public ResponseEntity<List<Afiliado>> obtenerAfiliadosPorTipo(@PathVariable String tipoAfiliado) {
        List<Afiliado> afiliados = afiliadoService.obtenerAfiliadosPorTipo(tipoAfiliado);
        return new ResponseEntity<>(afiliados, HttpStatus.OK);
    }

    // Obtener beneficiarios de un contribuyente
    @GetMapping("/contribuyente/{numeroDocumento}/beneficiarios")
    public ResponseEntity<List<Afiliado>> obtenerBeneficiarios(@PathVariable Long numeroDocumento) {
        List<Afiliado> beneficiarios = afiliadoService.obtenerBeneficiarios(numeroDocumento);
        return new ResponseEntity<>(beneficiarios, HttpStatus.OK);
    }

    // Actualizar afiliado
    @PutMapping("/{numeroDocumento}")
    public ResponseEntity<Afiliado> actualizarAfiliado(
            @PathVariable Long numeroDocumento,
            @RequestBody Afiliado afiliado) {
        try {
            if (!afiliadoService.obtenerAfiliadoPorDocumento(numeroDocumento).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Afiliado afiliadoActualizado = afiliadoService.actualizarAfiliado(numeroDocumento, afiliado);
            return new ResponseEntity<>(afiliadoActualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar afiliado
    @DeleteMapping("/{numeroDocumento}")
    public ResponseEntity<Void> eliminarAfiliado(@PathVariable Long numeroDocumento) {
        try {
            afiliadoService.eliminarAfiliado(numeroDocumento);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}