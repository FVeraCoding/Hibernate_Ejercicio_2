
package Ejercicio2.Controlador;

import Ejercicio2.Modelo.Clases.Direccion;
import Ejercicio2.Modelo.Persistencia.ControladoraPersistencia;
import Ejercicio2.Modelo.Persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;


public class DireccionController {
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    
    //Create
    public void addDireccion(Direccion direccion){
        controlPersis.addDireccion(direccion);
    }
    
    //Read
    public Direccion findDireccion(int id){
        return controlPersis.findDireccion(id);
    }
    
    public ArrayList<Direccion> findAllDirecciones(){
        return controlPersis.findAllDirecciones();
    }
    
    //Update
    public void editDireccion(Direccion direccion) throws Exception{
        controlPersis.editDireccion(direccion);
    }
    
    //Delete
    public void destroyDireccion(int id) throws NonexistentEntityException{
        controlPersis.destroyDireccion(id);
    }
}
