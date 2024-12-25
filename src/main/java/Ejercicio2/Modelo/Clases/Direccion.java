
package Ejercicio2.Modelo.Clases;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Direccion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    private String calle;
    private String ciudad;
    private int codigoPostal;
    
    @OneToOne(mappedBy="direccion", cascade = CascadeType.ALL)
    private Estudiante estudiante;
    
    public Direccion() {
    }

    public Direccion(String calle, String ciudad, int codigoPostal) {
        this.calle = calle;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    
    
    public void agregarEstudiante(Estudiante estudiante){
        this.estudiante = estudiante;
        estudiante.setDireccion(this);
    }
    
    
}
