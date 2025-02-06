package Controler;

import com.example.bitego_javafx.Clases.Pedido;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private final SessionFactory sf;

    public PedidoDAO(SessionFactory sf){
        this.sf = sf;
    }

    public List<Pedido> listarPedidos(String nombreAlu, String apellidoAlu, String curso) throws SQLException {

        //todo pendiente
        List<Pedido> listaPedidos = new ArrayList<>();
        Transaction transaction = null;


        try(Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Conexión con la BD");
            StringBuilder sql = new StringBuilder("select a.nombre, a.apellidos, a.id_curso, b.tipo, pb.id_pedido from pedido_bocadillo pb, alumno a, bocadillo b where pb.id_alumno = a.id_alumno AND pb.id_bocadillo = b.nombre AND DATE(pb.fecha_hora) = CURDATE() AND pb.retirado IS null");

            if (nombreAlu != null && !nombreAlu.isEmpty()) {
                sql.append(" AND a.nombre = :nombreAlu");
            }
            if (apellidoAlu != null && !apellidoAlu.isEmpty()) {
                sql.append(" AND a.apellidos = :apellidoAlu");
            }
            if (curso != null && !curso.isEmpty()) {
                sql.append(" AND a.id_curso = :curso");
            }


            Query query = session.createQuery(sql.toString(), Pedido.class);

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
