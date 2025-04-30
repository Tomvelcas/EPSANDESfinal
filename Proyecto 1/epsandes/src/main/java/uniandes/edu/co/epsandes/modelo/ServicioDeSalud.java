package uniandes.edu.co.epsandes.modelo;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "SERVICIODESALUD")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ServicioDeSalud {
    
    @Id
    @Column(name = "ID_SERVICIO")
    private Long idServicio;
    
    @Column(name = "NOMBRE", length = 60, nullable = false)
    private String nombre;
    
    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;
    
    @Column(name = "TIPO", length = 60, nullable = false)
    private String tipo;
    
    @ManyToMany(mappedBy = "servicios")
    @JsonIgnore
    private Set<IPS> ipsSet = new HashSet<>();
    
    @ManyToMany(mappedBy = "servicios")
    @JsonIgnore
    private Set<Medico> medicos = new HashSet<>();
    
    @ManyToMany(mappedBy = "servicios")
    @JsonIgnore
    private Set<OrdenDeServicio> ordenes = new HashSet<>();
    
    @OneToMany(mappedBy = "servicioDeSalud")
    @JsonIgnore
    private Set<AgendarCita> citas = new HashSet<>();

    // Constructor vacío
    public ServicioDeSalud() {}

    // Constructor con parámetros
    public ServicioDeSalud(Long idServicio, String nombre, String descripcion, String tipo) {
        this.idServicio = idServicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Set<IPS> getIpsSet() {
        return ipsSet;
    }

    public void setIpsSet(Set<IPS> ipsSet) {
        this.ipsSet = ipsSet;
    }

    public Set<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(Set<Medico> medicos) {
        this.medicos = medicos;
    }

    public Set<OrdenDeServicio> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(Set<OrdenDeServicio> ordenes) {
        this.ordenes = ordenes;
    }

    public Set<AgendarCita> getCitas() {
        return citas;
    }

    public void setCitas(Set<AgendarCita> citas) {
        this.citas = citas;
    }
}