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
import Ejercicio2.Modelo.Clases.Curso;
import Ejercicio2.Modelo.Clases.Profesor;
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
public class ProfesorJpaController implements Serializable {

    public ProfesorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ProfesorJpaController(){
        this.emf = Persistence.createEntityManagerFactory("JPUnit");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesor profesor) {
        if (profesor.getListaCursos() == null) {
            profesor.setListaCursos(new ArrayList<Curso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Curso> attachedListaCursos = new ArrayList<Curso>();
            for (Curso listaCursosCursoToAttach : profesor.getListaCursos()) {
                listaCursosCursoToAttach = em.getReference(listaCursosCursoToAttach.getClass(), listaCursosCursoToAttach.getId());
                attachedListaCursos.add(listaCursosCursoToAttach);
            }
            profesor.setListaCursos(attachedListaCursos);
            em.persist(profesor);
            for (Curso listaCursosCurso : profesor.getListaCursos()) {
                Profesor oldProfesorOfListaCursosCurso = listaCursosCurso.getProfesor();
                listaCursosCurso.setProfesor(profesor);
                listaCursosCurso = em.merge(listaCursosCurso);
                if (oldProfesorOfListaCursosCurso != null) {
                    oldProfesorOfListaCursosCurso.getListaCursos().remove(listaCursosCurso);
                    oldProfesorOfListaCursosCurso = em.merge(oldProfesorOfListaCursosCurso);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesor profesor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor persistentProfesor = em.find(Profesor.class, profesor.getId());
            List<Curso> listaCursosOld = persistentProfesor.getListaCursos();
            List<Curso> listaCursosNew = profesor.getListaCursos();
            List<Curso> attachedListaCursosNew = new ArrayList<Curso>();
            for (Curso listaCursosNewCursoToAttach : listaCursosNew) {
                listaCursosNewCursoToAttach = em.getReference(listaCursosNewCursoToAttach.getClass(), listaCursosNewCursoToAttach.getId());
                attachedListaCursosNew.add(listaCursosNewCursoToAttach);
            }
            listaCursosNew = attachedListaCursosNew;
            profesor.setListaCursos(listaCursosNew);
            profesor = em.merge(profesor);
            for (Curso listaCursosOldCurso : listaCursosOld) {
                if (!listaCursosNew.contains(listaCursosOldCurso)) {
                    listaCursosOldCurso.setProfesor(null);
                    listaCursosOldCurso = em.merge(listaCursosOldCurso);
                }
            }
            for (Curso listaCursosNewCurso : listaCursosNew) {
                if (!listaCursosOld.contains(listaCursosNewCurso)) {
                    Profesor oldProfesorOfListaCursosNewCurso = listaCursosNewCurso.getProfesor();
                    listaCursosNewCurso.setProfesor(profesor);
                    listaCursosNewCurso = em.merge(listaCursosNewCurso);
                    if (oldProfesorOfListaCursosNewCurso != null && !oldProfesorOfListaCursosNewCurso.equals(profesor)) {
                        oldProfesorOfListaCursosNewCurso.getListaCursos().remove(listaCursosNewCurso);
                        oldProfesorOfListaCursosNewCurso = em.merge(oldProfesorOfListaCursosNewCurso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = profesor.getId();
                if (findProfesor(id) == null) {
                    throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.");
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
            Profesor profesor;
            try {
                profesor = em.getReference(Profesor.class, id);
                profesor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.", enfe);
            }
            List<Curso> listaCursos = profesor.getListaCursos();
            for (Curso listaCursosCurso : listaCursos) {
                listaCursosCurso.setProfesor(null);
                listaCursosCurso = em.merge(listaCursosCurso);
            }
            em.remove(profesor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesor> findProfesorEntities() {
        return findProfesorEntities(true, -1, -1);
    }

    public List<Profesor> findProfesorEntities(int maxResults, int firstResult) {
        return findProfesorEntities(false, maxResults, firstResult);
    }

    private List<Profesor> findProfesorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesor.class));
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

    public Profesor findProfesor(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesor> rt = cq.from(Profesor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
