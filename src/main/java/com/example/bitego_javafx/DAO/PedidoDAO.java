package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.PedidoBocadillo;
import com.example.bitego_javafx.Util.Conexion;
import com.sun.jna.platform.win32.OaIdl;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public PedidoDAO() {
    }

    //Lista todos los bocadillos , Utilizado en Cocina
    public List<PedidoBocadillo> listarPedidos(String nombreAlu, String apellidoAlu, String curso) throws SQLException {

        //todo pendiente
        List<PedidoBocadillo> listaPedidos = new ArrayList<>();
        Transaction transaction = null;

        try(Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Conexión con la BD");
            String jpql = "FROM PedidoBocadillo pb WHERE FUNCTION('DATE', pb.fecha_hora) = CURRENT_DATE AND pb.retirado = false";

            /*if (nombreAlu != null && !nombreAlu.isEmpty()) {
                jpql = jpql + " AND a.nombre = :nombreAlu";
            }
            if (apellidoAlu != null && !apellidoAlu.isEmpty()) {
                jpql = jpql + " AND a.apellidos = :apellidoAlu";
            }
            if (curso != null && !curso.isEmpty()) {
                jpql = jpql = " AND a.id_curso = :curso";
            }*/

            listaPedidos = session.createQuery (jpql, PedidoBocadillo.class)
                    //.setParameter("nombreAlu", nombreAlu)
                    //.setParameter("apellidoAlu", apellidoAlu)
                    //.setParameter("curso", curso)
                    .getResultList();


        }catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return listaPedidos;
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
    public static List<PedidoBocadillo> obtenerPedidosDelAlumno(int id_alumno, int page, int offset, LocalDate fechaFiltro) {
        List<PedidoBocadillo> lista_pedidos_alumno = null;
        Transaction transaction = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String jpql = "FROM PedidoBocadillo WHERE alumno.id = :id AND fecha_hora >= :fechaFiltro ORDER BY fecha_hora DESC";
            lista_pedidos_alumno = session.createQuery(jpql, PedidoBocadillo.class)
                    .setParameter("id", id_alumno)
                    .setParameter("fechaFiltro", java.sql.Date.valueOf(fechaFiltro))
                    .setFirstResult((page - 1) * offset)
                    .setMaxResults(offset)
                    .getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return lista_pedidos_alumno;
    }
    public static long obtenerTotalPedidos(int id_alumno, LocalDate fechaFiltro) {
        long total = 0;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            String jpql = "SELECT COUNT(p) FROM PedidoBocadillo p WHERE p.alumno.id = :id AND p.fecha_hora >= :fechaFiltro";
            total = session.createQuery(jpql, Long.class)
                    .setParameter("id", id_alumno)
                    .setParameter("fechaFiltro", java.sql.Date.valueOf(fechaFiltro))
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public static double obtenerTotalGasto(int id_alumno, LocalDate fechaFiltro) {
        Double totalGasto = 0.0;
        try (Session session = Conexion.getSessionFactory().openSession()) {
            String jpql = "SELECT SUM(p.costo_final) FROM PedidoBocadillo p WHERE p.alumno.id = :id AND p.fecha_hora >= :fechaFiltro";
            totalGasto = session.createQuery(jpql, Double.class)
                    .setParameter("id", id_alumno)
                    .setParameter("fechaFiltro", java.sql.Date.valueOf(fechaFiltro))
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalGasto != null ? totalGasto : 0.0;
    }







}
