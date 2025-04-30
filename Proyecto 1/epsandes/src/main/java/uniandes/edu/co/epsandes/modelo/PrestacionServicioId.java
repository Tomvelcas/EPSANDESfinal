package uniandes.edu.co.epsandes.modelo;

import java.io.Serializable;
import java.util.Objects;

public class PrestacionServicioId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long ips; // Must match the ID type of IPS entity
    private Long agendarCita; // Must match the ID type of AgendarCita entity
    
    // Constructor vacío
    public PrestacionServicioId() {}
    
    // Constructor con parámetros
    public PrestacionServicioId(Long ips, Long agendarCita) {
        this.ips = ips;
        this.agendarCita = agendarCita;
    }
    
    // Getters y Setters
    public Long getIps() {
        return ips;
    }
    
    public void setIps(Long ips) {
        this.ips = ips;
    }
    
    public Long getAgendarCita() {
        return agendarCita;
    }
    
    public void setAgendarCita(Long agendarCita) {
        this.agendarCita = agendarCita;
    }
    
    // equals y hashCode son importantes para llaves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrestacionServicioId that = (PrestacionServicioId) o;
        return Objects.equals(ips, that.ips) && 
               Objects.equals(agendarCita, that.agendarCita);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ips, agendarCita);
    }
}