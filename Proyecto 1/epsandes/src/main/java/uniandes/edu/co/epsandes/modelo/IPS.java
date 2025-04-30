package uniandes.edu.co.epsandes.modelo;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "IPS")
public class IPS {
    
    @Id
    @Column(name = "NIT")
    private Long nit;
    
    @Column(name = "NOMBRE", length = 60, nullable = false)
    private String nombre;
    
    @Column(name = "DIRECCION", length = 60, nullable = false)
    private String direccion;
    
    @Column(name = "TELEFONO", length = 20, nullable = false)
    private String telefono;
    
    @OneToMany(mappedBy = "ips", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("ips")
    private Set<Medico> medicos = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "SERVICIOSIPS",
        joinColumns = @JoinColumn(name = "IPS_NIT"),
        inverseJoinColumns = @JoinColumn(name = "SERVICIO_ID")
    )
    @JsonIgnoreProperties({"ipsSet", "medicos", "ordenes", "citas"})
    private Set<ServicioDeSalud> servicios = new HashSet<>();
    
    @OneToMany(mappedBy = "ips", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"ips", "agendarCita"})
    private Set<PrestacionServicio> prestaciones = new HashSet<>();

    // Constructor vacío
    public IPS() {}

    // Constructor con parámetros
    public IPS(Long nit, String nombre, String direccion, String telefono) {
        this.nit = nit;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getNit() {
        return nit;
    }

    public void setNit(Long nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(Set<Medico> medicos) {
        this.medicos = medicos;
    }

    public Set<ServicioDeSalud> getServicios() {
        return servicios;
    }

    public void setServicios(Set<ServicioDeSalud> servicios) {
        this.servicios = servicios;
    }

    public Set<PrestacionServicio> getPrestaciones() {
        return prestaciones;
    }

    public void setPrestaciones(Set<PrestacionServicio> prestaciones) {
        this.prestaciones = prestaciones;
    }
}