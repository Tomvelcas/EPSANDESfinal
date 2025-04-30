package uniandes.edu.co.epsandes.servicio;
import uniandes.edu.co.epsandes.modelo.IPS;
import uniandes.edu.co.epsandes.modelo.ServicioDeSalud;
import uniandes.edu.co.epsandes.repositorio.IPSRepository;
import uniandes.edu.co.epsandes.repositorio.ServicioDeSaludRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioDeSaludService {

    private static final Logger logger = LoggerFactory.getLogger(ServicioDeSaludService.class);

    @Autowired
    private ServicioDeSaludRepository servicioRepository;

    @Autowired
    private IPSRepository ipsRepository;

    // RF2 - Registrar un servicio de salud
    @Transactional
    public ServicioDeSalud registrarServicio(ServicioDeSalud servicio) {
        logger.debug("Intentando registrar ServicioDeSalud: {}", servicio);
        if (servicioRepository.existsById(servicio.getIdServicio())) {
            logger.warn("Ya existe un servicio con el ID: {}", servicio.getIdServicio());
            throw new RuntimeException("Ya existe un servicio con el ID: " + servicio.getIdServicio());
        }
        ServicioDeSalud saved = servicioRepository.save(servicio);
        logger.info("ServicioDeSalud registrado exitosamente: {}", saved);
        return saved;
    }

    // RF3 - Asignar un servicio de salud a una IPS
    @Transactional
    public void asignarServicioAIPS(Long servicioId, Long ipsNit) {
        logger.debug("Asignando servicio {} a IPS {}", servicioId, ipsNit);
        ServicioDeSalud servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + servicioId));
        IPS ips = ipsRepository.findById(ipsNit)
                .orElseThrow(() -> new RuntimeException("IPS no encontrada con NIT: " + ipsNit));
        ips.getServicios().add(servicio);
        servicio.getIpsSet().add(ips);
        ipsRepository.save(ips);
        servicioRepository.save(servicio);
        logger.info("Servicio {} asignado a IPS {}", servicioId, ipsNit);
    }

    // Obtener todos los servicios
    public List<ServicioDeSalud> obtenerTodosServicios() {
        return servicioRepository.findAll();
    }

    // Obtener servicio por ID
    public Optional<ServicioDeSalud> obtenerServicioPorId(Long id) {
        return servicioRepository.findById(id);
    }

    // Obtener servicios por tipo
    public List<ServicioDeSalud> obtenerServiciosPorTipo(String tipo) {
        return servicioRepository.findByTipo(tipo);
    }

    // Obtener servicios por nombre
    public List<ServicioDeSalud> obtenerServiciosPorNombre(String nombre) {
        return servicioRepository.findByNombreContaining(nombre);
    }

    // Actualizar servicio
    @Transactional
    public ServicioDeSalud actualizarServicio(Long id, ServicioDeSalud servicioActualizado) {
        ServicioDeSalud servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        servicio.setNombre(servicioActualizado.getNombre());
        servicio.setDescripcion(servicioActualizado.getDescripcion());
        servicio.setTipo(servicioActualizado.getTipo());

        return servicioRepository.save(servicio);
    }

    // Eliminar servicio
    @Transactional
    public void eliminarServicio(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado con ID: " + id);
        }
        servicioRepository.deleteById(id);
    }

    // Obtener los 20 servicios más solicitados
    public List<ServicioDeSalud> obtenerServiciosMasSolicitados(String fechaInicio, String fechaFin) {
        return servicioRepository.findTop20ServiciosMasSolicitados(fechaInicio, fechaFin);
    }
}