package com.example.bitego_javafx.DAO;

import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Util.Conexion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UsuarioDAO {
    public UsuarioDAO() {}

    /**
     * Obtiene un objeto Alumno a partir de su dirección de correo electrónico.
     *
     * @param email Correo electrónico del alumno a buscar. No puede ser nulo ni vacío.
     * @return El objeto Alumno correspondiente al email proporcionado, o null si no se encuentra.
     * @throws RuntimeException Si ocurre un error en la consulta a la base de datos.
     *
     * Ejemplo de uso:
     * <pre>
     *     Alumno alumno = obtenerAlumnoPorEmail("correo@ejemplo.com");
     *     if (alumno != null) {
     *         System.out.println("Alumno encontrado: " + alumno.getNombre());
     *     }
     * </pre>
     */
    public static Alumno obtenerAlumnoPorEmail(String email) {
        Transaction transaction = null;
        Alumno alumno = null;

        try (Session session = Conexion.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<Alumno> query = session.createQuery(
                    "FROM Alumno WHERE email = :email", Alumno.class);
            query.setParameter("email", email);

            alumno = query.uniqueResult(); // Utilizamos uniqueResult para asegurarnos de que solo devuelva un Alumno

            transaction.commit(); // Confirmamos la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); //Evitamos inconsistencias si algo no ha salido bien
            }
            e.printStackTrace();
        }

        return alumno;
    }

}
