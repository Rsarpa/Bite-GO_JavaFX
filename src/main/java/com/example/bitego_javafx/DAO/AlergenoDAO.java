package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Alergeno;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import java.util.List;

public class AlergenoDAO {

    public List<Alergeno> obtenerTodosAlergenos() {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            return session.createQuery("FROM Alergeno", Alergeno.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
