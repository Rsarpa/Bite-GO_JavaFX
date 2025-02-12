package com.example.bitego_javafx.Model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pedido_bocadillo")
public class PedidoBocadillo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private int id_pedido;

    // Relación con Alumno (Un alumno puede tener varios pedidos)
    @ManyToOne
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;

    // Relación con Bocadillo (Un bocadillo puede estar en varios pedidos)
    @ManyToOne
    @JoinColumn(name = "id_bocadillo", nullable = false)
    private Bocadillo bocadillo;

    @Column(name = "fecha_hora", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_hora;

    @Column(name = "retirado", nullable = false)
    private Boolean retirado;

    @Column(name = "costo_final", nullable = false)
    private Float costo_final;

    @Column(name = "id_descuento")
    private Integer id_descuento;  // Cambiado de int a Integer para permitir null

    // 🔹 Constructor sin id_pedido (Hibernate lo maneja)
    public PedidoBocadillo(Alumno alumno, Bocadillo bocadillo, Date fecha_hora, Boolean retirado, Float costo_final, Integer id_descuento) {
        this.alumno = alumno;
        this.bocadillo = bocadillo;
        this.fecha_hora = fecha_hora;
        this.retirado = retirado;
        this.costo_final = costo_final;
        this.id_descuento = id_descuento;
    }

    public PedidoBocadillo() {}

    // Getters y Setters
    public int getId_pedido() { return id_pedido; }
    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }
    public Bocadillo getBocadillo() { return bocadillo; }
    public void setBocadillo(Bocadillo bocadillo) { this.bocadillo = bocadillo; }
    public Date getFecha_hora() { return fecha_hora; }
    public void setFecha_hora(Date fecha_hora) { this.fecha_hora = fecha_hora; }
    public Boolean getRetirado() { return retirado; }
    public void setRetirado(Boolean retirado) { this.retirado = retirado; }
    public Float getCosto_final() { return costo_final; }
    public void setCosto_final(Float costo_final) { this.costo_final = costo_final; }
    public Integer getId_descuento() { return id_descuento; }
    public void setId_descuento(Integer id_descuento) { this.id_descuento = id_descuento; }
}