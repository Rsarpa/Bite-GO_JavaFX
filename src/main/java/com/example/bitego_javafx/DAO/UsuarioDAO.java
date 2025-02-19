package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.Usuario;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UsuarioDAO {
    public UsuarioDAO() {}

    public static Alumno obtenerAlumnoPorEmail(String email) {
        Transaction transaction = null;
        Alumno alumno = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<Alumno> query = session.createQuery(
                    "FROM Alumno WHERE email = :email", Alumno.class);
            query.setParameter("email", email);

            alumno = query.uniqueResult(); // Utilizamos uniqueResult para asegurarnos de que solo devuelva un Alumno

            transaction.commit(); // Confirmamos la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); //Evitamos inconsistencias si algo no ha salido bien
            }
            e.printStackTrace();
        }

        return alumno;
    }

    public Usuario cargarUsuario(String email){
        Transaction transaction = null;
        Usuario usuario = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<Usuario> query = session.createQuery(
                    "FROM Usuario WHERE email = :email", Usuario.class);
            query.setParameter("email", email);

            usuario = query.uniqueResult(); // Utilizamos uniqueResult para asegurarnos de que solo devuelva un Usuario

            transaction.commit(); // Confirmamos la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); //Evitamos inconsistencias si algo no ha salido bien
            }
            e.printStackTrace();
        }

        return usuario;
    }

    public void save (Usuario usuario){
        //Se declara una variable para gestionar la transacción
        Transaction transaction = null;

        //Se obtiene una sesión de Hibernate, declarada dentro del Try para asegurar que se cierre automáticamente
        try (Session session = Conexion.getSessionFactory().openSession()) {
            //Se inicia la nueva transacción
            transaction = session.beginTransaction();
            //Se guarda el objeto alumno en la bd, utilzamos persists por que aún esta en estado transitorio
            session.persist(usuario);
            //Se confirma la transacción haciendo persistentes los cambios
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Usuario usuario){
        Transaction transaction = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            //Igual que el persits pero une los cambios a el valor anteriormente dado
            session.merge(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(String email) throws Exception {
        Transaction transaction = null;
        Usuario usuario = cargarUsuario(email);
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(usuario); // Igua que el persits pero elimina el alumno de la bd
            transaction.commit(); //Confirma la transacción haciendo persistentes los cambios
        } catch (Exception e){
            if (transaction != null) {
                transaction.rollback(); //Si ocurre algún error se revierte la transacción para evitar inconsistencias
                throw new Exception(e);
            }
            e.printStackTrace();
        }
    }

}
