package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    
    // Buscar médico por número de registro médico
    Optional<Medico> findByNumeroRegistroMedico(Long numeroRegistroMedico);
    
    // Buscar médicos por especialidad
    List<Medico> findByEspecialidad(String especialidad);
    
    // Buscar médicos por IPS
    List<Medico> findByIpsNit(Long nit);
    
    // Buscar médicos que prestan un servicio específico
    @Query("SELECT m FROM Medico m JOIN m.servicios s WHERE s.idServicio = :servicioId")
    List<Medico> findByServicioId(@Param("servicioId") Long servicioId);
    
    // Buscar médicos que prestan un servicio específico en una IPS específica
    @Query("SELECT m FROM Medico m JOIN m.servicios s WHERE s.idServicio = :servicioId AND m.ips.nit = :nit")
    List<Medico> findByServicioIdAndIpsNit(@Param("servicioId") Long servicioId, @Param("nit") Long nit);
}