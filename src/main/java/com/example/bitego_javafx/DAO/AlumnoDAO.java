package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AlumnoDAO {

    public List<Alumno> obtenerAlumnos() {
        List<Alumno> alumnos = null;
        Transaction transaction = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            alumnos = session.createQuery("FROM Alumno", Alumno.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Error al recuperar la lista de alumnos");
        }

        return alumnos;
    }

    public void save(Alumno alumno) {
        Transaction transaction = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(alumno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Alumno alumno) {
        Transaction transaction = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(alumno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(Alumno alumno) {
        Transaction transaction = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(alumno);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Alumno getById(int id) {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            return session.get(Alumno.class, id);
        }
    }

    public List<Alumno> getAll() {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            return session.createQuery("FROM Alumno", Alumno.class).list();
        }
    }

    public List<Alumno> getPaginated(int page, int offset) {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            return session.createQuery("FROM Alumno", Alumno.class)
                    .setFirstResult((page - 1) * offset)
                    .setMaxResults(offset)
                    .list();
        }
    }
}
