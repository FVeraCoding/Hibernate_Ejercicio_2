/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio2.Modelo.Persistencia;

import Ejercicio2.Modelo.Clases.Curso;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import Ejercicio2.Modelo.Clases.Profesor;
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
public class CursoJpaController implements Serializable {

    public CursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public CursoJpaController(){
        this.emf = Persistence.createEntityManagerFactory("JPUnit");
    }

    public void create(Curso curso) {
        if (curso.getListaEstudiantes() == null) {
            curso.setListaEstudiantes(new ArrayList<Estudiante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor profesor = curso.getProfesor();
            if (profesor != null) {
                profesor = em.getReference(profesor.getClass(), profesor.getId());
                curso.setProfesor(profesor);
            }
            List<Estudiante> attachedListaEstudiantes = new ArrayList<Estudiante>();
            for (Estudiante listaEstudiantesEstudianteToAttach : curso.getListaEstudiantes()) {
                listaEstudiantesEstudianteToAttach = em.getReference(listaEstudiantesEstudianteToAttach.getClass(), listaEstudiantesEstudianteToAttach.getId());
                attachedListaEstudiantes.add(listaEstudiantesEstudianteToAttach);
            }
            curso.setListaEstudiantes(attachedListaEstudiantes);
            em.persist(curso);
            if (profesor != null) {
                profesor.getListaCursos().add(curso);
                profesor = em.merge(profesor);
            }
            for (Estudiante listaEstudiantesEstudiante : curso.getListaEstudiantes()) {
                listaEstudiantesEstudiante.getListaCursos().add(curso);
                listaEstudiantesEstudiante = em.merge(listaEstudiantesEstudiante);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso persistentCurso = em.find(Curso.class, curso.getId());
            Profesor profesorOld = persistentCurso.getProfesor();
            Profesor profesorNew = curso.getProfesor();
            List<Estudiante> listaEstudiantesOld = persistentCurso.getListaEstudiantes();
            List<Estudiante> listaEstudiantesNew = curso.getListaEstudiantes();
            if (profesorNew != null) {
                profesorNew = em.getReference(profesorNew.getClass(), profesorNew.getId());
                curso.setProfesor(profesorNew);
            }
            List<Estudiante> attachedListaEstudiantesNew = new ArrayList<Estudiante>();
            for (Estudiante listaEstudiantesNewEstudianteToAttach : listaEstudiantesNew) {
                listaEstudiantesNewEstudianteToAttach = em.getReference(listaEstudiantesNewEstudianteToAttach.getClass(), listaEstudiantesNewEstudianteToAttach.getId());
                attachedListaEstudiantesNew.add(listaEstudiantesNewEstudianteToAttach);
            }
            listaEstudiantesNew = attachedListaEstudiantesNew;
            curso.setListaEstudiantes(listaEstudiantesNew);
            curso = em.merge(curso);
            if (profesorOld != null && !profesorOld.equals(profesorNew)) {
                profesorOld.getListaCursos().remove(curso);
                profesorOld = em.merge(profesorOld);
            }
            if (profesorNew != null && !profesorNew.equals(profesorOld)) {
                profesorNew.getListaCursos().add(curso);
                profesorNew = em.merge(profesorNew);
            }
            for (Estudiante listaEstudiantesOldEstudiante : listaEstudiantesOld) {
                if (!listaEstudiantesNew.contains(listaEstudiantesOldEstudiante)) {
                    listaEstudiantesOldEstudiante.getListaCursos().remove(curso);
                    listaEstudiantesOldEstudiante = em.merge(listaEstudiantesOldEstudiante);
                }
            }
            for (Estudiante listaEstudiantesNewEstudiante : listaEstudiantesNew) {
                if (!listaEstudiantesOld.contains(listaEstudiantesNewEstudiante)) {
                    listaEstudiantesNewEstudiante.getListaCursos().add(curso);
                    listaEstudiantesNewEstudiante = em.merge(listaEstudiantesNewEstudiante);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = curso.getId();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            Profesor profesor = curso.getProfesor();
            if (profesor != null) {
                profesor.getListaCursos().remove(curso);
                profesor = em.merge(profesor);
            }
            List<Estudiante> listaEstudiantes = curso.getListaEstudiantes();
            for (Estudiante listaEstudiantesEstudiante : listaEstudiantes) {
                listaEstudiantesEstudiante.getListaCursos().remove(curso);
                listaEstudiantesEstudiante = em.merge(listaEstudiantesEstudiante);
            }
            em.remove(curso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
