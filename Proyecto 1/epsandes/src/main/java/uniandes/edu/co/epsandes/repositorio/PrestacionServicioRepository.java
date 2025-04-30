package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.PrestacionServicio;
import uniandes.edu.co.epsandes.modelo.PrestacionServicioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestacionServicioRepository extends JpaRepository<PrestacionServicio, PrestacionServicioId> {
    
    // Buscar prestaciones por IPS
    List<PrestacionServicio> findByIpsNit(Long nit);
    
    // Buscar prestaciones por estado de realización
    List<PrestacionServicio> findByCitaRealizada(Boolean citaRealizada);
    
    // Verificar si existe una prestación para una cita específica
    boolean existsByAgendarCitaIdCita(Long idCita);
    
    // Obtener prestaciones realizadas en un período de tiempo
    @Query("SELECT p FROM PrestacionServicio p JOIN p.agendarCita ac " +
           "WHERE ac.fechaHora BETWEEN TO_TIMESTAMP(:fechaInicio, 'YYYY-MM-DD') " +
           "AND TO_TIMESTAMP(:fechaFin, 'YYYY-MM-DD') " +
           "AND p.citaRealizada = true")
    List<PrestacionServicio> findPrestacionesRealizadasEnPeriodo(
            @Param("fechaInicio") String fechaInicio, 
            @Param("fechaFin") String fechaFin);
}