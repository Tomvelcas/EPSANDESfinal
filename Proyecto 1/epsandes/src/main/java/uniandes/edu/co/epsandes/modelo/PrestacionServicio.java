package uniandes.edu.co.epsandes.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PRESTACIONSERVICIO")
@IdClass(PrestacionServicioId.class)
public class PrestacionServicio implements Serializable {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "IPS_NIT")
    private IPS ips;
    
    @Id
    @OneToOne
    @JoinColumn(name = "AGENDARCITA_IDCITA")
    private AgendarCita agendarCita;
    
    @Column(name = "CITAREALIZADA", nullable = false)
    private Boolean citaRealizada;

    // Constructor vacío
    public PrestacionServicio() {
        this.citaRealizada = false;
    }

    // Constructor con parámetros
    public PrestacionServicio(IPS ips, AgendarCita agendarCita, Boolean citaRealizada) {
        this.ips = ips;
        this.agendarCita = agendarCita;
        this.citaRealizada = citaRealizada;
    }

    // Getters y Setters
    public IPS getIps() {
        return ips;
    }

    public void setIps(IPS ips) {
        this.ips = ips;
    }

    public AgendarCita getAgendarCita() {
        return agendarCita;
    }

    public void setAgendarCita(AgendarCita agendarCita) {
        this.agendarCita = agendarCita;
    }

    public Boolean getCitaRealizada() {
        return citaRealizada;
    }

    public void setCitaRealizada(Boolean citaRealizada) {
        this.citaRealizada = citaRealizada;
    }
}

