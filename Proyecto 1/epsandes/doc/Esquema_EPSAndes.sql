-- =======================================================================
-- PROYECTO EPSANDES - SISTEMAS TRANSACCIONALES
-- CREACIÓN DE BASE DE DATOS
-- UNIVERSIDAD DE LOS ANDES - ISIS2304
-- =======================================================================

-- =======================================================================
-- PASO 1: ELIMINAR TABLAS EXISTENTES (Si existen)
-- =======================================================================

-- Eliminar restricciones de clave foránea primero
BEGIN
    FOR c IN (SELECT table_name, constraint_name 
              FROM user_constraints 
              WHERE constraint_type = 'R') LOOP
        EXECUTE IMMEDIATE ('ALTER TABLE ' || c.table_name || ' DROP CONSTRAINT ' || c.constraint_name);
    END LOOP;
END;
/

-- Eliminar tablas
BEGIN
    FOR t IN (SELECT table_name FROM user_tables) LOOP
        EXECUTE IMMEDIATE ('DROP TABLE ' || t.table_name || ' CASCADE CONSTRAINTS');
    END LOOP;
END;
/

-- =======================================================================
-- PASO 2: CREAR TABLAS
-- =======================================================================

-- Tabla AFILIADO
CREATE TABLE Afiliado 
    ( 
     TipoDocumento                VARCHAR2(20 CHAR), 
     NumeroDocumento              NUMBER(20,0) NOT NULL, 
     Nombre                       VARCHAR2(60 CHAR) NOT NULL, 
     FechaNacimiento              DATE NOT NULL, 
     Direccion                    VARCHAR2(60 CHAR), 
     Telefono                     VARCHAR2(20 CHAR), 
     TipoAfiliado                 VARCHAR2(20 CHAR) NOT NULL, 
     NumeroDocumentoContribuyente NUMBER(20,0), 
     Parentesco                   VARCHAR2(20 CHAR) 
    );

-- Tabla IPS
CREATE TABLE IPS 
    ( 
     NIT       NUMBER(20,0) NOT NULL, 
     Nombre    VARCHAR2(60 CHAR) NOT NULL, 
     Direccion VARCHAR2(60 CHAR) NOT NULL, 
     Telefono  VARCHAR2(20 CHAR) NOT NULL
    );

-- Tabla MEDICO
CREATE TABLE Medico 
    ( 
     Nombre               VARCHAR2(60 CHAR) NOT NULL, 
     TipoDocumento        VARCHAR2(20 CHAR) NOT NULL, 
     NumeroDocumento      NUMBER(20,0) NOT NULL, 
     NumeroRegistroMedico NUMBER(20,0) NOT NULL, 
     Especialidad         VARCHAR2(60 CHAR) NOT NULL, 
     IPS_NIT              NUMBER(20,0) NOT NULL 
    );

-- Tabla SERVICIO DE SALUD
CREATE TABLE ServicioDeSalud 
    ( 
     ID_Servicio NUMBER(20,0) NOT NULL, 
     Nombre      VARCHAR2(60 CHAR) NOT NULL, 
     Descripcion VARCHAR2(200 CHAR), 
     Tipo        VARCHAR2(60 CHAR) NOT NULL
    );

-- Tabla ORDEN DE SERVICIO
CREATE TABLE OrdenDeServicio 
    ( 
     ID_Orden                 NUMBER(20,0) NOT NULL, 
     Fecha_hora               TIMESTAMP NOT NULL, 
     EstadoOrden              VARCHAR2(20 CHAR) NOT NULL, 
     Medico_NumeroDocumento   NUMBER(20,0) NOT NULL, 
     Afiliado_NumeroDocumento NUMBER(20,0) NOT NULL 
    );

-- Tabla AGENDAR CITA
CREATE TABLE AgendarCita 
    ( 
     IDCita                   NUMBER(20,0) NOT NULL, 
     Fecha_hora               TIMESTAMP NOT NULL, 
     Afiliado_NumeroDocumento NUMBER(20,0) NOT NULL, 
     Medico_NumeroDocumento   NUMBER(20,0) NOT NULL, 
     ID_OrdenDeServicio       NUMBER(20,0), 
     ServicioDeSalud_ID       NUMBER(20,0) NOT NULL 
    );

-- Tabla PRESTACIÓN SERVICIO
CREATE TABLE PrestacionServicio 
    ( 
     IPS_NIT            NUMBER(20,0) NOT NULL, 
     AgendarCita_IDCita NUMBER(20,0) NOT NULL, 
     CitaRealizada      NUMBER(1) DEFAULT 0 
    );

-- Tabla SERVICIO ORDEN (Relación muchos a muchos)
CREATE TABLE ServicioOrden 
    ( 
     ServicioDeSalud_ID_Servicio NUMBER(20,0) NOT NULL, 
     OrdenDeServicio_ID_Orden    NUMBER(20,0) NOT NULL 
    );

-- Tabla SERVICIOS IPS (Relación muchos a muchos)
CREATE TABLE ServiciosIPS 
    ( 
     IPS_NIT     NUMBER(20,0) NOT NULL, 
     Servicio_ID NUMBER(20,0) NOT NULL 
    );

-- Tabla SERVICIOS MEDICO (Relación muchos a muchos)
CREATE TABLE ServiciosMedico 
    ( 
     Medico_NumeroDocumento NUMBER(20,0) NOT NULL, 
     Servicio_ID            NUMBER(20,0) NOT NULL 
    );

-- =======================================================================
-- PASO 3: CREAR RESTRICCIONES DE LLAVE PRIMARIA
-- =======================================================================

ALTER TABLE Afiliado 
    ADD CONSTRAINT Afiliado_PK PRIMARY KEY (NumeroDocumento);

ALTER TABLE IPS 
    ADD CONSTRAINT IPS_PK PRIMARY KEY (NIT);

ALTER TABLE Medico 
    ADD CONSTRAINT Medico_PK PRIMARY KEY (NumeroDocumento);

ALTER TABLE ServicioDeSalud 
    ADD CONSTRAINT ServicioDeSalud_PK PRIMARY KEY (ID_Servicio);

ALTER TABLE OrdenDeServicio 
    ADD CONSTRAINT OrdenDeServicio_PK PRIMARY KEY (ID_Orden);

ALTER TABLE AgendarCita 
    ADD CONSTRAINT AgendarCita_PK PRIMARY KEY (IDCita);

ALTER TABLE PrestacionServicio 
    ADD CONSTRAINT PrestacionServicio_PK PRIMARY KEY (IPS_NIT, AgendarCita_IDCita);

ALTER TABLE ServicioOrden 
    ADD CONSTRAINT ServicioOrden_PK PRIMARY KEY (ServicioDeSalud_ID_Servicio, OrdenDeServicio_ID_Orden);

ALTER TABLE ServiciosIPS 
    ADD CONSTRAINT ServiciosIPS_PK PRIMARY KEY (IPS_NIT, Servicio_ID);

ALTER TABLE ServiciosMedico 
    ADD CONSTRAINT ServiciosMedico_PK PRIMARY KEY (Medico_NumeroDocumento, Servicio_ID);

-- =======================================================================
-- PASO 4: CREAR RESTRICCIONES UNIQUE
-- =======================================================================

ALTER TABLE Medico 
    ADD CONSTRAINT UQ_RegistroMedico UNIQUE (NumeroRegistroMedico);

ALTER TABLE AgendarCita 
    ADD CONSTRAINT UQ_AgendarCita_Afiliado UNIQUE (Afiliado_NumeroDocumento, Fecha_hora);

ALTER TABLE PrestacionServicio 
    ADD CONSTRAINT UQ_PrestacionServicio_Cita UNIQUE (AgendarCita_IDCita);

-- =======================================================================
-- PASO 5: CREAR RESTRICCIONES CHECK
-- =======================================================================

ALTER TABLE Afiliado 
    ADD CONSTRAINT CHK_TipoAfiliado 
    CHECK (TipoAfiliado IN ('CONTRIBUYENTE', 'BENEFICIARIO'));

ALTER TABLE Afiliado 
    ADD CONSTRAINT CHK_TipoDocumento 
    CHECK (TipoDocumento IN ('CC', 'TI', 'CE', 'PA'));

ALTER TABLE OrdenDeServicio 
    ADD CONSTRAINT CHK_EstadoOrden 
    CHECK (EstadoOrden IN ('VIGENTE', 'CANCELADA', 'COMPLETADA'));

ALTER TABLE ServicioDeSalud 
    ADD CONSTRAINT CHK_TipoServicio 
    CHECK (Tipo IN ('CONSULTA_GENERAL', 'CONSULTA_ESPECIALISTA', 'CONSULTA_CONTROL', 
                    'CONSULTA_URGENCIAS', 'EXAMEN_DIAGNOSTICO', 'TERAPIA', 
                    'PROCEDIMIENTO_MEDICO', 'HOSPITALIZACION'));

ALTER TABLE PrestacionServicio 
    ADD CONSTRAINT CHK_CitaRealizada 
    CHECK (CitaRealizada IN (0, 1));

-- =======================================================================
-- PASO 6: CREAR RESTRICCIONES DE LLAVE FORÁNEA
-- =======================================================================

-- Afiliado
ALTER TABLE Afiliado 
    ADD CONSTRAINT Afiliado_Afiliado_FK FOREIGN KEY (NumeroDocumentoContribuyente) 
    REFERENCES Afiliado (NumeroDocumento) 
    NOT DEFERRABLE;

-- Medico
ALTER TABLE Medico 
    ADD CONSTRAINT Medico_IPS_FK FOREIGN KEY (IPS_NIT) 
    REFERENCES IPS (NIT) 
    NOT DEFERRABLE;

-- OrdenDeServicio
ALTER TABLE OrdenDeServicio 
    ADD CONSTRAINT OrdenDeServicio_Afiliado_FK FOREIGN KEY (Afiliado_NumeroDocumento) 
    REFERENCES Afiliado (NumeroDocumento) 
    NOT DEFERRABLE;

ALTER TABLE OrdenDeServicio 
    ADD CONSTRAINT OrdenDeServicio_Medico_FK FOREIGN KEY (Medico_NumeroDocumento) 
    REFERENCES Medico (NumeroDocumento) 
    NOT DEFERRABLE;

-- AgendarCita
ALTER TABLE AgendarCita 
    ADD CONSTRAINT AgendarCita_Afiliado_FK FOREIGN KEY (Afiliado_NumeroDocumento) 
    REFERENCES Afiliado (NumeroDocumento) 
    NOT DEFERRABLE;

ALTER TABLE AgendarCita 
    ADD CONSTRAINT AgendarCita_Medico_FK FOREIGN KEY (Medico_NumeroDocumento) 
    REFERENCES Medico (NumeroDocumento) 
    NOT DEFERRABLE;

ALTER TABLE AgendarCita 
    ADD CONSTRAINT AgendarCita_OrdenDeServicio_FK FOREIGN KEY (ID_OrdenDeServicio) 
    REFERENCES OrdenDeServicio (ID_Orden) 
    NOT DEFERRABLE;

ALTER TABLE AgendarCita 
    ADD CONSTRAINT AgendarCita_ServicioDeSalud_FK FOREIGN KEY (ServicioDeSalud_ID) 
    REFERENCES ServicioDeSalud (ID_Servicio) 
    NOT DEFERRABLE;

-- PrestacionServicio
ALTER TABLE PrestacionServicio 
    ADD CONSTRAINT PrestacionServicio_AgendarCita_FK FOREIGN KEY (AgendarCita_IDCita) 
    REFERENCES AgendarCita (IDCita) 
    NOT DEFERRABLE;

ALTER TABLE PrestacionServicio 
    ADD CONSTRAINT PrestacionServicio_IPS_FK FOREIGN KEY (IPS_NIT) 
    REFERENCES IPS (NIT) 
    NOT DEFERRABLE;

-- ServicioOrden
ALTER TABLE ServicioOrden 
    ADD CONSTRAINT ServicioOrden_OrdenDeServicio_FK FOREIGN KEY (OrdenDeServicio_ID_Orden) 
    REFERENCES OrdenDeServicio (ID_Orden) 
    NOT DEFERRABLE;

ALTER TABLE ServicioOrden 
    ADD CONSTRAINT ServicioOrden_ServicioDeSalud_FK FOREIGN KEY (ServicioDeSalud_ID_Servicio) 
    REFERENCES ServicioDeSalud (ID_Servicio) 
    NOT DEFERRABLE;

-- ServiciosIPS
ALTER TABLE ServiciosIPS 
    ADD CONSTRAINT ServiciosIPS_IPS_FK FOREIGN KEY (IPS_NIT) 
    REFERENCES IPS (NIT) 
    NOT DEFERRABLE;

ALTER TABLE ServiciosIPS 
    ADD CONSTRAINT ServiciosIPS_ServicioDeSalud_FK FOREIGN KEY (Servicio_ID) 
    REFERENCES ServicioDeSalud (ID_Servicio) 
    NOT DEFERRABLE;

-- ServiciosMedico
ALTER TABLE ServiciosMedico 
    ADD CONSTRAINT ServiciosMedico_Medico_FK FOREIGN KEY (Medico_NumeroDocumento) 
    REFERENCES Medico (NumeroDocumento) 
    NOT DEFERRABLE;

ALTER TABLE ServiciosMedico 
    ADD CONSTRAINT ServiciosMedico_ServicioDeSalud_FK FOREIGN KEY (Servicio_ID) 
    REFERENCES ServicioDeSalud (ID_Servicio) 
    NOT DEFERRABLE;

-- =======================================================================
-- PASO 7: CREAR ÍNDICES
-- =======================================================================

-- Índices para mejorar el rendimiento de las consultas frecuentes
CREATE INDEX IDX_Afiliado_Contribuyente ON Afiliado (NumeroDocumentoContribuyente);
CREATE INDEX IDX_Medico_IPS ON Medico (IPS_NIT);
CREATE INDEX IDX_OrdenDeServicio_Afiliado ON OrdenDeServicio (Afiliado_NumeroDocumento);
CREATE INDEX IDX_OrdenDeServicio_Medico ON OrdenDeServicio (Medico_NumeroDocumento);
CREATE INDEX IDX_AgendarCita_Afiliado ON AgendarCita (Afiliado_NumeroDocumento);
CREATE INDEX IDX_AgendarCita_Medico ON AgendarCita (Medico_NumeroDocumento);
CREATE INDEX IDX_AgendarCita_Servicio ON AgendarCita (ServicioDeSalud_ID);
CREATE INDEX IDX_AgendarCita_Orden ON AgendarCita (ID_OrdenDeServicio);
CREATE INDEX IDX_PrestacionServicio_IPS ON PrestacionServicio (IPS_NIT);
-- El siguiente índice no es necesario porque ya existe uno creado por la restricción UNIQUE
-- CREATE INDEX IDX_PrestacionServicio_Cita ON PrestacionServicio (AgendarCita_IDCita);
CREATE INDEX IDX_ServicioOrden_Servicio ON ServicioOrden (ServicioDeSalud_ID_Servicio);
CREATE INDEX IDX_ServicioOrden_Orden ON ServicioOrden (OrdenDeServicio_ID_Orden);
CREATE INDEX IDX_ServiciosIPS_IPS ON ServiciosIPS (IPS_NIT);
CREATE INDEX IDX_ServiciosIPS_Servicio ON ServiciosIPS (Servicio_ID);
CREATE INDEX IDX_ServiciosMedico_Medico ON ServiciosMedico (Medico_NumeroDocumento);
CREATE INDEX IDX_ServiciosMedico_Servicio ON ServiciosMedico (Servicio_ID);

-- =======================================================================
-- FIN DEL SCRIPT DE CREACIÓN
-- =======================================================================