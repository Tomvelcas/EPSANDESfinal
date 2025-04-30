package uniandes.edu.co.epsandes.servicio;

import uniandes.edu.co.epsandes.modelo.Afiliado;
import uniandes.edu.co.epsandes.modelo.Medico;
import uniandes.edu.co.epsandes.modelo.OrdenDeServicio;
import uniandes.edu.co.epsandes.modelo.ServicioDeSalud;
import uniandes.edu.co.epsandes.repositorio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrdenDeServicioService {

    private static final Logger logger = LoggerFactory.getLogger(OrdenDeServicioService.class);

    @Autowired
    private OrdenDeServicioRepository ordenRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private AfiliadoRepository afiliadoRepository;

    @Autowired
    private ServicioDeSaludRepository servicioRepository;

    // RF6 - Registrar una orden de servicio
    @Transactional
    public OrdenDeServicio registrarOrden(OrdenDeServicio orden) {
        logger.debug("Intentando registrar OrdenDeServicio: {}", orden);
        // Verificar si ya existe una orden con el mismo ID
        if (ordenRepository.existsById(orden.getIdOrden())) {
            logger.warn("Ya existe una orden con el ID: {}", orden.getIdOrden());
            throw new RuntimeException("Ya existe una orden con el ID: " + orden.getIdOrden());
        }

        // Verificar que el médico existe
        Medico medico = medicoRepository.findById(orden.getMedico().getNumeroDocumento())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con documento: " + 
                        orden.getMedico().getNumeroDocumento()));

        // Verificar que el afiliado existe
        Afiliado afiliado = afiliadoRepository.findById(orden.getAfiliado().getNumeroDocumento())
                .orElseThrow(() -> new RuntimeException("Afiliado no encontrado con documento: " + 
                        orden.getAfiliado().getNumeroDocumento()));

        orden.setMedico(medico);
        orden.setAfiliado(afiliado);
        orden.setFechaHora(LocalDateTime.now());
        orden.setEstadoOrden("VIGENTE");

        OrdenDeServicio saved = ordenRepository.save(orden);
        logger.info("OrdenDeServicio registrada exitosamente: {}", saved);
        return saved;
    }

    // Asignar servicios a una orden
    @Transactional
    public void asignarServiciosAOrden(Long ordenId, List<Long> serviciosIds) {
        OrdenDeServicio orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + ordenId));

        for (Long servicioId : serviciosIds) {
            ServicioDeSalud servicio = servicioRepository.findById(servicioId)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + servicioId));

            orden.getServicios().add(servicio);
            servicio.getOrdenes().add(orden);
        }

        ordenRepository.save(orden);
    }

    // Obtener todas las órdenes
    public List<OrdenDeServicio> obtenerTodasOrdenes() {
        return ordenRepository.findAll();
    }

    // Obtener orden por ID
    public Optional<OrdenDeServicio> obtenerOrdenPorId(Long id) {
        return ordenRepository.findById(id);
    }

    // Obtener órdenes por estado
    public List<OrdenDeServicio> obtenerOrdenesPorEstado(String estado) {
        return ordenRepository.findByEstadoOrden(estado);
    }

    // Obtener órdenes por afiliado
    public List<OrdenDeServicio> obtenerOrdenesPorAfiliado(Long numeroDocumento) {
        return ordenRepository.findByAfiliadoNumeroDocumento(numeroDocumento);
    }

    // Obtener órdenes vigentes de un afiliado
    public List<OrdenDeServicio> obtenerOrdenesVigentesPorAfiliado(Long numeroDocumento) {
        return ordenRepository.findOrdenesVigentesByAfiliado(numeroDocumento);
    }

    // Actualizar estado de la orden
    @Transactional
    public OrdenDeServicio actualizarEstadoOrden(Long id, String nuevoEstado) {
        OrdenDeServicio orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));

        if (!nuevoEstado.equals("VIGENTE") && !nuevoEstado.equals("CANCELADA") && !nuevoEstado.equals("COMPLETADA")) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado);
        }

        orden.setEstadoOrden(nuevoEstado);
        return ordenRepository.save(orden);
    }

    // Cancelar orden
    @Transactional
    public OrdenDeServicio cancelarOrden(Long id) {
        return actualizarEstadoOrden(id, "CANCELADA");
    }

    // Completar orden
    @Transactional
    public OrdenDeServicio completarOrden(Long id) {
        return actualizarEstadoOrden(id, "COMPLETADA");
    }

    // Eliminar orden
    @Transactional
    public void eliminarOrden(Long id) {
        if (!ordenRepository.existsById(id)) {
            throw new RuntimeException("Orden no encontrada con ID: " + id);
        }
        ordenRepository.deleteById(id);
    }
}