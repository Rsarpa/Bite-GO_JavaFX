package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.PedidoBocadillo;
import com.example.bitego_javafx.Util.Conexion;
import com.sun.jna.platform.win32.OaIdl;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedidoDAO {

    public PedidoDAO() {}

    //Lista todos los bocadillos , Utilizado en Cocina
    public List<PedidoBocadillo> listarPedidos(HashMap<String, String> filtros){

        List<PedidoBocadillo> listaPedidos = new ArrayList<>();
        Transaction transaction = null;

        try(Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Conexión con la BD");
            StringBuilder jpql = new StringBuilder("FROM PedidoBocadillo pb WHERE FUNCTION('DATE', pb.fecha_hora) = CURRENT_DATE AND pb.retirado = false");

            if (filtros != null){
                for (String key : filtros.keySet()){
                    if (key.equals("nombre")){
                        jpql.append(" AND pb.alumno.nombre LIKE :").append(key);
                    }if (key.equals("apellido")){
                        jpql.append(" AND pb.alumno.apellidos LIKE :").append(key);
                    }if(key.equals("curso")){
                        jpql.append(" AND pb.alumno.curso.nombre_curso LIKE :").append(key);
                    }
                }
            }

            Query<PedidoBocadillo> query = session.createQuery(jpql.toString(), PedidoBocadillo.class);


            // Asignar valores a los parámetros de la consulta
            if (filtros != null)
                for (HashMap.Entry<String, String> filtro : filtros.entrySet()) {
                    query.setParameter(filtro.getKey(),"%"+filtro.getValue()+"%");
                }

            return query.list();

        }
    }

    //Lista todos los bocadillos , Utilizado en Cocina
    public List<PedidoBocadillo> listarPedidosRetirados(HashMap<String, String> filtros){

        List<PedidoBocadillo> listaPedidos = new ArrayList<>();
        Transaction transaction = null;

        try(Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Conexión con la BD");
            StringBuilder jpql = new StringBuilder("FROM PedidoBocadillo pb WHERE FUNCTION('DATE', pb.fecha_hora) = CURRENT_DATE AND pb.retirado = true");

            if (filtros != null){
                for (String key : filtros.keySet()){
                    if (key.equals("nombre")){
                        jpql.append(" AND pb.alumno.nombre LIKE :").append(key);
                    }if (key.equals("apellido")){
                        jpql.append(" AND pb.alumno.apellidos LIKE :").append(key);
                    }if(key.equals("curso")){
                        jpql.append(" AND pb.alumno.curso.nombre_curso LIKE :").append(key);
                    }
                }
            }

            Query<PedidoBocadillo> query = session.createQuery(jpql.toString(), PedidoBocadillo.class);


            // Asignar valores a los parámetros de la consulta
            if (filtros != null)
                for (HashMap.Entry<String, String> filtro : filtros.entrySet()) {
                    query.setParameter(filtro.getKey(),"%"+filtro.getValue()+"%");
                }

            return query.list();

        }
    }

    public Boolean realizarPedido(PedidoBocadillo pedido) {
        Transaction transaction = null; //Inicializa la transacción
        //System.out.println("Entra a realizar pedido");
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(pedido); //Utiliza save o persist para guardar el pedido
            transaction.commit();
            System.out.println("Todo realizado correctamente");
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Que mierda está pasando");
                return false;
            }
            e.printStackTrace();
            System.out.println("Error al realizar el pedido");
        }
        return null;
    }

    //Otener el pedido del dia del alumno
    public PedidoBocadillo obtenerPedidoDelDia(int id_alumno, Date fecha) {
        PedidoBocadillo pedidoHoy = null;
        Transaction transaction = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction(); // Inicia la transacción

            pedidoHoy = session.createQuery(
                            "FROM PedidoBocadillo WHERE alumno.id = :id AND DATE(fecha_hora) = :fechahoy", //Utilizamos DATE para comparar las fechas
                            PedidoBocadillo.class)
                    .setParameter("id", id_alumno)
                    .setParameter("fechahoy", fecha)
                    .setMaxResults(1)  // Para asegurarnos de obtener solo un resultado
                    .uniqueResult();   // Devuelve un solo objeto o null si no hay resultados

            transaction.commit(); // Confirma la transacción

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Revierte la transacción si existe algun error para evitar incosistencia de Datos
            }
            e.printStackTrace();
        }

        return pedidoHoy;
    }

    public void marcarRetirado(int idPedido){
        Transaction transaction = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Obtener el pedido por ID
            PedidoBocadillo pedido = session.get(PedidoBocadillo.class, idPedido);
            if (pedido != null) {
                pedido.setRetirado(true); // Marcar como retirado
                session.update(pedido);   // Actualizar en la base de datos
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void marcarNoRetirado(int idPedido) {
        Transaction transaction = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            PedidoBocadillo pedido = session.get(PedidoBocadillo.class, idPedido);
            if (pedido != null) {
                pedido.setRetirado(false); // Marcar como no retirado
                session.update(pedido);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }



    //En si actua como un delete , debido a que eliminamos el registro de la BD
    public Boolean cancelarPedido(int id_alumno,Date fecha){
        PedidoBocadillo pedidoHoy=obtenerPedidoDelDia(id_alumno,fecha); //Lo primero que hacemos es obtener el Pedido del Dia
        Transaction transaction = null;
        try (Session session=Conexion.getSessionFactory().openSession()){ //Iniciamos la transacción en el try
            transaction = session.beginTransaction();
            session.remove(pedidoHoy); //Eliminamos el registro de la bd,utilizamos remove para eliminar antes del commit
            transaction.commit();
            System.out.println("Pedido eliminado correctamente");
            return true;
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); //Volvemos atras si existe algun error para evitar la inconsistencia
            }
            e.printStackTrace();
            System.out.println("Error al cancelar el pedido");
        }
        return false;
    }

    /*
    Obtenemos los pedidos del alumno teniendo en cuenta el ComboBox para filtrar por los tiempos
    Pedidos de antes de: 1MES 3 MESES 6 MESES 1 AÑO
     */
    public static List<PedidoBocadillo> obtenerPedidosDelAlumno(int id_alumno, int page, int offset, LocalDate fechaFiltro, LocalDate fechaInicio, LocalDate fechaFin) {
        List<PedidoBocadillo> lista_pedidos_alumno = null;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            String jpql = "FROM PedidoBocadillo WHERE alumno.id = :id AND fecha_hora >= :fechaFiltro";
            if (fechaInicio != null && fechaFin != null) {
                jpql += " AND fecha_hora BETWEEN :fechaInicio AND :fechaFin";
            }
            Query<PedidoBocadillo> query = session.createQuery(jpql, PedidoBocadillo.class)
                    .setParameter("id", id_alumno)
                    .setParameter("fechaFiltro", fechaFiltro != null ? Date.valueOf(fechaFiltro) : Date.valueOf(LocalDate.now().minusYears(10)));
            if (fechaInicio != null && fechaFin != null) {
                query.setParameter("fechaInicio", Date.valueOf(fechaInicio));
                query.setParameter("fechaFin", Date.valueOf(fechaFin));
            }
            return query.setFirstResult((page - 1) * offset).setMaxResults(offset).list();
        }
    }

    public static long obtenerTotalPedidos(int id_alumno, LocalDate fechaFiltro, LocalDate fechaInicio, LocalDate fechaFin) {
        long total = 0;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            String jpql = "SELECT COUNT(p) FROM PedidoBocadillo p WHERE p.alumno.id = :id";

            if (fechaInicio != null && fechaFin != null) {
                jpql += " AND p.fecha_hora BETWEEN :fechaInicio AND :fechaFin";
            } else if (fechaFiltro != null) {
                jpql += " AND p.fecha_hora >= :fechaFiltro";
            }

            Query<Long> query = session.createQuery(jpql, Long.class)
                    .setParameter("id", id_alumno);

            if (fechaInicio != null && fechaFin != null) {
                query.setParameter("fechaInicio", java.sql.Date.valueOf(fechaInicio));
                query.setParameter("fechaFin", java.sql.Date.valueOf(fechaFin));
            } else if (fechaFiltro != null) {
                query.setParameter("fechaFiltro", java.sql.Date.valueOf(fechaFiltro));
            }

            total = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public static double obtenerTotalGasto(int id_alumno, LocalDate fechaFiltro, LocalDate fechaInicio, LocalDate fechaFin) {
        Double totalGasto = 0.0;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            String jpql = "SELECT SUM(p.costo_final) FROM PedidoBocadillo p WHERE p.alumno.id = :id";

            if (fechaInicio != null && fechaFin != null) {
                jpql += " AND p.fecha_hora BETWEEN :fechaInicio AND :fechaFin";
            } else if (fechaFiltro != null) {
                jpql += " AND p.fecha_hora >= :fechaFiltro";
            }

            Query<Double> query = session.createQuery(jpql, Double.class)
                    .setParameter("id", id_alumno);

            if (fechaInicio != null && fechaFin != null) {
                query.setParameter("fechaInicio", java.sql.Date.valueOf(fechaInicio));
                query.setParameter("fechaFin", java.sql.Date.valueOf(fechaFin));
            } else if (fechaFiltro != null) {
                query.setParameter("fechaFiltro", java.sql.Date.valueOf(fechaFiltro));
            }

            totalGasto = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalGasto != null ? totalGasto : 0.0;
    }








}
