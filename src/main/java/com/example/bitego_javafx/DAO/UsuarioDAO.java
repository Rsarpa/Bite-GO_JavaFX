package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UsuarioDAO {
    public UsuarioDAO() {
    }

    public static Alumno obtenerAlumnoPorEmail(String email) {
        Alumno alumno = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            Query<Alumno> query = session.createQuery(
                    "FROM Alumno WHERE email = :email", Alumno.class);
            query.setParameter("email", email);
            alumno = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alumno;
    }
}
