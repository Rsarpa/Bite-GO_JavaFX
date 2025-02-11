package com.example.bitego_javafx.Model;

import jakarta.persistence.*;

import java.time.Year;
import java.util.Date;
@Entity
@Table(name="curso") //Asegurate de que coincide con el nombre de la tabla BD
public class Curso {
    public Curso() {
    }

    public Curso(int id_curso, String nombre_curso, Year anyo) {
        this.id_curso = id_curso;
        this.nombre_curso = nombre_curso;
        this.anyo = anyo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_curso;

    @Column(name = "nombre_curso", nullable = false)
    private String nombre_curso;

    @Column(name = "anyo", nullable = false)
    private Year anyo;
}
