package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.Curso;
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

    public Bocadillo existeBocadillo(String nombre){
        Transaction transaction = null;
        Bocadillo bocadillo = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction(); // Iniciar la transacción

            Query<Bocadillo> query = session.createQuery("FROM Bocadillo WHERE nombre = :nombre", Bocadillo.class);
            query.setParameter("nombre", nombre);

            bocadillo = query.uniqueResult(); // Utilizamos uniqueResult para asegurarnos de que devuelva un unico resultado y generar una consistencia
            transaction.commit(); // Confirmamos la transaccion
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Revertimos si no ha sido success para evitar inconsistencia en la transaccióm
            }
            e.printStackTrace();
        }
        return bocadillo;
    }

    public void save(Bocadillo bocadillo){

        //Se declara una variable para gestionar la transacción
        Transaction transaction = null;

        //Se obtiene una sesión de Hibernate, declarada dentro del Try para asegurar que se cierre automáticamente
        try (Session session = Conexion.getSessionFactory().openSession()) {
            //Se inicia la nueva transacción
            transaction = session.beginTransaction();
            //Se guarda el objeto alumno en la bd, utilzamos persists por que aún esta en estado transitorio
            session.persist(bocadillo);
            //Se confirma la transacción haciendo persistentes los cambios
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Bocadillo bocadillo) { //Se pasa un objeto alumno
        Transaction transaction = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            //Igual que el persits pero une los cambios a el valor anteriormente dado
            session.merge(bocadillo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(Bocadillo bocadillo){
        Transaction transaction = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(bocadillo); // Igua que el persits pero elimina el alumno de la bd
            transaction.commit(); //Confirma la transacción haciendo persistentes los cambios
        } catch (Exception e){
            if (transaction != null) {
                transaction.rollback(); //Si ocurre algún error se revierte la transacción para evitar inconsistencias
            }
            e.printStackTrace();
        }
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

    public long count(HashMap<String, String> filtros) {
        try (Session session = Conexion.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(b) FROM Bocadillo b WHERE true");

            if (filtros != null) {
                for (String key : filtros.keySet()) {
                    hql.append(" AND b.").append(key).append(" LIKE :").append(key);
                }
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            if (filtros != null) {
                for (HashMap.Entry<String, String> filtro : filtros.entrySet()) {
                    query.setParameter(filtro.getKey(), "%" + filtro.getValue() + "%");
                }
            }

            return query.getSingleResult();
        }
    }
}
