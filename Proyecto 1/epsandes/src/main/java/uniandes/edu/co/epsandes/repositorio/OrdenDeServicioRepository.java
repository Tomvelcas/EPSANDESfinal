package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.OrdenDeServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdenDeServicioRepository extends JpaRepository<OrdenDeServicio, Long> {
    
    // Buscar órdenes por estado
    List<OrdenDeServicio> findByEstadoOrden(String estadoOrden);
    
    // Buscar órdenes por afiliado
    List<OrdenDeServicio> findByAfiliadoNumeroDocumento(Long numeroDocumento);
    
    // Buscar órdenes por médico
    List<OrdenDeServicio> findByMedicoNumeroDocumento(Long numeroDocumento);
    
    // Buscar órdenes en un rango de fechas
    List<OrdenDeServicio> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Buscar órdenes vigentes de un afiliado
    @Query("SELECT o FROM OrdenDeServicio o WHERE o.afiliado.numeroDocumento = :numeroDocumento " +
           "AND o.estadoOrden = 'VIGENTE'")
    List<OrdenDeServicio> findOrdenesVigentesByAfiliado(@Param("numeroDocumento") Long numeroDocumento);
}