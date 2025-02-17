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

    //Este method obtiene una lista de obtejos alumnos paginados,permitiendo aplicar filtros dinámicos utilizando HashMap
    public List<Alumno> getPaginated(int page, int offset, HashMap<String, String> filtros) {
        //Nos aseguramos de que la sesión se cierre utilizando la declaración en el try
        try (Session session = Conexion.getSessionFactory().openSession()) {
            /*
            Se empieza con FROM Alumno a WHERE true
            para que la construcción de filtros sea más sencilla
            (ya que true siempre es válido y evita problemas al concatenar condiciones).
             */
            StringBuilder hql = new StringBuilder("FROM Alumno a WHERE true"); //Utilizamos StringBuilder para que la consulta más eficiente
            /*
            Si filtros no es nulo se recorren sus claves donde key representa los nombres de los atributos de alumno
            A cada clave se le agrega un filtro LIKE dinámicamente
             Si filtros contiene { "nombre": "Juan", "curso": "Matemáticas" }
             Pimera append(key) para el nombre del atributo y segunda append(key) para el valor
             FROM Alumno a WHERE true
             AND a.nombre LIKE :nombre
             AND a.curso LIKE :curso
             */

            if (filtros != null) {
                for (String key : filtros.keySet()) {
                    hql.append(" AND a.").append(key).append(" LIKE :").append(key);
                }
            }
            //Se convierte de StringBuilder a String y se ejecuta la consulta
            Query<Alumno> query = session.createQuery(hql.toString(), Alumno.class);

            //Se envuelve con % % para permita coindicencias parciales
            if (filtros != null) {
                for (HashMap.Entry<String, String> filtro : filtros.entrySet()) {
                    query.setParameter(filtro.getKey(), "%" + filtro.getValue() + "%");
                }
            }

            query.setFirstResult((page - 1) * offset);
            //EJ Si page = 1 y offset =10, el primer resultado será (1 - 1) * 10 = 0 (primer resultado).
            query.setMaxResults(offset);
            //Asigna el numero máximo de resultados

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