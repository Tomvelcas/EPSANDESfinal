package uniandes.edu.co.epsandes.repositorio;
import uniandes.edu.co.epsandes.modelo.Afiliado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfiliadoRepository extends JpaRepository<Afiliado, Long> {
    
    // Buscar afiliados por tipo
    List<Afiliado> findByTipoAfiliado(String tipoAfiliado);
    
    // Buscar beneficiarios de un contribuyente específico
    List<Afiliado> findByNumeroDocumentoContribuyente(Long numeroDocumentoContribuyente);
    
    // Buscar afiliados por nombre
    List<Afiliado> findByNombreContaining(String nombre);
    
    // Buscar afiliados por tipo de documento
    List<Afiliado> findByTipoDocumento(String tipoDocumento);
    
    // Verificar si un afiliado tiene beneficiarios
    @Query("SELECT COUNT(a) > 0 FROM Afiliado a WHERE a.numeroDocumentoContribuyente = :numeroDocumento")
    boolean tieneBeneficiarios(@Param("numeroDocumento") Long numeroDocumento);
}