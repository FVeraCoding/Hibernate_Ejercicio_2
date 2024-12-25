package Ejercicio2.Modelo.Clases;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Profesor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Basic
    private String nombre;
    private String departamento;

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL)
    List<Curso> listaCursos = new ArrayList<Curso>();

    public Profesor() {
    }

    public Profesor(String nombre, String departamento) {
        this.nombre = nombre;
        this.departamento = departamento;
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

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<Curso> getListaCursos() {
        return listaCursos;
    }

    public void setListaCursos(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }
    
    public void agregarCurso(Curso curso){
        if(!this.listaCursos.contains(curso)){
            this.listaCursos.add(curso);
            
            curso.setProfesor(this);
        }
    }

}
