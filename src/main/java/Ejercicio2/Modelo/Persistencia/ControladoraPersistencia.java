
package Ejercicio2.Modelo.Persistencia;

import Ejercicio2.Modelo.Clases.Curso;
import Ejercicio2.Modelo.Clases.Direccion;
import Ejercicio2.Modelo.Clases.Estudiante;
import Ejercicio2.Modelo.Clases.Profesor;
import Ejercicio2.Modelo.Persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;

public class ControladoraPersistencia {
    CursoJpaController cursoController = new CursoJpaController();
    DireccionJpaController direController = new DireccionJpaController();
    EstudianteJpaController estController = new EstudianteJpaController();
    ProfesorJpaController proController = new ProfesorJpaController();

    
    //Direccion
    public void addDireccion(Direccion direccion) {
        direController.create(direccion);
    }

    public Direccion findDireccion(int id) {
        return direController.findDireccion(id);
    }

    public ArrayList<Direccion> findAllDirecciones() {
        List<Direccion> lista = direController.findDireccionEntities();
        ArrayList<Direccion> listaDirecciones = new ArrayList<Direccion>(lista);
        return listaDirecciones;
    }

    public void editDireccion(Direccion direccion) throws Exception {
        direController.edit(direccion);
    }

    public void destroyDireccion(int id) throws NonexistentEntityException {
        direController.destroy(id);
    }
    
    //Estudiante

    public void addEstudiante(Estudiante estudiante) {
        estController.create(estudiante);
    }

    public Estudiante findEstudiante(int id) {
        return estController.findEstudiante(id);
    }

    public ArrayList<Estudiante> findAllEstudiantes() {
        List<Estudiante> lista = estController.findEstudianteEntities();
        ArrayList<Estudiante> listaEstudiantes = new ArrayList<Estudiante>(lista);
        return listaEstudiantes;
    }

    public void editEstudiante(Estudiante estudiante) throws Exception {
        estController.edit(estudiante);
    }
    
    public void destroyEstudiante(int id) throws NonexistentEntityException{
        estController.destroy(id);
    }

    
    //PROFESOR
    public void addProfesor(Profesor profe) {
        proController.create(profe);
    }

    public Profesor findProfesor(int id) {
        return proController.findProfesor(id);
    }

    public ArrayList<Profesor> findAllProfesores() {
        List<Profesor> lista = proController.findProfesorEntities();
        ArrayList<Profesor> listaProfesores = new ArrayList<Profesor>(lista);
        return listaProfesores;
    }

    public void editProfesor(Profesor profe) throws Exception {
        proController.edit(profe);
    }

    public void destroyProfesor(int id) throws NonexistentEntityException {
        proController.destroy(id);
    }
    
    //CURSO

    public void addCurso(Curso curso) {
        cursoController.create(curso);
    }

    public Curso findCurso(int id) {
        return cursoController.findCurso(id);
    }

    public ArrayList<Curso> findAllCursos() {
        List<Curso> lista = cursoController.findCursoEntities();
        ArrayList<Curso> listaCursos = new ArrayList<Curso>(lista);
        return listaCursos;
    }

    public void editCurso(Curso curso) throws Exception {
        cursoController.edit(curso);
    }

    public void destroyCurso(int id) throws NonexistentEntityException {
        cursoController.destroy(id);
    }
    
    
    
    
}
