package uniandes.edu.co.epsandes.repositorio;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uniandes.edu.co.epsandes.modelo.AgendarCita;

@Repository
public interface AgendarCitaRepository extends JpaRepository<AgendarCita, Long> {
    
    // Buscar citas por afiliado
    List<AgendarCita> findByAfiliadoNumeroDocumento(Long numeroDocumento);
    
    // Buscar citas por médico
    List<AgendarCita> findByMedicoNumeroDocumento(Long numeroDocumento);
    
    // Buscar citas por servicio de salud
    List<AgendarCita> findByServicioDeSaludIdServicio(Long idServicio);
    
    // Buscar citas en un rango de fechas
    List<AgendarCita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Verificar si existe una cita para un afiliado en una fecha y hora específica
    boolean existsByAfiliadoNumeroDocumentoAndFechaHora(Long numeroDocumento, LocalDateTime fechaHora);
    
    // Buscar disponibilidad de citas para un servicio en las próximas 4 semanas
    @Query(value = "SELECT m.NOMBRE as nombre_medico, i.NOMBRE as nombre_ips, " +
        "TO_CHAR(fechas.fecha_base + (horas.hora/24), 'YYYY-MM-DD HH24:MI') as fecha_disponible " +
        "FROM SERVICIOSMEDICO sm " +
        "JOIN MEDICO m ON m.NUMERODOCUMENTO = sm.MEDICO_NUMERODOCUMENTO " +
        "JOIN IPS i ON i.NIT = m.IPS_NIT " +
        "CROSS JOIN (SELECT TRUNC(SYSDATE) + LEVEL - 1 as fecha_base FROM DUAL " +
        "CONNECT BY LEVEL <= 28) fechas " +
        "CROSS JOIN (SELECT LEVEL - 1 as hora FROM DUAL " + // Modificado para generar horas exactas
        "CONNECT BY LEVEL <= 24) horas " + // 24 horas al día
        "WHERE sm.SERVICIO_ID = :servicioId " +
        "AND NOT EXISTS (SELECT 1 FROM AGENDARCITA ac " +
        "WHERE ac.MEDICO_NUMERODOCUMENTO = sm.MEDICO_NUMERODOCUMENTO " +
        "AND ac.SERVICIODESALUD_ID = :servicioId " +
        "AND ac.FECHA_HORA = fechas.fecha_base + (horas.hora/24))", 
        nativeQuery = true)
    List<Object[]> findDisponibilidadServicio(@Param("servicioId") Long servicioId);
    
    // Buscar disponibilidad de citas para un servicio en un rango de fechas específico
    @Query(value = "SELECT ac.IDCITA, ac.FECHA_HORA, m.NOMBRE, s.NOMBRE " +
              "FROM AGENDARCITA ac " +
              "JOIN MEDICO m ON ac.MEDICO_NUMERODOCUMENTO = m.NUMERODOCUMENTO " +
              "JOIN SERVICIODESALUD s ON ac.SERVICIODESALUD_ID = s.ID_SERVICIO " +
              "WHERE ac.SERVICIODESALUD_ID = :servicioId " +
              "AND ac.MEDICO_NUMERODOCUMENTO = :medicoId " +
              "AND TO_CHAR(ac.FECHA_HORA, 'YYYY-MM-DD HH24:MI:SS') BETWEEN :fechaInicio AND :fechaFin", 
        nativeQuery = true)
    List<Object[]> findDisponibilidadServicioTransaccional(@Param("servicioId") Long servicioId, 
                                        @Param("medicoId") Long medicoId, 
                                        @Param("fechaInicio") String fechaInicio, 
                                        @Param("fechaFin") String fechaFin);
}