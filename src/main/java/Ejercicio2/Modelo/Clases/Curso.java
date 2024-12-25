package Ejercicio2.Modelo.Clases;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Curso implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    
    @Basic
    private String nombre;
    private int creditos;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="profesor_id")
    private Profesor profesor;
    
    @ManyToMany(mappedBy="listaCursos")
    private List<Estudiante> listaEstudiantes = new ArrayList<>();

    public Curso() {
    }

    public Curso(String nombre, int creditos) {
        this.nombre = nombre;
        this.creditos = creditos;
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

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
    
        public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public List<Estudiante> getListaEstudiantes() {
        return listaEstudiantes;
    }

    public void setListaEstudiantes(List<Estudiante> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }
    
    public void agregarEstudiante(Estudiante estudiante){
        if(!this.getListaEstudiantes().contains(estudiante)){
            this.listaEstudiantes.add(estudiante);
            
            if(!estudiante.getListaCursos().contains(this)){
                estudiante.getListaCursos().add(this);
            }
        }
    }
    
    public void agregarProfesor(Profesor profesor){
        this.setProfesor(profesor);
        
        if(!profesor.getListaCursos().contains(this)){
            profesor.getListaCursos().add(this);
        }
    }
    
}
