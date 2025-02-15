package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Curso;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class CursoDAO {
    public static Curso getByNombre(String nombre) {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            Query<Curso> query = session.createQuery("FROM Curso WHERE nombre_curso = :nombre", Curso.class);
            query.setParameter("nombre", nombre);
            return query.uniqueResult();
        }
    }
}
