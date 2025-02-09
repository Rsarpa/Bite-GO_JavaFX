package com.example.bitego_javafx.Model;

import java.util.Date;

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

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public void setId_alumno(int id_alumno) {
        this.id_alumno = id_alumno;
    }

    public void setId_bocadillo(String id_bocadillo) {
        this.id_bocadillo = id_bocadillo;
    }

    public void setFecha_hora(Date fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public void setRetirado(Date retirado) {
        this.retirado = retirado;
    }

    public void setCosto_final(Float costo_final) {
        this.costo_final = costo_final;
    }

    public void setId_descuento(int id_descuento) {
        this.id_descuento = id_descuento;
    }


}
