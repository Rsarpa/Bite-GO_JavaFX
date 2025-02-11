package com.example.bitego_javafx.Model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pedido_bocadillo")
public class PedidoBocadillo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pedido;

    @ManyToOne
    @JoinColumn(name = "id_bocadillo", nullable = false)
    private Bocadillo bocadillo;

    @Column(name = "fecha_hora", nullable = false)
    private Date fecha_hora;

    @Column(name = "retirado")
    private Date retirado;

    @Column(name = "costo_final", nullable = false)
    private Float costo_final;

    @Column(name = "id_descuento")
    private int id_descuento;

    public PedidoBocadillo(int id_pedido, Bocadillo bocadillo, Date fecha_hora, Date retirado, Float costo_final, int id_descuento) {
        this.id_pedido = id_pedido;
        this.bocadillo = bocadillo;
        this.fecha_hora = fecha_hora;
        this.retirado = retirado;
        this.costo_final = costo_final;
        this.id_descuento = id_descuento;
    }

    public PedidoBocadillo() {}

    public int getId_pedido() { return id_pedido; }
    public void setId_pedido(int id_pedido) { this.id_pedido = id_pedido; }

    public Bocadillo getBocadillo() { return bocadillo; }
    public void setBocadillo(Bocadillo bocadillo) { this.bocadillo = bocadillo; }

    public Date getFecha_hora() { return fecha_hora; }
    public void setFecha_hora(Date fecha_hora) { this.fecha_hora = fecha_hora; }

    public Date getRetirado() { return retirado; }
    public void setRetirado(Date retirado) { this.retirado = retirado; }

    public Float getCosto_final() { return costo_final; }
    public void setCosto_final(Float costo_final) { this.costo_final = costo_final; }

    public int getId_descuento() { return id_descuento; }
    public void setId_descuento(int id_descuento) { this.id_descuento = id_descuento; }
}
