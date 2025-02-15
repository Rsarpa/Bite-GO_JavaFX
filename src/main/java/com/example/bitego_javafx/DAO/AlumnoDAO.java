package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;

public class AlumnoDAO {

    public List<Alumno> obtenerAlumnos() {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            return session.createQuery("FROM Alumno", Alumno.class).list();
        }
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

    public List<Alumno> getPaginated(int page, int offset, HashMap<String, String> filtros) {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM Alumno a WHERE true");

            if (filtros != null) {
                for (String key : filtros.keySet()) {
                    hql.append(" AND a.").append(key).append(" LIKE :").append(key);
                }
            }

            Query<Alumno> query = session.createQuery(hql.toString(), Alumno.class);

            if (filtros != null) {
                for (HashMap.Entry<String, String> filtro : filtros.entrySet()) {
                    query.setParameter(filtro.getKey(), "%" + filtro.getValue() + "%");
                }
            }

            query.setFirstResult((page - 1) * offset);
            query.setMaxResults(offset);

            return query.list();
        }
    }

    public long count(HashMap<String, String> filtros) {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(a) FROM Alumno a WHERE true");

            if (filtros != null) {
                for (String key : filtros.keySet()) {
                    hql.append(" AND a.").append(key).append(" LIKE :").append(key);
                }
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            if (filtros != null) {
                for (HashMap.Entry<String, String> filtro : filtros.entrySet()) {
                    query.setParameter(filtro.getKey(), "%" + filtro.getValue() + "%");
                }
            }

            return query.getSingleResult();
        }
    }
}