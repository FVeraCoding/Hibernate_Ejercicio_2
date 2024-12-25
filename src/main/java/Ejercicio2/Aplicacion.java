package Ejercicio2;

import Ejercicio2.Controlador.CursoController;
import Ejercicio2.Controlador.DireccionController;
import Ejercicio2.Controlador.EstudianteController;
import Ejercicio2.Controlador.ProfesorController;
import Ejercicio2.Modelo.Clases.Curso;
import Ejercicio2.Modelo.Clases.Direccion;
import Ejercicio2.Modelo.Clases.Estudiante;
import Ejercicio2.Modelo.Clases.Profesor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aplicacion {

    public static void main(String[] args) {
        try {
            DireccionController direControl = new DireccionController();
            EstudianteController estuControl = new EstudianteController();
            ProfesorController profeControl = new ProfesorController();
            CursoController cursoControl = new CursoController();

            Direccion direccion = new Direccion("C/ Falsa 123", "Gijón", 33201);
            direControl.addDireccion(direccion);
            
            Direccion direccion2 = new Direccion("Avenida Real 45", "Oviedo", 33002);
            direControl.addDireccion(direccion2);

            Estudiante estudiante = new Estudiante("Alice", "alice@gmail.com");
            estuControl.addEstudiante(estudiante);
            
            Estudiante estudiante2 = new Estudiante("Bob", "bob@correo.com");
            estuControl.addEstudiante(estudiante2);

            Profesor profesor = new Profesor("Dr. Smith", "Matemáticas");
            profeControl.addProfesor(profesor);
            
            Profesor profesor2 = new Profesor("Dr. Johnson", "Física");
            profeControl.addProfesor(profesor2);
            
            Curso curso = new Curso("Álgebra", 5);
            cursoControl.addCurso(curso);
            
            Curso curso2 = new Curso("Física I", 4);
            cursoControl.addCurso(curso2);
            
            estudiante.agregarDireccion(direccion);
            estudiante.agregarCurso(curso);
            profesor.agregarCurso(curso);
            
            estudiante2.agregarDireccion(direccion2);
            estudiante2.agregarCurso(curso2);
            profesor2.agregarCurso(curso2);

            
            estuControl.editEstudiante(estudiante);
            profeControl.editProfesor(profesor);
            cursoControl.editCurso(curso);
            
            estuControl.editEstudiante(estudiante2);
            profeControl.editProfesor(profesor2);
            cursoControl.editCurso(curso2);
            
        } catch (Exception ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
