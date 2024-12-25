
package Ejercicio2.Modelo.Clases;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Estudiante implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Basic
    private String nombre;
    private String email;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id")
    private Direccion direccion;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Estudiante_Curso",
            joinColumns = @JoinColumn(name="estudiante_id"),
            inverseJoinColumns = @JoinColumn(name="curso_id"))
    private List<Curso> listaCursos = new ArrayList<Curso>();

    public Estudiante() {
    }

    public Estudiante(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public List<Curso> getListaCursos() {
        return listaCursos;
    }

    public void setListaCursos(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }
    
    public void agregarDireccion(Direccion direccion){
        this.direccion = direccion;
        direccion.setEstudiante(this);
    }
    
    public void agregarCurso(Curso curso){
        if(!this.listaCursos.contains(curso)){
            this.listaCursos.add(curso);
            
            if(!curso.getListaEstudiantes().contains(this)){
                curso.getListaEstudiantes().add(this);
            }
        }
    }
    
}
