
package Ejercicio2.Controlador;

import Ejercicio2.Modelo.Clases.Estudiante;
import Ejercicio2.Modelo.Clases.Profesor;
import Ejercicio2.Modelo.Persistencia.ControladoraPersistencia;
import Ejercicio2.Modelo.Persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;


public class ProfesorController {
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    
    //Create
    public void addProfesor(Profesor profe) {
        controlPersis.addProfesor(profe);
    }

    //Read
    public Profesor findProfesor(int id) {
        return controlPersis.findProfesor(id);
    }

    public ArrayList<Profesor> findAllProfesores() {
        return controlPersis.findAllProfesores();
    }

    //Update
    public void editProfesor(Profesor profe) throws Exception {
        controlPersis.editProfesor(profe);
    }

    //Delete
    public void destroyProfesor(int id) throws NonexistentEntityException {
        controlPersis.destroyProfesor(id);
    }
}
