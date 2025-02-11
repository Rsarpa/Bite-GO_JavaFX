package com.example.bitego_javafx.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "alumno")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_alumno;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "localidad", nullable = false)
    private String localidad;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = true)
    private Curso curso;

    @Column(name = "motivo_baja", columnDefinition = "TEXT", nullable = true)
    private String motivo_baja;

    @Column(name = "abonado", nullable = false)
    private boolean abonado;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<PedidoBocadillo> pedidos;

    public Alumno(int id_alumno, String nombre, String apellidos, String dni, String localidad, String email, Curso curso, String motivo_baja, boolean abonado) {
        this.id_alumno = id_alumno;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.localidad = localidad;
        this.email = email;
        this.curso = curso;
        this.motivo_baja = motivo_baja;
        this.abonado = abonado;
    }

    public Alumno() {}

    public int getId_alumno() {
        return id_alumno;
    }

    public void setId_alumno(int id_alumno) {
        this.id_alumno = id_alumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getMotivo_baja() {
        return motivo_baja;
    }

    public void setMotivo_baja(String motivo_baja) {
        this.motivo_baja = motivo_baja;
    }

    public boolean isAbonado() {
        return abonado;
    }

    public void setAbonado(boolean abonado) {
        this.abonado = abonado;
    }

    public List<PedidoBocadillo> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidoBocadillo> pedidos) {
        this.pedidos = pedidos;
    }
}
