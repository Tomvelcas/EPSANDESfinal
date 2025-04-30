-- =======================================================================
-- PROYECTO EPSANDES - SISTEMAS TRANSACCIONALES
-- SCRIPTS DE PRUEBA
-- UNIVERSIDAD DE LOS ANDES - ISIS2304
-- =======================================================================

-- =======================================================================
-- PRUEBA 1: PRUEBAS DE UNICIDAD DE TUPLAS
-- =======================================================================

-- Intentar insertar una IPS con PK nueva (debería funcionar)
INSERT INTO IPS (NIT, Nombre, Direccion, Telefono) 
VALUES (9999999999, 'Nueva IPS de Prueba', 'Calle 200 #50-60', '6019876543');

-- Intentar insertar una IPS con la misma PK (debería fallar)
INSERT INTO IPS (NIT, Nombre, Direccion, Telefono) 
VALUES (9999999999, 'IPS Duplicada', 'Otra dirección', '6011234567');
-- RESULTADO ESPERADO: Error por violación de llave primaria

-- Limpiar datos de prueba
DELETE FROM IPS WHERE NIT = 9999999999;

-- =======================================================================
-- PRUEBA 2: PRUEBAS DE INTEGRIDAD REFERENCIAL CON FK
-- =======================================================================

-- Intentar insertar un médico con IPS existente (debería funcionar)
INSERT INTO Medico (Nombre, TipoDocumento, NumeroDocumento, NumeroRegistroMedico, Especialidad, IPS_NIT) 
VALUES ('Dr. Prueba Exitosa', 'CC', 9999999999, 99999, 'Medicina General', 1234567890);

-- Intentar insertar un médico con IPS que no existe (debería fallar)
INSERT INTO Medico (Nombre, TipoDocumento, NumeroDocumento, NumeroRegistroMedico, Especialidad, IPS_NIT) 
VALUES ('Dr. Prueba Fallida', 'CC', 9999999998, 99998, 'Medicina General', 5555555555);
-- RESULTADO ESPERADO: Error por violación de llave foránea

-- Limpiar datos de prueba
DELETE FROM Medico WHERE NumeroDocumento = 9999999999;

-- =======================================================================
-- PRUEBA 3: PRUEBAS DE INTEGRIDAD DE ACUERDO CON RESTRICCIONES DE CHEQUEO
-- =======================================================================

-- Intentar insertar un afiliado con tipo válido (debería funcionar)
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('CC', 9999999997, 'Afiliado de Prueba', TO_DATE('1990-01-01', 'YYYY-MM-DD'), 'Dirección Test', '3001234567', 'CONTRIBUYENTE', NULL, NULL);

-- Intentar insertar un afiliado con tipo inválido (debería fallar)
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('CC', 9999999996, 'Afiliado Inválido', TO_DATE('1990-01-01', 'YYYY-MM-DD'), 'Dirección Test', '3001234567', 'INVALIDO', NULL, NULL);
-- RESULTADO ESPERADO: Error por violación de restricción CHECK

-- Intentar insertar una orden con estado inválido (debería fallar)
INSERT INTO OrdenDeServicio (ID_Orden, Fecha_hora, EstadoOrden, Medico_NumeroDocumento, Afiliado_NumeroDocumento) 
VALUES (9999, TO_TIMESTAMP('2025-03-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'INVALIDO', 8001234567, 1001234567);
-- RESULTADO ESPERADO: Error por violación de restricción CHECK

-- Limpiar datos de prueba
DELETE FROM Afiliado WHERE NumeroDocumento = 9999999997;

-- =======================================================================
-- PRUEBA 4: PRUEBAS DE REGLAS DE NEGOCIO
-- =======================================================================

-- Prueba: No permitir citas duplicadas para el mismo afiliado en la misma fecha/hora
INSERT INTO AgendarCita (IDCita, Fecha_hora, Afiliado_NumeroDocumento, Medico_NumeroDocumento, ID_OrdenDeServicio, ServicioDeSalud_ID) 
VALUES (9999, TO_TIMESTAMP('2025-04-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1001234567, 8001234567, NULL, 1);

-- Intentar agendar otra cita para el mismo afiliado en la misma fecha/hora (debería fallar)
INSERT INTO AgendarCita (IDCita, Fecha_hora, Afiliado_NumeroDocumento, Medico_NumeroDocumento, ID_OrdenDeServicio, ServicioDeSalud_ID) 
VALUES (9998, TO_TIMESTAMP('2025-04-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1001234567, 8002345678, NULL, 2);
-- RESULTADO ESPERADO: Error por violación de restricción UNIQUE

-- Limpiar datos de prueba
DELETE FROM AgendarCita WHERE IDCita = 9999;

-- =======================================================================
-- PRUEBA 5: PRUEBAS DE INTEGRIDAD DE BENEFICIARIOS
-- =======================================================================

-- Prueba: Beneficiario debe estar asociado a un contribuyente válido
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('TI', 9999999995, 'Beneficiario de Prueba', TO_DATE('2010-01-01', 'YYYY-MM-DD'), 'Dirección Test', '3001234567', 'BENEFICIARIO', 1001234567, 'HIJO');

-- Intentar insertar un beneficiario asociado a un contribuyente que no existe (debería fallar)
INSERT INTO Afiliado (TipoDocumento, NumeroDocumento, Nombre, FechaNacimiento, Direccion, Telefono, TipoAfiliado, NumeroDocumentoContribuyente, Parentesco) 
VALUES ('TI', 9999999994, 'Beneficiario Inválido', TO_DATE('2010-01-01', 'YYYY-MM-DD'), 'Dirección Test', '3001234567', 'BENEFICIARIO', 9999999999, 'HIJO');
-- RESULTADO ESPERADO: Error por violación de llave foránea

-- Limpiar datos de prueba
DELETE FROM Afiliado WHERE NumeroDocumento = 9999999995;

-- =======================================================================
-- PRUEBA 6: PRUEBAS DE PRESTACIÓN DE SERVICIOS
-- =======================================================================

-- Prueba: No permitir prestación de servicio duplicada para la misma cita
-- Primero insertar una cita de prueba
INSERT INTO AgendarCita (IDCita, Fecha_hora, Afiliado_NumeroDocumento, Medico_NumeroDocumento, ID_OrdenDeServicio, ServicioDeSalud_ID) 
VALUES (9997, TO_TIMESTAMP('2025-04-15 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1001234567, 8001234567, NULL, 1);

-- Insertar prestación de servicio
INSERT INTO PrestacionServicio (IPS_NIT, AgendarCita_IDCita, CitaRealizada) 
VALUES (1234567890, 9997, 1);

-- Intentar duplicar la prestación (debería fallar)
INSERT INTO PrestacionServicio (IPS_NIT, AgendarCita_IDCita, CitaRealizada) 
VALUES (1234567890, 9997, 1);
-- RESULTADO ESPERADO: Error por violación de restricción UNIQUE

-- Limpiar datos de prueba
DELETE FROM PrestacionServicio WHERE AgendarCita_IDCita = 9997;
DELETE FROM AgendarCita WHERE IDCita = 9997;

-- =======================================================================
-- FIN DE LOS SCRIPTS DE PRUEBA
-- =======================================================================