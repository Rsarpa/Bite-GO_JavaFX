package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Curso;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CursoDAO {
    public CursoDAO() {
    }

    // METHOD utilizado para asignar de manera sencilla el curso en el CRUD, a partir del nombre obtenemos el objeto Curso
    public static Curso getByNombre(String nombre) {
        Transaction transaction = null;
        Curso curso = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction(); // Iniciar la transacción

            Query<Curso> query = session.createQuery("FROM Curso WHERE nombre_curso = :nombre", Curso.class);
            query.setParameter("nombre", nombre);

            curso = query.uniqueResult(); // Utilizamos uniqueResult para asegurarnos de que devuelva un unico resultado y generar una consistencia

            transaction.commit(); // Confirmamos la transaccion
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Revertimos si no ha sido success para evitar inconsistencia en la transaccióm
            }
            e.printStackTrace();
        }

        return curso;
    }

    public List<Curso> obtenerTodos() {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            return session.createQuery("FROM Curso", Curso.class).list();
        }
    }

}
