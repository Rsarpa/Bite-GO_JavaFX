package DAO;

import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class BocadilloDAO {

    public List<Bocadillo> obtenerBocadillosDelDia() {
        List<Bocadillo> bocadillos = null;
        int diaHoy = LocalDate.now().getDayOfWeek().getValue(); // Obtiene el día actual (1=Lunes, 7=Domingo)

        try (Session session = Conexion.getSessionFactory().openSession()) {
            bocadillos = session.createQuery(
                            "FROM Bocadillo WHERE dia_asociado = :diaHoy", Bocadillo.class)
                    .setParameter("diaHoy", diaHoy)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al recuperar los bocadillos del día");
        }
        return bocadillos;
    }
}
