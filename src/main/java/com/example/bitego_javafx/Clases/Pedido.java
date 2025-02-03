package com.example.bitego_javafx.Clases;

import com.example.bitego_javafx.Conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private int id_pedido;
    private int id_alumno;
    private String id_bocadillo;
    private Date fecha_hora;
    private Date retirado;
    private Float costo_final;
    private int id_descuento;

    public Pedido(int id_pedido, int id_alumno, String id_bocadillo, Date fecha_hora, Date retirado, Float costo_final, int id_descuento) {
        this.id_pedido = id_pedido;
        this.id_alumno = id_alumno;
        this.id_bocadillo = id_bocadillo;
        this.fecha_hora = fecha_hora;
        this.retirado = retirado;
        this.costo_final = costo_final;
        this.id_descuento = id_descuento;
    }

    public Pedido(){}

    public static List<Pedido> listarPedidos(String nombreAlu, String apellidoAlu, String curso) throws SQLException {

        List<Pedido> listaPedidos = new ArrayList<>();

        Connection connection = null;
        connection = Conexion.getInstance();

        if (connection != null){
            System.out.println("Conexión con la BD");
            String sql = "select a.nombre, a.apellidos, a.id_curso, b.tipo, pb.id_pedido from pedido_bocadillo pb, alumno a, bocadillo b where pb.id_alumno = a.id_alumno = a.id_alumno AND pb.id_bocadillo = b.nombre AND DATE(pb.fecha_hora) = CURDATE() AND pb.retirado IS null";

            if (nombreAlu != ""){
                sql = sql + "and a.nombre = " + nombreAlu;
            }

            if (apellidoAlu != ""){
                sql = sql + "and a.apellidos = " + apellidoAlu;
            }

            if (apellidoAlu != ""){
                sql = sql + "and a.id_curso = " + curso;
            }

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                String nombreAlumno = rs.getString("nombre") + rs.getString("apellido");
                curso = rs.getString("id_curso");
                String tipo = rs.getString("tipo");


            }
        }
    }
}
