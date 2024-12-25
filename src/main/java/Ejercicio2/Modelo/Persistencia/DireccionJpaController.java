/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio2.Modelo.Persistencia;

import Ejercicio2.Modelo.Clases.Direccion;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import Ejercicio2.Modelo.Clases.Estudiante;
import Ejercicio2.Modelo.Persistencia.exceptions.NonexistentEntityException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

/**
 *
 * @author Fernando
 */
public class DireccionJpaController implements Serializable {

    public DireccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public DireccionJpaController(){
        this.emf = Persistence.createEntityManagerFactory("JPUnit");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Direccion direccion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante estudiante = direccion.getEstudiante();
            if (estudiante != null) {
                estudiante = em.getReference(estudiante.getClass(), estudiante.getId());
                direccion.setEstudiante(estudiante);
            }
            em.persist(direccion);
            if (estudiante != null) {
                Direccion oldDireccionOfEstudiante = estudiante.getDireccion();
                if (oldDireccionOfEstudiante != null) {
                    oldDireccionOfEstudiante.setEstudiante(null);
                    oldDireccionOfEstudiante = em.merge(oldDireccionOfEstudiante);
                }
                estudiante.setDireccion(direccion);
                estudiante = em.merge(estudiante);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Direccion direccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion persistentDireccion = em.find(Direccion.class, direccion.getId());
            Estudiante estudianteOld = persistentDireccion.getEstudiante();
            Estudiante estudianteNew = direccion.getEstudiante();
            if (estudianteNew != null) {
                estudianteNew = em.getReference(estudianteNew.getClass(), estudianteNew.getId());
                direccion.setEstudiante(estudianteNew);
            }
            direccion = em.merge(direccion);
            if (estudianteOld != null && !estudianteOld.equals(estudianteNew)) {
                estudianteOld.setDireccion(null);
                estudianteOld = em.merge(estudianteOld);
            }
            if (estudianteNew != null && !estudianteNew.equals(estudianteOld)) {
                Direccion oldDireccionOfEstudiante = estudianteNew.getDireccion();
                if (oldDireccionOfEstudiante != null) {
                    oldDireccionOfEstudiante.setEstudiante(null);
                    oldDireccionOfEstudiante = em.merge(oldDireccionOfEstudiante);
                }
                estudianteNew.setDireccion(direccion);
                estudianteNew = em.merge(estudianteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = direccion.getId();
                if (findDireccion(id) == null) {
                    throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.");
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
            Direccion direccion;
            try {
                direccion = em.getReference(Direccion.class, id);
                direccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.", enfe);
            }
            Estudiante estudiante = direccion.getEstudiante();
            if (estudiante != null) {
                estudiante.setDireccion(null);
                estudiante = em.merge(estudiante);
            }
            em.remove(direccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Direccion> findDireccionEntities() {
        return findDireccionEntities(true, -1, -1);
    }

    public List<Direccion> findDireccionEntities(int maxResults, int firstResult) {
        return findDireccionEntities(false, maxResults, firstResult);
    }

    private List<Direccion> findDireccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direccion.class));
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

    public Direccion findDireccion(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Direccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDireccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Direccion> rt = cq.from(Direccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
