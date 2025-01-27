package com.example.bitego_javafx;

import java.sql.*;

public class Conexion {

    private static Connection connection=null;

    private Conexion(){}

    public static Connection getInstance() throws SQLException {
        if(connection==null){
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_bocadillos?user=root&password=");
                Class.forName("org.mysql.jdbc.Driver");
            }catch (SQLException | ClassNotFoundException e){
                System.out.println("Error al conectar con la base de datos");
            }
        }
        return connection;
    }
}
