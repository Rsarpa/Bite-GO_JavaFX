package com.example.bitego_javafx.Model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Year;
import java.util.Date;

public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_curso;

    @Column(name = "nombre_curso", nullable = false)
    private String nombre_curso;

    @Column(name = "anyo", nullable = false)
    private Year anyo;
}
