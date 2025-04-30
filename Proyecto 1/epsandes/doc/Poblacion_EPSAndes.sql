-- =======================================================================
-- PROYECTO EPSANDES - SISTEMAS TRANSACCIONALES
-- POBLACIÓN DE DATOS
-- UNIVERSIDAD DE LOS ANDES - ISIS2304
-- =======================================================================

-- =======================================================================
-- PASO 1: LIMPIAR DATOS EXISTENTES
-- =======================================================================

DELETE FROM ServiciosMedico;
DELETE FROM ServiciosIPS;
DELETE FROM ServicioOrden;
DELETE FROM PrestacionServicio;
DELETE FROM AgendarCita;
DELETE FROM OrdenDeServicio;
DELETE FROM Medico;
DELETE FROM ServicioDeSalud;
DELETE FROM IPS;
DELETE FROM Afiliado;

-- =======================================================================
-- PASO 2: INSERTAR DATOS DE IPS
-- =======================================================================

INSERT INTO IPS (NIT, Nombre, Direccion, Telefono) VALUES (1234567890, 'Hospital San Rafael', 'Calle 100 #15-25', '6017456789');
INSERT INTO IPS (NIT, Nombre, Direccion, Telefono) VALUES (1234567891, 'Clínica Los Andes', 'Carrera 7 #45-67', '6017654321');
INSERT INTO IPS (NIT, Nombre, Direccion, Telefono) VALUES (1234567892, 'Centro Médico Colsanitas', 'Avenida 19 #100-45', '6018967452');
INSERT INTO IPS (NIT, Nombre, Direccion, Telefono) VALUES (1234567893, 'Hospital Universitario', 'Calle 170 #45-30', '6017896541');
INSERT INTO IPS (NIT, Nombre, Direccion, Telefono) VALUES (1234567894, 'Clínica del Country', 'Carrera 16 #82-57', '6016543987');

-- =======================================================================
-- PASO 3: INSERTAR DATOS DE SERVICIOS DE SALUD
-- =======================================================================

INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (1, 'Consulta Medicina General', 'Consulta médica con médico general', 'CONSULTA_GENERAL');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (2, 'Consulta Cardiología', 'Consulta con especialista en cardiología', 'CONSULTA_ESPECIALISTA');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (3, 'Consulta Pediatría', 'Consulta con especialista en pediatría', 'CONSULTA_ESPECIALISTA');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (4, 'Consulta Ortopedia', 'Consulta con especialista en ortopedia', 'CONSULTA_ESPECIALISTA');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (5, 'Consulta de Control', 'Consulta de seguimiento y control', 'CONSULTA_CONTROL');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (6, 'Urgencias', 'Servicio de urgencias 24 horas', 'CONSULTA_URGENCIAS');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (7, 'Radiografía', 'Examen diagnóstico de radiografía', 'EXAMEN_DIAGNOSTICO');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (8, 'Ecografía', 'Examen diagnóstico de ecografía', 'EXAMEN_DIAGNOSTICO');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (9, 'Examenes de Laboratorio', 'Análisis clínicos de laboratorio', 'EXAMEN_DIAGNOSTICO');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (10, 'Terapia Física', 'Sesiones de terapia física', 'TERAPIA');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (11, 'Terapia Respiratoria', 'Sesiones de terapia respiratoria', 'TERAPIA');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (12, 'Cirugía Ambulatoria', 'Procedimientos quirúrgicos ambulatorios', 'PROCEDIMIENTO_MEDICO');
INSERT INTO ServicioDeSalud (ID_Servicio, Nombre, Descripcion, Tipo) VALUES (13, 'Hospitalización General', 'Servicio de hospitalización', 'HOSPITALIZACION');

-- =======================================================================
-- PASO 4: INSERTAR DATOS DE AFILIADOS
-- =======================================================================

-- Contribuyentes
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('CC', 1001234567, 'Juan Pérez García', TO_DATE('1980-05-15', 'YYYY-MM-DD'), 'Calle 45 #12-34', '3101234567', 'CONTRIBUYENTE', NULL, NULL);
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('CC', 1002345678, 'María López Rodríguez', TO_DATE('1975-08-20', 'YYYY-MM-DD'), 'Carrera 15 #67-89', '3202345678', 'CONTRIBUYENTE', NULL, NULL);
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('CC', 1003456789, 'Carlos Martínez Sánchez', TO_DATE('1985-11-30', 'YYYY-MM-DD'), 'Avenida 30 #45-67', '3003456789', 'CONTRIBUYENTE', NULL, NULL);

-- Beneficiarios
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('TI', 2001234567, 'Ana Pérez López', TO_DATE('2010-03-25', 'YYYY-MM-DD'), 'Calle 45 #12-34', '3101234567', 'BENEFICIARIO', 1001234567, 'HIJA');
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('CC', 2002345678, 'Laura Pérez López', TO_DATE('1982-06-10', 'YYYY-MM-DD'), 'Calle 45 #12-34', '3104567890', 'BENEFICIARIO', 1001234567, 'CONYUGE');
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('TI', 2003456789, 'Daniel López González', TO_DATE('2012-09-15', 'YYYY-MM-DD'), 'Carrera 15 #67-89', '3202345678', 'BENEFICIARIO', 1002345678, 'HIJO');

-- =======================================================================
-- PASO 5: INSERTAR DATOS DE MÉDICOS
-- =======================================================================

INSERT INTO Medico (Nombre, TipoDocumento, NumeroDocumento, NumeroRegistroMedico, Especialidad, IPS_NIT) 
VALUES ('Dr. Roberto Gómez', 'CC', 8001234567, 12345, 'Medicina General', 1234567890);
INSERT INTO Medico (Nombre, TipoDocumento, NumeroDocumento, NumeroRegistroMedico, Especialidad, IPS_NIT) 
VALUES ('Dra. Carmen Silva', 'CC', 8002345678, 23456, 'Cardiología', 1234567890);
INSERT INTO Medico (Nombre, TipoDocumento, NumeroDocumento, NumeroRegistroMedico, Especialidad, IPS_NIT) 
VALUES ('Dr. Luis Ramírez', 'CC', 8003456789, 34567, 'Pediatría', 1234567891);
INSERT INTO Medico (Nombre, TipoDocumento, NumeroDocumento, NumeroRegistroMedico, Especialidad, IPS_NIT) 
VALUES ('Dra. Patricia Torres', 'CC', 8004567890, 45678, 'Ortopedia', 1234567891);
INSERT INTO Medico (Nombre, TipoDocumento, NumeroDocumento, NumeroRegistroMedico, Especialidad, IPS_NIT) 
VALUES ('Dr. Andrés Moreno', 'CC', 8005678901, 56789, 'Medicina General', 1234567892);
INSERT INTO Medico (Nombre, TipoDocumento, NumeroDocumento, NumeroRegistroMedico, Especialidad, IPS_NIT) 
VALUES ('Dra. Diana Castro', 'CC', 8006789012, 67890, 'Radiología', 1234567892);

-- =======================================================================
-- PASO 6: ASIGNAR SERVICIOS A IPS
-- =======================================================================

-- Hospital San Rafael
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567890, 1);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567890, 2);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567890, 5);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567890, 6);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567890, 7);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567890, 8);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567890, 9);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567890, 13);

-- Clínica Los Andes
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567891, 1);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567891, 3);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567891, 4);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567891, 5);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567891, 6);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567891, 7);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567891, 9);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567891, 10);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567891, 12);

-- Centro Médico Colsanitas
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567892, 1);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567892, 5);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567892, 6);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567892, 7);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567892, 8);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567892, 9);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567892, 10);
INSERT INTO ServiciosIPS (IPS_NIT, Servicio_ID) VALUES (1234567892, 11);

-- =======================================================================
-- PASO 7: ASIGNAR SERVICIOS A MÉDICOS
-- =======================================================================

INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8001234567, 1);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8001234567, 5);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8001234567, 6);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8002345678, 2);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8002345678, 5);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8003456789, 3);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8003456789, 5);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8003456789, 6);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8004567890, 4);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8004567890, 5);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8005678901, 1);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8005678901, 5);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8005678901, 6);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8006789012, 7);
INSERT INTO ServiciosMedico (Medico_NumeroDocumento, Servicio_ID) VALUES (8006789012, 8);

-- =======================================================================
-- PASO 8: CREAR ÓRDENES DE SERVICIO
-- =======================================================================

INSERT INTO OrdenDeServicio (ID_Orden, Fecha_hora, EstadoOrden, Medico_NumeroDocumento, Afiliado_NumeroDocumento) 
VALUES (1, TO_TIMESTAMP('2025-01-15 10:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'VIGENTE', 8001234567, 1001234567);
INSERT INTO OrdenDeServicio (ID_Orden, Fecha_hora, EstadoOrden, Medico_NumeroDocumento, Afiliado_NumeroDocumento) 
VALUES (2, TO_TIMESTAMP('2025-01-16 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'VIGENTE', 8001234567, 1002345678);
INSERT INTO OrdenDeServicio (ID_Orden, Fecha_hora, EstadoOrden, Medico_NumeroDocumento, Afiliado_NumeroDocumento) 
VALUES (3, TO_TIMESTAMP('2025-01-17 09:15:00', 'YYYY-MM-DD HH24:MI:SS'), 'VIGENTE', 8003456789, 2001234567);
INSERT INTO OrdenDeServicio (ID_Orden, Fecha_hora, EstadoOrden, Medico_NumeroDocumento, Afiliado_NumeroDocumento) 
VALUES (4, TO_TIMESTAMP('2025-01-18 14:45:00', 'YYYY-MM-DD HH24:MI:SS'), 'COMPLETADA', 8005678901, 1003456789);

-- =======================================================================
-- PASO 9: RELACIONAR SERVICIOS CON ÓRDENES
-- =======================================================================

INSERT INTO ServicioOrden (ServicioDeSalud_ID_Servicio, OrdenDeServicio_ID_Orden) VALUES (2, 1); -- Cardiología para Juan Pérez
INSERT INTO ServicioOrden (ServicioDeSalud_ID_Servicio, OrdenDeServicio_ID_Orden) VALUES (7, 1); -- Radiografía para Juan Pérez
INSERT INTO ServicioOrden (ServicioDeSalud_ID_Servicio, OrdenDeServicio_ID_Orden) VALUES (4, 2); -- Ortopedia para María López
INSERT INTO ServicioOrden (ServicioDeSalud_ID_Servicio, OrdenDeServicio_ID_Orden) VALUES (3, 3); -- Pediatría para Ana Pérez
INSERT INTO ServicioOrden (ServicioDeSalud_ID_Servicio, OrdenDeServicio_ID_Orden) VALUES (9, 4); -- Exámenes de laboratorio para Carlos Martínez

-- =======================================================================
-- PASO 10: AGENDAR CITAS
-- =======================================================================

-- Citas sin orden (consulta general y urgencias)
INSERT INTO AgendarCita (IDCita, Fecha_hora, Afiliado_NumeroDocumento, Medico_NumeroDocumento, ID_OrdenDeServicio, ServicioDeSalud_ID) 
VALUES (1, TO_TIMESTAMP('2025-02-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1001234567, 8001234567, NULL, 1);
INSERT INTO AgendarCita (IDCita, Fecha_hora, Afiliado_NumeroDocumento, Medico_NumeroDocumento, ID_OrdenDeServicio, ServicioDeSalud_ID) 
VALUES (2, TO_TIMESTAMP('2025-02-02 09:30:00', 'YYYY-MM-DD HH24:MI:SS'), 1002345678, 8005678901, NULL, 1);
INSERT INTO AgendarCita (IDCita, Fecha_hora, Afiliado_NumeroDocumento, Medico_NumeroDocumento, ID_OrdenDeServicio, ServicioDeSalud_ID) 
VALUES (3, TO_TIMESTAMP('2025-02-03 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2003456789, 8003456789, NULL, 6);

-- Citas con orden de servicio
INSERT INTO AgendarCita (IDCita, Fecha_hora, Afiliado_NumeroDocumento, Medico_NumeroDocumento, ID_OrdenDeServicio, ServicioDeSalud_ID) 
VALUES (4, TO_TIMESTAMP('2025-02-05 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1001234567, 8002345678, 1, 2);
INSERT INTO AgendarCita (IDCita, Fecha_hora, Afiliado_NumeroDocumento, Medico_NumeroDocumento, ID_OrdenDeServicio, ServicioDeSalud_ID) 
VALUES (5, TO_TIMESTAMP('2025-02-05 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1001234567, 8006789012, 1, 7);
INSERT INTO AgendarCita (IDCita, Fecha_hora, Afiliado_NumeroDocumento, Medico_NumeroDocumento, ID_OrdenDeServicio, ServicioDeSalud_ID) 
VALUES (6, TO_TIMESTAMP('2025-02-07 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 1002345678, 8004567890, 2, 4);

-- =======================================================================
-- PASO 11: REGISTRAR PRESTACIÓN DE SERVICIOS
-- =======================================================================

INSERT INTO PrestacionServicio (IPS_NIT, AgendarCita_IDCita, CitaRealizada) VALUES (1234567890, 1, 1); -- Cita realizada
INSERT INTO PrestacionServicio (IPS_NIT, AgendarCita_IDCita, CitaRealizada) VALUES (1234567892, 2, 1); -- Cita realizada
INSERT INTO PrestacionServicio (IPS_NIT, AgendarCita_IDCita, CitaRealizada) VALUES (1234567891, 3, 0); -- Cita pendiente
INSERT INTO PrestacionServicio (IPS_NIT, AgendarCita_IDCita, CitaRealizada) VALUES (1234567890, 4, 0); -- Cita pendiente
INSERT INTO PrestacionServicio (IPS_NIT, AgendarCita_IDCita, CitaRealizada) VALUES (1234567892, 5, 0); -- Cita pendiente
INSERT INTO PrestacionServicio (IPS_NIT, AgendarCita_IDCita, CitaRealizada) VALUES (1234567891, 6, 0); -- Cita pendiente

-- =======================================================================
-- FIN DEL SCRIPT DE POBLACIÓN
-- =======================================================================

COMMIT;