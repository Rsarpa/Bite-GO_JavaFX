package Controler;

import com.example.bitego_javafx.Clases.Usuario;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class LoginDAO {
    private final SessionFactory sf;

    public LoginDAO(SessionFactory sf) {
        this.sf = sf;
    }

    public Usuario log(String email, String contrasena){

        Usuario usuario = null;

        try(Session session = sf.openSession()) {
            StringBuilder sql = new StringBuilder("select * from usuario where email = '" + email + "' and contrasenya = '" + contrasena + "'");
            Query query = session.createQuery(sql.toString(), Usuario.class);

            query.setParameter("email", email);
            query.setParameter("contrasena", contrasena);

            usuario = (Usuario) query.getSingleResult();
        }catch (NoResultException e){
            System.out.println("No se encontraron resultados");
            usuario = null;
        }
        return usuario;
    }
}
