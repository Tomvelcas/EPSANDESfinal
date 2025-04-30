package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.IPS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPSRepository extends JpaRepository<IPS, Long> {
    
    // Buscar IPS por nombre
    Optional<IPS> findByNombre(String nombre);
    
    // Buscar IPS que ofrecen un servicio específico
    @Query("SELECT i FROM IPS i JOIN i.servicios s WHERE s.idServicio = :servicioId")
    List<IPS> findByServicioId(@Param("servicioId") Long servicioId);
    
    // Buscar IPS por dirección
    List<IPS> findByDireccionContaining(String direccion);
    
    // Verificar si existe una IPS con un NIT específico
    boolean existsByNit(Long nit);
}