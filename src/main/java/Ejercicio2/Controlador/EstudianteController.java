
package Ejercicio2.Controlador;

import Ejercicio2.Modelo.Clases.Estudiante;
import Ejercicio2.Modelo.Persistencia.ControladoraPersistencia;
import Ejercicio2.Modelo.Persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;


public class EstudianteController {
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();

    //Create
    public void addEstudiante(Estudiante estudiante) {
        controlPersis.addEstudiante(estudiante);
    }

    //Read
    public Estudiante findEstudiante(int id) {
        return controlPersis.findEstudiante(id);
    }

    public ArrayList<Estudiante> findAllEstudiantes() {
        return controlPersis.findAllEstudiantes();
    }

    //Update
    public void editEstudiante(Estudiante estudiante) throws Exception {
        controlPersis.editEstudiante(estudiante);
    }

    //Delete
    public void destroyEstudiante(int id) throws NonexistentEntityException {
        controlPersis.destroyEstudiante(id);
    }
}
