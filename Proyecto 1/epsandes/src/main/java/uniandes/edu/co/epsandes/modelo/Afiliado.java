package uniandes.edu.co.epsandes.modelo;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AFILIADO")
public class Afiliado {
    
    @Id
    @Column(name = "NUMERODOCUMENTO")
    private Long numeroDocumento;
    
    @Column(name = "TIPODOCUMENTO", length = 20)
    private String tipoDocumento;
    
    @Column(name = "NOMBRE", length = 60, nullable = false)
    private String nombre;
    
    @Column(name = "FECHANACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;
    
    @Column(name = "DIRECCION", length = 60)
    private String direccion;
    
    @Column(name = "TELEFONO", length = 20)
    private String telefono;
    
    @Column(name = "TIPOAFILIADO", length = 20, nullable = false)
    private String tipoAfiliado;
    
    @Column(name = "NUMERODOCUMENTOCONTRIBUYENTE")
    private Long numeroDocumentoContribuyente;
    
    @Column(name = "PARENTESCO", length = 20)
    private String parentesco;
    
    @ManyToOne
    @JoinColumn(name = "NUMERODOCUMENTOCONTRIBUYENTE", insertable = false, updatable = false)
    private Afiliado contribuyente;
    
    @OneToMany(mappedBy = "contribuyente")
    private Set<Afiliado> beneficiarios = new HashSet<>();
    
    @OneToMany(mappedBy = "afiliado")
    private Set<AgendarCita> citas = new HashSet<>();
    
    @OneToMany(mappedBy = "afiliado")
    private Set<OrdenDeServicio> ordenes = new HashSet<>();

    // Constructor vacío
    public Afiliado() {}

    // Constructor con parámetros
    public Afiliado(Long numeroDocumento, String tipoDocumento, String nombre, 
                   LocalDate fechaNacimiento, String direccion, String telefono, 
                   String tipoAfiliado, Long numeroDocumentoContribuyente, String parentesco) {
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoAfiliado = tipoAfiliado;
        this.numeroDocumentoContribuyente = numeroDocumentoContribuyente;
        this.parentesco = parentesco;
    }

    // Getters y Setters
    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public String getTipoAfiliado() {
        return tipoAfiliado;
    }

    public void setTipoAfiliado(String tipoAfiliado) {
        this.tipoAfiliado = tipoAfiliado;
    }

    public Long getNumeroDocumentoContribuyente() {
        return numeroDocumentoContribuyente;
    }

    public void setNumeroDocumentoContribuyente(Long numeroDocumentoContribuyente) {
        this.numeroDocumentoContribuyente = numeroDocumentoContribuyente;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public Afiliado getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Afiliado contribuyente) {
        this.contribuyente = contribuyente;
    }

    public Set<Afiliado> getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(Set<Afiliado> beneficiarios) {
        this.beneficiarios = beneficiarios;
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