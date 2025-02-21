package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;

public class AlumnoDAO {
    /*
    public List<Alumno> obtenerAlumnos() {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            return session.createQuery("FROM Alumno", Alumno.class).list();
        }
    }*/

    public void save(Alumno alumno) {

        //Se declara una variable para gestionar la transacción
        Transaction transaction = null;

        //Se obtiene una sesión de Hibernate, declarada dentro del Try para asegurar que se cierre automáticamente
        try (Session session = Conexion.getSessionFactory().openSession()) {
            //Se inicia la nueva transacción
            transaction = session.beginTransaction();
            //Se guarda el objeto alumno en la bd, utilzamos persists por que aún esta en estado transitorio
            session.persist(alumno);
            //Se confirma la transacción haciendo persistentes los cambios
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Alumno alumno) { //Se pasa un objeto alumno
        Transaction transaction = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(alumno); //Igual que el persits pero une los cambios a el valor anteriormente dado
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
            session.remove(alumno); // Igua que el persits pero elimina el alumno de la bd
            transaction.commit(); //Confirma la transacción haciendo persistentes los cambios
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); //Si ocurre algún error se revierte la transacción para evitar inconsistencias
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

            //Filtros
            if (filtros != null) {
                for (String key : filtros.keySet()) {
                    //Caso especial para Curso
                    if (key.equals("idCurso")) { // Si el filtro es por ID del curso
                        hql.append(" AND a.curso.id_curso = :idCurso"); // Comparación directa
                    } else {
                        hql.append(" AND a.").append(key).append(" LIKE :").append(key);
                    }
                }
            }

            Query<Alumno> query = session.createQuery(hql.toString(), Alumno.class);

            //Valores
            if (filtros != null) {
                for (HashMap.Entry<String, String> filtro : filtros.entrySet()) {
                    //Caso especial para el Curso
                    if (filtro.getKey().equals("idCurso")) {
                        query.setParameter("idCurso", Integer.parseInt(filtro.getValue())); // Convertir a número
                    } else {
                        query.setParameter(filtro.getKey(), "%" + filtro.getValue() + "%");
                    }
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

            //Filtros
            if (filtros != null) {
                for (String key : filtros.keySet()) {
                    //Caso especial para el Curso
                    if (key.equals("idCurso")) {
                        hql.append(" AND a.curso.id_curso = :idCurso");
                    } else {
                        hql.append(" AND a.").append(key).append(" LIKE :").append(key);
                    }
                }
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);
            //Valor
            if (filtros != null) {
                for (HashMap.Entry<String, String> filtro : filtros.entrySet()) {
                    //Caso especial para el Curso
                    if (filtro.getKey().equals("idCurso")) {
                        query.setParameter("idCurso", Integer.parseInt(filtro.getValue()));
                    } else {
                        query.setParameter(filtro.getKey(), "%" + filtro.getValue() + "%");
                    }
                }
            }

            return query.getSingleResult();
        }
    }

}