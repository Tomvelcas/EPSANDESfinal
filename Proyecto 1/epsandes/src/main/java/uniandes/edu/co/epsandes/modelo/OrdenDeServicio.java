package uniandes.edu.co.epsandes.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ORDENDESERVICIO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrdenDeServicio {
    
    @Id
    @Column(name = "ID_ORDEN")
    private Long idOrden;
    
    @Column(name = "FECHA_HORA", nullable = false)
    private LocalDateTime fechaHora;
    
    @Column(name = "ESTADOORDEN", length = 20, nullable = false)
    private String estadoOrden;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEDICO_NUMERODOCUMENTO", nullable = false)
    @JsonIgnoreProperties({"ordenes", "citas", "servicios", "ips"})
    private Medico medico;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AFILIADO_NUMERODOCUMENTO", nullable = false)
    @JsonIgnoreProperties({"ordenes", "citas", "contribuyente", "beneficiarios"})
    private Afiliado afiliado;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"ordenes", "medicos", "ipsSet", "citas"})
    @JoinTable(
        name = "SERVICIORDEN",
        joinColumns = @JoinColumn(name = "ORDENDESERVICIO_ID_ORDEN"),
        inverseJoinColumns = @JoinColumn(name = "SERVICIODESALUD_ID_SERVICIO")
    )
    private Set<ServicioDeSalud> servicios = new HashSet<>();
    
    @OneToMany(mappedBy = "ordenDeServicio", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("ordenDeServicio")
    private Set<AgendarCita> citas = new HashSet<>();

    // Constructor vacío
    public OrdenDeServicio() {}

    // Constructor con parámetros
    public OrdenDeServicio(Long idOrden, LocalDateTime fechaHora, String estadoOrden, 
                          Medico medico, Afiliado afiliado) {
        this.idOrden = idOrden;
        this.fechaHora = fechaHora;
        this.estadoOrden = estadoOrden;
        this.medico = medico;
        this.afiliado = afiliado;
    }

    // Getters y Setters
    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Afiliado getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }

    public Set<ServicioDeSalud> getServicios() {
        return servicios;
    }

    public void setServicios(Set<ServicioDeSalud> servicios) {
        this.servicios = servicios;
    }

    public Set<AgendarCita> getCitas() {
        return citas;
    }

    public void setCitas(Set<AgendarCita> citas) {
        this.citas = citas;
    }
}