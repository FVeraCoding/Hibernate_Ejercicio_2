
package Ejercicio2.Controlador;

import Ejercicio2.Modelo.Clases.Curso;
import Ejercicio2.Modelo.Clases.Estudiante;
import Ejercicio2.Modelo.Persistencia.ControladoraPersistencia;
import Ejercicio2.Modelo.Persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;


public class CursoController {
        ControladoraPersistencia controlPersis = new ControladoraPersistencia();

    //Create
    public void addCurso(Curso curso) {
        controlPersis.addCurso(curso);
    }

    //Read
    public Curso findCurso(int id) {
        return controlPersis.findCurso(id);
    }

    public ArrayList<Curso> findAllCursos() {
        return controlPersis.findAllCursos();
    }

    //Update
    public void editCurso(Curso curso) throws Exception {
        controlPersis.editCurso(curso);
    }

    //Delete
    public void destroyCurso(int id) throws NonexistentEntityException {
        controlPersis.destroyCurso(id);
    }
}
