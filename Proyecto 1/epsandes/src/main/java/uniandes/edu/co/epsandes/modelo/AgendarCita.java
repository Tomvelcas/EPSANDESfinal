package uniandes.edu.co.epsandes.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "AGENDARCITA")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AgendarCita {
    
    @Id
    @Column(name = "IDCITA")
    private Long idCita;
    
    @Column(name = "FECHA_HORA", nullable = false)
    private LocalDateTime fechaHora;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AFILIADO_NUMERODOCUMENTO", nullable = false)
    @JsonIgnoreProperties({"beneficiarios", "contribuyente", "citas", "ordenes"})
    private Afiliado afiliado;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEDICO_NUMERODOCUMENTO", nullable = false)
    @JsonIgnoreProperties({"servicios", "citas", "ordenes", "ips"})
    private Medico medico;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ORDENDESERVICIO")
    @JsonIgnoreProperties({"citas", "servicios", "medico", "afiliado"})
    private OrdenDeServicio ordenDeServicio;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SERVICIODESALUD_ID", nullable = false)
    @JsonIgnoreProperties({"ipsSet", "medicos", "ordenes", "citas"})
    private ServicioDeSalud servicioDeSalud;
    
    @OneToOne(mappedBy = "agendarCita")
    @JsonIgnoreProperties("agendarCita")
    private PrestacionServicio prestacionServicio;

    // Constructor vacío
    public AgendarCita() {}

    // Constructor con parámetros
    public AgendarCita(Long idCita, LocalDateTime fechaHora, Afiliado afiliado, 
                      Medico medico, OrdenDeServicio ordenDeServicio, 
                      ServicioDeSalud servicioDeSalud) {
        this.idCita = idCita;
        this.fechaHora = fechaHora;
        this.afiliado = afiliado;
        this.medico = medico;
        this.ordenDeServicio = ordenDeServicio;
        this.servicioDeSalud = servicioDeSalud;
    }

    // Getters y Setters
    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Afiliado getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public OrdenDeServicio getOrdenDeServicio() {
        return ordenDeServicio;
    }

    public void setOrdenDeServicio(OrdenDeServicio ordenDeServicio) {
        this.ordenDeServicio = ordenDeServicio;
    }

    public ServicioDeSalud getServicioDeSalud() {
        return servicioDeSalud;
    }

    public void setServicioDeSalud(ServicioDeSalud servicioDeSalud) {
        this.servicioDeSalud = servicioDeSalud;
    }

    public PrestacionServicio getPrestacionServicio() {
        return prestacionServicio;
    }

    public void setPrestacionServicio(PrestacionServicio prestacionServicio) {
        this.prestacionServicio = prestacionServicio;
    }
}