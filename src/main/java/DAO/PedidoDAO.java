package DAO;

import com.example.bitego_javafx.Model.Pedido;
import com.example.bitego_javafx.Util.Conexion;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public List<Pedido> listarPedidos(String nombreAlu, String apellidoAlu, String curso) throws SQLException {

        //todo pendiente
        List<Pedido> listaPedidos = new ArrayList<>();
        Transaction transaction = null;

        try(Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Conexión con la BD");
            StringBuilder jpql = new StringBuilder ("SELECT a.nombre, a.apellidos, c.nombre, b.tipo, pb.id " +
                    "FROM PedidoBocadillo pb " +
                    "JOIN pb.alumno a " +
                    "JOIN pb.bocadillo b " +
                    "JOIN a.curso c " +
                    "WHERE FUNCTION('DATE', pb.fechaHora) = CURRENT_DATE " +
                    "AND pb.retirado IS NULL");

            if (nombreAlu != null && !nombreAlu.isEmpty()) {
                jpql.append(" AND a.nombre = :nombreAlu");
            }
            if (apellidoAlu != null && !apellidoAlu.isEmpty()) {
                jpql.append(" AND a.apellidos = :apellidoAlu");
            }
            if (curso != null && !curso.isEmpty()) {
                jpql.append(" AND a.id_curso = :curso");
            }


            Query query = session.createQuery(jpql.toString(), Pedido.class);

            if (nombreAlu != null && !nombreAlu.isEmpty()){
                query.setParameter("nombreAlu",nombreAlu);
            }
            if (apellidoAlu != null && !apellidoAlu.isEmpty()){
                query.setParameter("nombreAlu",nombreAlu);
            }
            if (nombreAlu != null && !nombreAlu.isEmpty()){
                query.setParameter("nombreAlu",nombreAlu);
            }

            //Recoger el resultado de la consulta
            listaPedidos = query.getResultList();

        }catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return listaPedidos;
    }
}
