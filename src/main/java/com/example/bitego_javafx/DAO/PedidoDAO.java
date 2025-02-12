package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.PedidoBocadillo;
import com.example.bitego_javafx.Util.Conexion;
import com.sun.jna.platform.win32.OaIdl;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public PedidoDAO() {
    }

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
        Transaction transaction = null;
        System.out.println("hermano estás entrando o no");
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(pedido);
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


    public PedidoBocadillo obtenerPedidoDelDia(int id_alumno, Date fecha) {
        PedidoBocadillo pedidoHoy = null;
        Transaction transaction = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction(); // Inicia la transacción

            pedidoHoy = session.createQuery(
                            "FROM PedidoBocadillo WHERE alumno.id = :id AND DATE(fecha_hora) = :fechahoy",
                            PedidoBocadillo.class)
                    .setParameter("id", id_alumno)
                    .setParameter("fechahoy", fecha)
                    .setMaxResults(1)  // Para asegurarnos de obtener solo un resultado
                    .uniqueResult();   // Devuelve un solo objeto o null si no hay resultados

            transaction.commit(); // Confirma la transacción

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Revierte la transacción en caso de error
            }
            e.printStackTrace();
        }

        return pedidoHoy;
    }


    public Boolean cancelarPedido(int id_alumno,Date fecha){
        PedidoBocadillo pedidoHoy=obtenerPedidoDelDia(id_alumno,fecha);
        Transaction transaction = null;
        try (Session session=Conexion.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.delete(pedidoHoy);
            transaction.commit();
            System.out.println("Pedido eliminado correctamente");
            return true;
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Error al cancelar el pedido");
        }
        return false;
    }




}
