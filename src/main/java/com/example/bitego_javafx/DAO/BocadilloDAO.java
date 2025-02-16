package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class BocadilloDAO {

    //Recupera todos los Bocadillos Disponibles, útil para el adminsitrador
    public List<Bocadillo> obtenerBocadillos(){
        List<Bocadillo> bocadillos = null;
        Transaction transaction = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            //utilizamos getResultList para obtener una pila de resultados
            bocadillos = session.createQuery("FROM Bocadillo", Bocadillo.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Error al recuperar la lista de bocadillos");
        }

        return bocadillos;
    }

    //Obtenemos los bocadillos filtrados por dia
    public List<Bocadillo> obtenerBocadillosDelDia() {
        List<Bocadillo> bocadillos = null;
        int diaHoy = LocalDate.now().getDayOfWeek().getValue(); // Obtiene el día actual (1=Lunes, 7=Domingo) (En numero entero)

        Transaction transaction = null;
        //Abrimos la sesion en el try para asegurar que se cierre una vez terminada
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            bocadillos = session.createQuery(
                            "FROM Bocadillo WHERE dia_asociado = :diaHoy", Bocadillo.class)
                    .setParameter("diaHoy", diaHoy) //Asignamos el parámetro de esta forma para evitar la inyección
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Error al recuperar los bocadillos del día");
        }
        return bocadillos;
    }

    public List<Bocadillo> getPaginated(int page, int offset, HashMap<String, String> filtros){
        try (Session session = Conexion.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM Bocadillo a WHERE true"); //Utilizamos StringBuilder para que la consulta más eficiente

            if (filtros != null) {
                for (String key : filtros.keySet()) {
                    hql.append(" AND b.").append(key).append(" LIKE :").append(key);
                }
            }

            Query<Bocadillo> query = session.createQuery(hql.toString(), Bocadillo.class);

            //Se envuelve con % % para permita coindicencias parciales
            if (filtros != null) {
                for (HashMap.Entry<String, String> filtro : filtros.entrySet()) {
                    query.setParameter(filtro.getKey(), "%" + filtro.getValue() + "%");
                }
            }

            query.setFirstResult((page - 1) * offset);
            //EJ Si page = 1 y offset =10, el primer resultado será (1 - 1) * 10 = 0 (primer resultado).
            query.setMaxResults(offset);

            return query.list();
        }
    }
}
