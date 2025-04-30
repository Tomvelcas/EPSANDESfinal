-- =======================================================================
-- PROYECTO EPSANDES - SISTEMAS TRANSACCIONALES
-- CONSULTAS RFC (REQUERIMIENTOS FUNCIONALES DE CONSULTA)
-- UNIVERSIDAD DE LOS ANDES - ISIS2304
-- =======================================================================

-- =======================================================================
-- RFC1: CONSULTAR LA AGENDA DE DISPONIBILIDAD DE UN SERVICIO DE SALUD
-- EN LAS SIGUIENTES 4 SEMANAS
-- =======================================================================

-- Variables para la consulta (establecer antes de ejecutar)
-- :ID_SERVICIO_BUSCADO := 1;  -- ID del servicio a buscar

WITH Citas_Agendadas AS (
    SELECT 
        ac.Fecha_hora,
        ac.Medico_NumeroDocumento,
        ac.ServicioDeSalud_ID
    FROM AgendarCita ac
    WHERE ac.Fecha_hora BETWEEN SYSDATE AND SYSDATE + 28
)
SELECT 
    s.Nombre AS Servicio,
    TO_CHAR(disponibilidad.fecha_disponibilidad, 'YYYY-MM-DD HH24:MI') AS Fecha_Disponible,
    i.Nombre AS IPS,
    m.Nombre AS Medico
FROM (
    -- Generar horarios disponibles (asumiendo jornada laboral de 8:00 a 18:00)
    SELECT 
        fecha_base + (hora/24) AS fecha_disponibilidad,
        sm.Medico_NumeroDocumento,
        sm.Servicio_ID
    FROM 
        (SELECT TRUNC(SYSDATE) + LEVEL - 1 AS fecha_base
         FROM DUAL
         CONNECT BY LEVEL <= 28) fechas,
        (SELECT 8 + LEVEL - 1 AS hora
         FROM DUAL
         CONNECT BY LEVEL <= 10) horas,
        ServiciosMedico sm
    WHERE 
        sm.Servicio_ID = :ID_SERVICIO_BUSCADO
        AND TO_CHAR(fecha_base, 'D') BETWEEN '2' AND '6'  -- Lunes a Viernes
) disponibilidad
JOIN Medico m ON m.NumeroDocumento = disponibilidad.Medico_NumeroDocumento
JOIN IPS i ON i.NIT = m.IPS_NIT
JOIN ServicioDeSalud s ON s.ID_Servicio = disponibilidad.Servicio_ID
WHERE NOT EXISTS (
    SELECT 1 
    FROM Citas_Agendadas ca
    WHERE ca.Fecha_hora = disponibilidad.fecha_disponibilidad
    AND ca.Medico_NumeroDocumento = disponibilidad.Medico_NumeroDocumento
    AND ca.ServicioDeSalud_ID = disponibilidad.Servicio_ID
)
ORDER BY disponibilidad.fecha_disponibilidad, i.Nombre, m.Nombre;

/*
DOCUMENTACIÓN RFC1:
- Tablas usadas: ServicioDeSalud, Medico, IPS, ServiciosMedico, AgendarCita
- Atributos para joins:
  * Medico.NumeroDocumento = ServiciosMedico.Medico_NumeroDocumento
  * Medico.IPS_NIT = IPS.NIT
  * ServicioDeSalud.ID_Servicio = ServiciosMedico.Servicio_ID
- Tipo de join: INNER JOIN
- Razón: Necesitamos solo las combinaciones válidas de médicos que prestan el servicio
*/

-- =======================================================================
-- RFC2: MOSTRAR LOS 20 SERVICIOS MÁS SOLICITADOS EN UN PERÍODO DE TIEMPO
-- =======================================================================

-- Variables para la consulta
-- :FECHA_INICIO := TO_DATE('2025-01-01', 'YYYY-MM-DD');
-- :FECHA_FIN := TO_DATE('2025-12-31', 'YYYY-MM-DD');

SELECT 
    s.ID_Servicio,
    s.Nombre AS Nombre_Servicio,
    s.Tipo AS Tipo_Servicio,
    COUNT(ac.IDCita) AS Total_Solicitudes
FROM ServicioDeSalud s
LEFT JOIN AgendarCita ac ON s.ID_Servicio = ac.ServicioDeSalud_ID
    AND ac.Fecha_hora BETWEEN :FECHA_INICIO AND :FECHA_FIN
GROUP BY s.ID_Servicio, s.Nombre, s.Tipo
ORDER BY Total_Solicitudes DESC
FETCH FIRST 20 ROWS ONLY;

/*
DOCUMENTACIÓN RFC2:
- Tablas usadas: ServicioDeSalud, AgendarCita
- Atributos para joins:
  * ServicioDeSalud.ID_Servicio = AgendarCita.ServicioDeSalud_ID
- Tipo de join: LEFT JOIN
- Razón: Necesitamos todos los servicios, incluso los que no han sido solicitados
*/

-- =======================================================================
-- RFC3: MOSTRAR EL ÍNDICE DE USO DE CADA UNO DE LOS SERVICIOS PROVISTOS
-- =======================================================================

-- Variables para la consulta
-- :FECHA_INICIO := TO_DATE('2025-01-01', 'YYYY-MM-DD');
-- :FECHA_FIN := TO_DATE('2025-12-31', 'YYYY-MM-DD');

WITH Servicios_Disponibles AS (
    SELECT 
        s.ID_Servicio,
        s.Nombre,
        COUNT(DISTINCT sm.Medico_NumeroDocumento) AS Medicos_Disponibles,
        COUNT(DISTINCT si.IPS_NIT) AS IPS_Disponibles
    FROM ServicioDeSalud s
    LEFT JOIN ServiciosMedico sm ON s.ID_Servicio = sm.Servicio_ID
    LEFT JOIN ServiciosIPS si ON s.ID_Servicio = si.Servicio_ID
    GROUP BY s.ID_Servicio, s.Nombre
),
Servicios_Usados AS (
    SELECT 
        ac.ServicioDeSalud_ID,
        COUNT(*) AS Total_Usos
    FROM AgendarCita ac
    JOIN PrestacionServicio ps ON ac.IDCita = ps.AgendarCita_IDCita
    WHERE ps.CitaRealizada = 1
    AND ac.Fecha_hora BETWEEN :FECHA_INICIO AND :FECHA_FIN
    GROUP BY ac.ServicioDeSalud_ID
)
SELECT 
    sd.ID_Servicio,
    sd.Nombre AS Nombre_Servicio,
    sd.Medicos_Disponibles,
    sd.IPS_Disponibles,
    COALESCE(su.Total_Usos, 0) AS Total_Usos,
    CASE 
        WHEN sd.Medicos_Disponibles = 0 OR sd.IPS_Disponibles = 0 THEN 0
        ELSE ROUND(COALESCE(su.Total_Usos, 0) / (sd.Medicos_Disponibles * sd.IPS_Disponibles), 4)
    END AS Indice_Uso
FROM Servicios_Disponibles sd
LEFT JOIN Servicios_Usados su ON sd.ID_Servicio = su.ServicioDeSalud_ID
ORDER BY Indice_Uso DESC, sd.Nombre;

/*
DOCUMENTACIÓN RFC3:
- Tablas usadas: ServicioDeSalud, ServiciosMedico, ServiciosIPS, AgendarCita, PrestacionServicio
- Atributos para joins:
  * ServicioDeSalud.ID_Servicio = ServiciosMedico.Servicio_ID
  * ServicioDeSalud.ID_Servicio = ServiciosIPS.Servicio_ID
  * AgendarCita.IDCita = PrestacionServicio.AgendarCita_IDCita
- Tipo de join: LEFT JOIN (para incluir servicios sin uso) e INNER JOIN (para citas realizadas)
- Razón: Necesitamos calcular el índice incluso para servicios que no han sido usados
*/

-- =======================================================================
-- RFC4: MOSTRAR LA UTILIZACIÓN DE SERVICIOS POR UN AFILIADO DADO
-- EN UN PERÍODO DADO
-- =======================================================================

-- Variables para la consulta
-- :NUMERO_DOCUMENTO_AFILIADO := 1001234567;
-- :FECHA_INICIO := TO_DATE('2025-01-01', 'YYYY-MM-DD');
-- :FECHA_FIN := TO_DATE('2025-12-31', 'YYYY-MM-DD');

SELECT 
    s.Nombre AS Nombre_Servicio,
    TO_CHAR(ac.Fecha_hora, 'YYYY-MM-DD HH24:MI') AS Fecha_Servicio,
    m.Nombre AS Medico_Atendido,
    i.Nombre AS IPS_Prestadora
FROM AgendarCita ac
JOIN ServicioDeSalud s ON ac.ServicioDeSalud_ID = s.ID_Servicio
JOIN Medico m ON ac.Medico_NumeroDocumento = m.NumeroDocumento
JOIN IPS i ON m.IPS_NIT = i.NIT
JOIN PrestacionServicio ps ON ac.IDCita = ps.AgendarCita_IDCita
WHERE ac.Afiliado_NumeroDocumento = :NUMERO_DOCUMENTO_AFILIADO
AND ac.Fecha_hora BETWEEN :FECHA_INICIO AND :FECHA_FIN
AND ps.CitaRealizada = 1
ORDER BY ac.Fecha_hora;

/*
DOCUMENTACIÓN RFC4:
- Tablas usadas: AgendarCita, ServicioDeSalud, Medico, IPS, PrestacionServicio
- Atributos para joins:
  * AgendarCita.ServicioDeSalud_ID = ServicioDeSalud.ID_Servicio
  * AgendarCita.Medico_NumeroDocumento = Medico.NumeroDocumento
  * Medico.IPS_NIT = IPS.NIT
  * AgendarCita.IDCita = PrestacionServicio.AgendarCita_IDCita
- Tipo de join: INNER JOIN
- Razón: Solo necesitamos los servicios efectivamente prestados al afiliado
*/

-- =======================================================================
-- CONSULTAS ADICIONALES PARA PRUEBAS Y VERIFICACIÓN
-- =======================================================================

-- Consulta para verificar los datos insertados en cada tabla
SELECT 'IPS' as Tabla, COUNT(*) as Total FROM IPS
UNION ALL
SELECT 'ServicioDeSalud', COUNT(*) FROM ServicioDeSalud
UNION ALL
SELECT 'Afiliado', COUNT(*) FROM Afiliado
UNION ALL
SELECT 'Medico', COUNT(*) FROM Medico
UNION ALL
SELECT 'OrdenDeServicio', COUNT(*) FROM OrdenDeServicio
UNION ALL
SELECT 'AgendarCita', COUNT(*) FROM AgendarCita
UNION ALL
SELECT 'PrestacionServicio', COUNT(*) FROM PrestacionServicio
UNION ALL
SELECT 'ServicioOrden', COUNT(*) FROM ServicioOrden
UNION ALL
SELECT 'ServiciosIPS', COUNT(*) FROM ServiciosIPS
UNION ALL
SELECT 'ServiciosMedico', COUNT(*) FROM ServiciosMedico;

-- Consulta para verificar afiliados por tipo
SELECT 
    TipoAfiliado,
    COUNT(*) as Total
FROM Afiliado
GROUP BY TipoAfiliado;

-- Consulta para verificar médicos por especialidad
SELECT 
    Especialidad,
    COUNT(*) as Total
FROM Medico
GROUP BY Especialidad
ORDER BY Total DESC;

-- Consulta para verificar servicios por tipo
SELECT 
    Tipo,
    COUNT(*) as Total
FROM ServicioDeSalud
GROUP BY Tipo
ORDER BY Total DESC;

-- Consulta para verificar órdenes por estado
SELECT 
    EstadoOrden,
    COUNT(*) as Total
FROM OrdenDeServicio
GROUP BY EstadoOrden;

-- =======================================================================
-- FIN DE LOS SCRIPTS DE CONSULTAS RFC
-- =======================================================================