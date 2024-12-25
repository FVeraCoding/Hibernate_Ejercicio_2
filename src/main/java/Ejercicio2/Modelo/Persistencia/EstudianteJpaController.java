/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio2.Modelo.Persistencia;

import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import Ejercicio2.Modelo.Clases.Direccion;
import Ejercicio2.Modelo.Clases.Curso;
import Ejercicio2.Modelo.Clases.Estudiante;
import Ejercicio2.Modelo.Persistencia.exceptions.NonexistentEntityException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fernando
 */
public class EstudianteJpaController implements Serializable {

    public EstudianteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public EstudianteJpaController(){
        this.emf = Persistence.createEntityManagerFactory("JPUnit");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estudiante estudiante) {
        if (estudiante.getListaCursos() == null) {
            estudiante.setListaCursos(new ArrayList<Curso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccion = estudiante.getDireccion();
            if (direccion != null) {
                direccion = em.getReference(direccion.getClass(), direccion.getId());
                estudiante.setDireccion(direccion);
            }
            List<Curso> attachedListaCursos = new ArrayList<Curso>();
            for (Curso listaCursosCursoToAttach : estudiante.getListaCursos()) {
                listaCursosCursoToAttach = em.getReference(listaCursosCursoToAttach.getClass(), listaCursosCursoToAttach.getId());
                attachedListaCursos.add(listaCursosCursoToAttach);
            }
            estudiante.setListaCursos(attachedListaCursos);
            em.persist(estudiante);
            if (direccion != null) {
                Estudiante oldEstudianteOfDireccion = direccion.getEstudiante();
                if (oldEstudianteOfDireccion != null) {
                    oldEstudianteOfDireccion.setDireccion(null);
                    oldEstudianteOfDireccion = em.merge(oldEstudianteOfDireccion);
                }
                direccion.setEstudiante(estudiante);
                direccion = em.merge(direccion);
            }
            for (Curso listaCursosCurso : estudiante.getListaCursos()) {
                listaCursosCurso.getListaEstudiantes().add(estudiante);
                listaCursosCurso = em.merge(listaCursosCurso);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estudiante estudiante) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante persistentEstudiante = em.find(Estudiante.class, estudiante.getId());
            Direccion direccionOld = persistentEstudiante.getDireccion();
            Direccion direccionNew = estudiante.getDireccion();
            List<Curso> listaCursosOld = persistentEstudiante.getListaCursos();
            List<Curso> listaCursosNew = estudiante.getListaCursos();
            if (direccionNew != null) {
                direccionNew = em.getReference(direccionNew.getClass(), direccionNew.getId());
                estudiante.setDireccion(direccionNew);
            }
            List<Curso> attachedListaCursosNew = new ArrayList<Curso>();
            for (Curso listaCursosNewCursoToAttach : listaCursosNew) {
                listaCursosNewCursoToAttach = em.getReference(listaCursosNewCursoToAttach.getClass(), listaCursosNewCursoToAttach.getId());
                attachedListaCursosNew.add(listaCursosNewCursoToAttach);
            }
            listaCursosNew = attachedListaCursosNew;
            estudiante.setListaCursos(listaCursosNew);
            estudiante = em.merge(estudiante);
            if (direccionOld != null && !direccionOld.equals(direccionNew)) {
                direccionOld.setEstudiante(null);
                direccionOld = em.merge(direccionOld);
            }
            if (direccionNew != null && !direccionNew.equals(direccionOld)) {
                Estudiante oldEstudianteOfDireccion = direccionNew.getEstudiante();
                if (oldEstudianteOfDireccion != null) {
                    oldEstudianteOfDireccion.setDireccion(null);
                    oldEstudianteOfDireccion = em.merge(oldEstudianteOfDireccion);
                }
                direccionNew.setEstudiante(estudiante);
                direccionNew = em.merge(direccionNew);
            }
            for (Curso listaCursosOldCurso : listaCursosOld) {
                if (!listaCursosNew.contains(listaCursosOldCurso)) {
                    listaCursosOldCurso.getListaEstudiantes().remove(estudiante);
                    listaCursosOldCurso = em.merge(listaCursosOldCurso);
                }
            }
            for (Curso listaCursosNewCurso : listaCursosNew) {
                if (!listaCursosOld.contains(listaCursosNewCurso)) {
                    listaCursosNewCurso.getListaEstudiantes().add(estudiante);
                    listaCursosNewCurso = em.merge(listaCursosNewCurso);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = estudiante.getId();
                if (findEstudiante(id) == null) {
                    throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante estudiante;
            try {
                estudiante = em.getReference(Estudiante.class, id);
                estudiante.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.", enfe);
            }
            Direccion direccion = estudiante.getDireccion();
            if (direccion != null) {
                direccion.setEstudiante(null);
                direccion = em.merge(direccion);
            }
            List<Curso> listaCursos = estudiante.getListaCursos();
            for (Curso listaCursosCurso : listaCursos) {
                listaCursosCurso.getListaEstudiantes().remove(estudiante);
                listaCursosCurso = em.merge(listaCursosCurso);
            }
            em.remove(estudiante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estudiante> findEstudianteEntities() {
        return findEstudianteEntities(true, -1, -1);
    }

    public List<Estudiante> findEstudianteEntities(int maxResults, int firstResult) {
        return findEstudianteEntities(false, maxResults, firstResult);
    }

    private List<Estudiante> findEstudianteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estudiante.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Estudiante findEstudiante(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estudiante.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstudianteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estudiante> rt = cq.from(Estudiante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
