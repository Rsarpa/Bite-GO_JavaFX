package DAO;

import com.example.bitego_javafx.Model.Usuario;
import com.example.bitego_javafx.Util.Conexion;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LoginDAO {

    public Usuario log(String email, String contrasenya){

        String rol = "";
        Usuario usuario = null;

        try(Session session = Conexion.getSessionFactory().openSession()) {
            usuario = session.createQuery("FROM Usuario WHERE email = :email and contrasenya = :contrasenya", Usuario.class)
                .setParameter("email", email)
                .setParameter("contrasenya", contrasenya)
                .uniqueResult();

            if (usuario != null) {
                System.out.println("Usuario: " + email + " " + contrasenya + " " + rol);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error en la consulta");
        }
        return usuario;
    }
}
