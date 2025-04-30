package uniandes.edu.co.epsandes.servicio;
import uniandes.edu.co.epsandes.modelo.IPS;
import uniandes.edu.co.epsandes.modelo.ServicioDeSalud;
import uniandes.edu.co.epsandes.repositorio.IPSRepository;
import uniandes.edu.co.epsandes.modelo.Medico;
import uniandes.edu.co.epsandes.repositorio.MedicoRepository;
import uniandes.edu.co.epsandes.repositorio.ServicioDeSaludRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;

@Service
public class MedicoService {

    private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private IPSRepository ipsRepository;

    @Autowired
    private ServicioDeSaludRepository servicioRepository;

    // RF4 - Registrar médico
    @Transactional
    public Medico registrarMedico(Medico medico, List<Long> serviciosIds) {
        logger.debug("Intentando registrar Medico: {} con servicios: {}", medico, serviciosIds);
        
        // Verificar si ya existe un médico con el mismo número de documento
        if (medicoRepository.existsById(medico.getNumeroDocumento())) {
            logger.warn("Ya existe un médico con el número de documento: {}", medico.getNumeroDocumento());
            throw new RuntimeException("Ya existe un médico con el número de documento: " + medico.getNumeroDocumento());
        }

        // Verificar si ya existe un médico con el mismo número de registro médico
        Optional<Medico> medicoExistente = medicoRepository.findByNumeroRegistroMedico(medico.getNumeroRegistroMedico());
        if (medicoExistente.isPresent()) {
            logger.warn("Ya existe un médico con el número de registro médico: {}", medico.getNumeroRegistroMedico());
            throw new RuntimeException("Ya existe un médico con el número de registro médico: " + medico.getNumeroRegistroMedico());
        }

        // Verificar que la IPS existe
        if (medico.getIps() == null) {
            logger.warn("No se proporcionó información de IPS para el médico");
            throw new RuntimeException("Debe proporcionar la IPS a la que pertenece el médico");
        }
        
        IPS ips = ipsRepository.findById(medico.getIps().getNit())
                .orElseThrow(() -> {
                    logger.warn("IPS no encontrada con NIT: {}", medico.getIps().getNit());
                    return new RuntimeException("IPS no encontrada con NIT: " + medico.getIps().getNit());
                });

        medico.setIps(ips);
        
        // Inicializar el conjunto de servicios si es null
        if (medico.getServicios() == null) {
            medico.setServicios(new HashSet<>());
        }
        
        // Guardar el médico primero
        Medico saved = medicoRepository.save(medico);
        
        // Procesar los servicios si se proporcionaron
        if (serviciosIds != null && !serviciosIds.isEmpty()) {
            for (Long servicioId : serviciosIds) {
                if (servicioId != null) { // Extra null check
                    ServicioDeSalud servicio = servicioRepository.findById(servicioId)
                        .orElseThrow(() -> {
                            logger.warn("Servicio no encontrado con ID: {}", servicioId);
                            return new RuntimeException("Servicio no encontrado con ID: " + servicioId);
                        });
                    saved.getServicios().add(servicio);
                    servicio.getMedicos().add(saved);
                    servicioRepository.save(servicio);
                }
            }
            // Guardar el médico nuevamente con los servicios actualizados
            saved = medicoRepository.save(saved);
        }
        
        logger.info("Médico registrado exitosamente con sus servicios: {}", saved);
        return saved;
    }

    // Asignar servicio a médico
    @Transactional
    public void asignarServicioAMedico(Long medicoDocumento, Long servicioId) {
        logger.debug("Asignando servicio {} a médico {}", servicioId, medicoDocumento);
        
        Medico medico = medicoRepository.findById(medicoDocumento)
                .orElseThrow(() -> {
                    logger.warn("Médico no encontrado con documento: {}", medicoDocumento);
                    return new RuntimeException("Médico no encontrado con documento: " + medicoDocumento);
                });

        ServicioDeSalud servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> {
                    logger.warn("Servicio no encontrado con ID: {}", servicioId);
                    return new RuntimeException("Servicio no encontrado con ID: " + servicioId);
                });

        // Verificar si ya tiene asignado el servicio
        if (medico.getServicios().contains(servicio)) {
            logger.warn("El médico ya tiene asignado este servicio");
            throw new RuntimeException("El médico ya tiene asignado este servicio");
        }

        medico.getServicios().add(servicio);
        servicio.getMedicos().add(medico);

        medicoRepository.save(medico);
        servicioRepository.save(servicio);
        logger.info("Servicio {} asignado a médico {}", servicioId, medicoDocumento);
    }

    // Obtener todos los médicos
    public List<Medico> obtenerTodosMedicos() {
        return medicoRepository.findAll();
    }

    // Obtener médico por número de documento
    public Optional<Medico> obtenerMedicoPorDocumento(Long numeroDocumento) {
        return medicoRepository.findById(numeroDocumento);
    }

    // Obtener médico por número de registro médico
    public Optional<Medico> obtenerMedicoPorRegistroMedico(Long numeroRegistroMedico) {
        return medicoRepository.findByNumeroRegistroMedico(numeroRegistroMedico);
    }

    // Obtener médicos por especialidad
    public List<Medico> obtenerMedicosPorEspecialidad(String especialidad) {
        return medicoRepository.findByEspecialidad(especialidad);
    }

    // Obtener médicos por IPS
    public List<Medico> obtenerMedicosPorIPS(Long nit) {
        return medicoRepository.findByIpsNit(nit);
    }

    // Obtener médicos que prestan un servicio específico
    public List<Medico> obtenerMedicosPorServicio(Long servicioId) {
        return medicoRepository.findByServicioId(servicioId);
    }

    // Actualizar médico
    @Transactional
    public Medico actualizarMedico(Long numeroDocumento, Medico medicoActualizado) {
        Medico medico = medicoRepository.findById(numeroDocumento)
                .orElseThrow(() -> {
                    logger.warn("Médico no encontrado con documento: {}", numeroDocumento);
                    return new RuntimeException("Médico no encontrado con documento: " + numeroDocumento);
                });

        // Actualizar campos básicos
        medico.setNombre(medicoActualizado.getNombre());
        medico.setTipoDocumento(medicoActualizado.getTipoDocumento());
        medico.setEspecialidad(medicoActualizado.getEspecialidad());

        // Actualizar IPS si se proporciona una diferente
        if (medicoActualizado.getIps() != null && 
            medicoActualizado.getIps().getNit() != null && 
            !medico.getIps().getNit().equals(medicoActualizado.getIps().getNit())) {
            
            IPS nuevaIps = ipsRepository.findById(medicoActualizado.getIps().getNit())
                    .orElseThrow(() -> {
                        logger.warn("IPS no encontrada con NIT: {}", medicoActualizado.getIps().getNit());
                        return new RuntimeException("IPS no encontrada con NIT: " + medicoActualizado.getIps().getNit());
                    });
            medico.setIps(nuevaIps);
        }

        return medicoRepository.save(medico);
    }

    // Eliminar médico
    @Transactional
    public void eliminarMedico(Long numeroDocumento) {
        if (!medicoRepository.existsById(numeroDocumento)) {
            logger.warn("Médico no encontrado con documento: {}", numeroDocumento);
            throw new RuntimeException("Médico no encontrado con documento: " + numeroDocumento);
        }
        
        Medico medico = medicoRepository.findById(numeroDocumento).get();
        
        // Verificar si tiene citas u órdenes
        if (medico.getCitas() != null && !medico.getCitas().isEmpty()) {
            logger.warn("No se puede eliminar un médico que tiene citas asignadas");
            throw new RuntimeException("No se puede eliminar un médico que tiene citas asignadas");
        }
        
        if (medico.getOrdenes() != null && !medico.getOrdenes().isEmpty()) {
            logger.warn("No se puede eliminar un médico que ha generado órdenes de servicio");
            throw new RuntimeException("No se puede eliminar un médico que ha generado órdenes de servicio");
        }
        
        // Primero eliminar relaciones con servicios
        for (ServicioDeSalud servicio : medico.getServicios()) {
            servicio.getMedicos().remove(medico);
            servicioRepository.save(servicio);
        }
        medico.getServicios().clear();
        medicoRepository.save(medico);
        
        // Ahora sí eliminar el médico
        medicoRepository.deleteById(numeroDocumento);
        logger.info("Médico eliminado exitosamente con documento: {}", numeroDocumento);
    }
}