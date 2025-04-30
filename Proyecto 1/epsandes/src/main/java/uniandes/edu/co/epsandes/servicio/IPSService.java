package uniandes.edu.co.epsandes.servicio;
import uniandes.edu.co.epsandes.modelo.IPS;
import uniandes.edu.co.epsandes.repositorio.IPSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class IPSService {

    private static final Logger logger = LoggerFactory.getLogger(IPSService.class);

    @Autowired
    private IPSRepository ipsRepository;

    // RF1 - Registrar IPS
    @Transactional
    public IPS registrarIPS(IPS ips) {
        logger.debug("Intentando registrar IPS: {}", ips);
        // Verificar si ya existe una IPS con el mismo NIT
        if (ipsRepository.existsByNit(ips.getNit())) {
            logger.warn("Ya existe una IPS con el NIT: {}", ips.getNit());
            throw new RuntimeException("Ya existe una IPS con el NIT: " + ips.getNit());
        }
        IPS saved = ipsRepository.save(ips);
        logger.info("IPS registrada exitosamente: {}", saved);
        return saved;
    }

    // Obtener todas las IPS
    public List<IPS> obtenerTodasIPS() {
        return ipsRepository.findAll();
    }

    // Obtener IPS por NIT
    public Optional<IPS> obtenerIPSPorNit(Long nit) {
        return ipsRepository.findById(nit);
    }

    // Obtener IPS por nombre
    public Optional<IPS> obtenerIPSPorNombre(String nombre) {
        return ipsRepository.findByNombre(nombre);
    }

    // Actualizar IPS
    @Transactional
    public IPS actualizarIPS(Long nit, IPS ipsActualizada) {
        IPS ips = ipsRepository.findById(nit)
                .orElseThrow(() -> new RuntimeException("IPS no encontrada con NIT: " + nit));

        ips.setNombre(ipsActualizada.getNombre());
        ips.setDireccion(ipsActualizada.getDireccion());
        ips.setTelefono(ipsActualizada.getTelefono());

        return ipsRepository.save(ips);
    }

    // Eliminar IPS
    @Transactional
    public void eliminarIPS(Long nit) {
        if (!ipsRepository.existsById(nit)) {
            throw new RuntimeException("IPS no encontrada con NIT: " + nit);
        }
        ipsRepository.deleteById(nit);
    }

    // Obtener IPS que prestan un servicio específico
    public List<IPS> obtenerIPSPorServicio(Long servicioId) {
        return ipsRepository.findByServicioId(servicioId);
    }
}