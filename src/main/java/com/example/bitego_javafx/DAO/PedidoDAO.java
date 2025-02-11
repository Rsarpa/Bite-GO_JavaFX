package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.PedidoBocadillo;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
            String jpql = "FROM PedidoBocadillo pb WHERE FUNCTION('DATE', pb.fecha_hora) = CURRENT_DATE AND pb.retirado IS NULL";

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
                    //.setParameter("nombre", nombreAlu)
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

    public void realizarPedido(PedidoBocadillo pedido) {
        Transaction transaction = null;
        System.out.println("hermano estás entrando o no");
        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(pedido);
            transaction.commit();
            System.out.println("Todo realizado correctamente");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Que mierda está pasando");
            }
            e.printStackTrace();
            System.out.println("Error al realizar el pedido");
        }
    }



}
