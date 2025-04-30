package uniandes.edu.co.epsandes.servicio;

import uniandes.edu.co.epsandes.modelo.Afiliado;
import uniandes.edu.co.epsandes.repositorio.AfiliadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class AfiliadoService {

    private static final Logger logger = LoggerFactory.getLogger(AfiliadoService.class);

    @Autowired
    private AfiliadoRepository afiliadoRepository;

    // RF5 - Registrar afiliado
    @Transactional
    public Afiliado registrarAfiliado(Afiliado afiliado) {
        logger.debug("Intentando registrar Afiliado: {}", afiliado);
        
        // Verificar si ya existe un afiliado con el mismo número de documento
        if (afiliadoRepository.existsById(afiliado.getNumeroDocumento())) {
            logger.warn("Ya existe un afiliado con el número de documento: {}", afiliado.getNumeroDocumento());
            throw new RuntimeException("Ya existe un afiliado con el número de documento: " + afiliado.getNumeroDocumento());
        }
        
        // Validar tipo de afiliado
        if (!"CONTRIBUYENTE".equals(afiliado.getTipoAfiliado()) && 
            !"BENEFICIARIO".equals(afiliado.getTipoAfiliado())) {
            logger.warn("Tipo de afiliado no válido: {}", afiliado.getTipoAfiliado());
            throw new RuntimeException("Tipo de afiliado no válido. Debe ser CONTRIBUYENTE o BENEFICIARIO");
        }
        
        // Validaciones específicas para beneficiarios
        if ("BENEFICIARIO".equals(afiliado.getTipoAfiliado())) {
            // Verificar que se proporcione un contribuyente
            if (afiliado.getNumeroDocumentoContribuyente() == null) {
                logger.warn("Un beneficiario debe tener un contribuyente asociado");
                throw new RuntimeException("Un beneficiario debe tener un contribuyente asociado");
            }
            
            // Verificar que el contribuyente existe y es de tipo CONTRIBUYENTE
            Afiliado contribuyente = afiliadoRepository.findById(afiliado.getNumeroDocumentoContribuyente())
                    .orElseThrow(() -> {
                        logger.warn("Contribuyente no encontrado con documento: {}", afiliado.getNumeroDocumentoContribuyente());
                        return new RuntimeException("Contribuyente no encontrado con documento: " + afiliado.getNumeroDocumentoContribuyente());
                    });
            
            if (!"CONTRIBUYENTE".equals(contribuyente.getTipoAfiliado())) {
                logger.warn("El afiliado referenciado no es un contribuyente");
                throw new RuntimeException("El afiliado referenciado no es un contribuyente");
            }
            
            // Verificar que se proporcione un parentesco
            if (afiliado.getParentesco() == null || afiliado.getParentesco().trim().isEmpty()) {
                logger.warn("Un beneficiario debe tener un parentesco definido");
                throw new RuntimeException("Un beneficiario debe tener un parentesco definido");
            }
            
            // Establecer referencia al contribuyente
            afiliado.setContribuyente(contribuyente);
        } else {
            // Si es contribuyente, asegurarse de que no tenga un contribuyente asociado
            afiliado.setNumeroDocumentoContribuyente(null);
            afiliado.setParentesco(null);
        }
        
        // Validar el tipo de documento
        if (afiliado.getTipoDocumento() == null || 
            (!afiliado.getTipoDocumento().equals("CC") && 
             !afiliado.getTipoDocumento().equals("TI") && 
             !afiliado.getTipoDocumento().equals("CE") && 
             !afiliado.getTipoDocumento().equals("PA"))) {
            logger.warn("Tipo de documento no válido: {}", afiliado.getTipoDocumento());
            throw new RuntimeException("Tipo de documento no válido. Debe ser CC, TI, CE o PA");
        }
        
        // Guardar el afiliado
        Afiliado saved = afiliadoRepository.save(afiliado);
        logger.info("Afiliado registrado exitosamente: {}", saved);
        return saved;
    }

    // Obtener todos los afiliados
    public List<Afiliado> obtenerTodosAfiliados() {
        logger.debug("Obteniendo todos los afiliados");
        return afiliadoRepository.findAll();
    }

    // Obtener afiliado por número de documento
    public Optional<Afiliado> obtenerAfiliadoPorDocumento(Long numeroDocumento) {
        logger.debug("Buscando afiliado con número de documento: {}", numeroDocumento);
        return afiliadoRepository.findById(numeroDocumento);
    }

    // Obtener afiliados por tipo
    public List<Afiliado> obtenerAfiliadosPorTipo(String tipoAfiliado) {
        logger.debug("Buscando afiliados de tipo: {}", tipoAfiliado);
        return afiliadoRepository.findByTipoAfiliado(tipoAfiliado);
    }

    // Obtener beneficiarios de un contribuyente
    public List<Afiliado> obtenerBeneficiarios(Long numeroDocumentoContribuyente) {
        logger.debug("Buscando beneficiarios del contribuyente con documento: {}", numeroDocumentoContribuyente);
        
        // Verificar primero que el contribuyente existe
        Afiliado contribuyente = afiliadoRepository.findById(numeroDocumentoContribuyente)
                .orElseThrow(() -> {
                    logger.warn("Contribuyente no encontrado con documento: {}", numeroDocumentoContribuyente);
                    return new RuntimeException("Contribuyente no encontrado con documento: " + numeroDocumentoContribuyente);
                });
        
        // Verificar que es realmente un contribuyente
        if (!"CONTRIBUYENTE".equals(contribuyente.getTipoAfiliado())) {
            logger.warn("El afiliado con documento {} no es un contribuyente", numeroDocumentoContribuyente);
            throw new RuntimeException("El afiliado con documento " + numeroDocumentoContribuyente + " no es un contribuyente");
        }
        
        return afiliadoRepository.findByNumeroDocumentoContribuyente(numeroDocumentoContribuyente);
    }

    // Actualizar afiliado
    @Transactional
    public Afiliado actualizarAfiliado(Long numeroDocumento, Afiliado afiliadoActualizado) {
        logger.debug("Intentando actualizar afiliado con documento: {}", numeroDocumento);
        
        // Buscar el afiliado existente
        Afiliado afiliado = afiliadoRepository.findById(numeroDocumento)
                .orElseThrow(() -> {
                    logger.warn("Afiliado no encontrado con documento: {}", numeroDocumento);
                    return new RuntimeException("Afiliado no encontrado con documento: " + numeroDocumento);
                });
        
        // No permitir cambiar el tipo de afiliado ni el documento del contribuyente
        if (!afiliado.getTipoAfiliado().equals(afiliadoActualizado.getTipoAfiliado())) {
            logger.warn("No se permite cambiar el tipo de afiliado");
            throw new RuntimeException("No se permite cambiar el tipo de afiliado");
        }
        
        if ("BENEFICIARIO".equals(afiliado.getTipoAfiliado()) && 
            !afiliado.getNumeroDocumentoContribuyente().equals(afiliadoActualizado.getNumeroDocumentoContribuyente())) {
            logger.warn("No se permite cambiar el contribuyente asociado");
            throw new RuntimeException("No se permite cambiar el contribuyente asociado");
        }
        
        // Actualizar campos permitidos
        afiliado.setNombre(afiliadoActualizado.getNombre());
        afiliado.setDireccion(afiliadoActualizado.getDireccion());
        afiliado.setTelefono(afiliadoActualizado.getTelefono());
        
        // Solo actualizar la fecha de nacimiento si se proporciona
        if (afiliadoActualizado.getFechaNacimiento() != null) {
            afiliado.setFechaNacimiento(afiliadoActualizado.getFechaNacimiento());
        }
        
        // Para beneficiarios, permitir actualizar el parentesco
        if ("BENEFICIARIO".equals(afiliado.getTipoAfiliado()) && 
            afiliadoActualizado.getParentesco() != null && 
            !afiliadoActualizado.getParentesco().trim().isEmpty()) {
            afiliado.setParentesco(afiliadoActualizado.getParentesco());
        }

        // Guardar los cambios
        Afiliado updated = afiliadoRepository.save(afiliado);
        logger.info("Afiliado actualizado exitosamente: {}", updated);
        return updated;
    }

    // Eliminar afiliado
    @Transactional
    public void eliminarAfiliado(Long numeroDocumento) {
        logger.debug("Intentando eliminar afiliado con documento: {}", numeroDocumento);
        
        // Verificar que el afiliado existe
        Afiliado afiliado = afiliadoRepository.findById(numeroDocumento)
                .orElseThrow(() -> {
                    logger.warn("Afiliado no encontrado con documento: {}", numeroDocumento);
                    return new RuntimeException("Afiliado no encontrado con documento: " + numeroDocumento);
                });

        // Si es contribuyente, verificar que no tenga beneficiarios
        if ("CONTRIBUYENTE".equals(afiliado.getTipoAfiliado())) {
            boolean tieneBeneficiarios = afiliadoRepository.tieneBeneficiarios(numeroDocumento);
            if (tieneBeneficiarios) {
                logger.warn("No se puede eliminar un contribuyente que tiene beneficiarios asociados");
                throw new RuntimeException("No se puede eliminar un contribuyente que tiene beneficiarios asociados");
            }
        }
        
        // Verificar que no tenga citas u órdenes asociadas
        if (afiliado.getCitas() != null && !afiliado.getCitas().isEmpty()) {
            logger.warn("No se puede eliminar un afiliado que tiene citas asociadas");
            throw new RuntimeException("No se puede eliminar un afiliado que tiene citas asociadas");
        }
        
        if (afiliado.getOrdenes() != null && !afiliado.getOrdenes().isEmpty()) {
            logger.warn("No se puede eliminar un afiliado que tiene órdenes de servicio asociadas");
            throw new RuntimeException("No se puede eliminar un afiliado que tiene órdenes de servicio asociadas");
        }

        // Eliminar el afiliado
        afiliadoRepository.deleteById(numeroDocumento);
        logger.info("Afiliado eliminado exitosamente con documento: {}", numeroDocumento);
    }
}