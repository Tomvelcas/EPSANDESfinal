package uniandes.edu.co.epsandes.modelo;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "MEDICO")
public class Medico {
    
    @Id
    @Column(name = "NUMERODOCUMENTO")
    private Long numeroDocumento;
    
    @Column(name = "NOMBRE", length = 60, nullable = false)
    private String nombre;
    
    @Column(name = "TIPODOCUMENTO", length = 20, nullable = false)
    private String tipoDocumento;
    
    @Column(name = "NUMEROREGISTROMEDICO", unique = true, nullable = false)
    private Long numeroRegistroMedico;
    
    @Column(name = "ESPECIALIDAD", length = 60, nullable = false)
    private String especialidad;
    
    @ManyToOne
    @JoinColumn(name = "IPS_NIT", nullable = false)
    @JsonIgnoreProperties(value = {"medicos", "prestaciones"}, allowSetters = true)
    private IPS ips;
    
    @ManyToMany
    @JoinTable(
        name = "SERVICIOSMEDICO",
        joinColumns = @JoinColumn(name = "MEDICO_NUMERODOCUMENTO"),
        inverseJoinColumns = @JoinColumn(name = "SERVICIO_ID")
    )
    @JsonIgnore // Evita problemas de lazy loading
    private Set<ServicioDeSalud> servicios = new HashSet<>();
    
    @OneToMany(mappedBy = "medico")
    @JsonIgnore
    private Set<AgendarCita> citas = new HashSet<>();
    
    @OneToMany(mappedBy = "medico")
    @JsonIgnore
    private Set<OrdenDeServicio> ordenes = new HashSet<>();

    // Constructor vacío
    public Medico() {}

    // Constructor con parámetros
    public Medico(Long numeroDocumento, String nombre, String tipoDocumento, 
                 Long numeroRegistroMedico, String especialidad, IPS ips) {
        this.numeroDocumento = numeroDocumento;
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.numeroRegistroMedico = numeroRegistroMedico;
        this.especialidad = especialidad;
        this.ips = ips;
    }

    // Getters y Setters
    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getNumeroRegistroMedico() {
        return numeroRegistroMedico;
    }

    public void setNumeroRegistroMedico(Long numeroRegistroMedico) {
        this.numeroRegistroMedico = numeroRegistroMedico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public IPS getIps() {
        return ips;
    }

    public void setIps(IPS ips) {
        this.ips = ips;
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

    public Set<OrdenDeServicio> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(Set<OrdenDeServicio> ordenes) {
        this.ordenes = ordenes;
    }
}