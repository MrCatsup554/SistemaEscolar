package org.example;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.sql.*;

public class ConexionBD {
    String hostname = "fi.jcaguilar.dev";
    String sshUser = "patito";
    String sshPass = "cuack";

    String dbUser = "becario";
    String dbPass = "FdI-its-5a";
    String conString;

    public ConexionBD() throws JSchException {
        JSch jsch = new JSch();

        //ssh patito@fi.jcaguilar.dev
        Session sesion = jsch.getSession(sshUser, hostname);

        //Introducir contraseña
        sesion.setPassword(sshPass);

        //Deshabilita los mensajes de error
        sesion.setConfig("StrictHostKeyChecking", "no");

        //Obtenemos un puerto redireccionando
        sesion.connect();
        int port = sesion.setPortForwardingL(0, "localhost", 3306);

        conString = "jdbc:mariadb://localhost:" + port + "/its5a";

        System.out.println("Conexion exitosa a la base de datos");
    }

    //PERSONAS
    public String selectPersona() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s | %-15s | %-15s | %-8s | %-12s | %-15s\n",
                "ID", "Nombre", "Apellido", "Sexo", "Fec. Nac.", "Rol"));
        sb.append("-".repeat(75));
        sb.append("\n");

        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql =
                    "select id_persona, nombre, apellido," +
                            "case when sexo = 'h' then 'Hombre' " +
                            "when sexo = 'm' then 'Mujer' " +
                            "else 'Otro' end as sexo," +
                            "fh_nac," +
                            "case " +
                            "when id_rol = 1 then 'Estudiante' " +
                            "when id_rol = 2 then 'Profesor' " +
                            "when id_rol = 3 then 'Directivo' " +
                            "else 'Administrativo' end as rol " +
                            "from personas_escuela " +
                            "order by id_persona desc;";
            ResultSet resultado = sentencia.executeQuery(sql);

            while (resultado.next()) {
                String id_persona = resultado.getString(1);
                String nombre = resultado.getString(2);
                String apellido = resultado.getString(3);
                String sexo = resultado.getString(4);
                String fh_nac = resultado.getString(5);
                String rol = resultado.getString(6);

                sb.append(String.format("%-5s | %-15s | %-15s | %-8s | %-12s | %-15s\n",
                        id_persona, nombre, apellido, sexo, fh_nac, rol));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
    public void insertPersonas(String nombre, String apellido, char sexo, String fh_nac, int id_rol) {

        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            PreparedStatement sentencia = con.prepareStatement(
                    "insert into personas_escuela(nombre, apellido, sexo, fh_nac, id_rol) values" +
                            "(?, ?, ?, ?, ?);"
            );
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setString(3, String.valueOf(sexo));
            sentencia.setString(4, fh_nac);
            sentencia.setInt(5, id_rol);

            int resultado = sentencia.executeUpdate();
            System.out.println("Filas afectadas (Personas): " + resultado);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //MATERIAS
    public String selectMateria() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s | %-30s | %-10s | %-10s\n", "ID", "Descripción", "Semestre", "Créditos"));
        sb.append("-".repeat(65));
        sb.append("\n");

        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql =
                    "select id_materia, descripcion, semestre, creditos from materias order by id_materia;";
            ResultSet resultado = sentencia.executeQuery(sql);

            while (resultado.next()) {
                int id_materia = resultado.getInt("id_materia");
                String descripcion = resultado.getString("descripcion");
                int semestre = resultado.getInt("semestre");
                int creditos = resultado.getInt("creditos");

                sb.append(String.format("%-5d | %-30s | %-10d | %-10d\n",
                        id_materia, descripcion, semestre, creditos));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public void insertMateria(String descripcion, int semestre, int creditos) {
        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            PreparedStatement sentencia = con.prepareStatement(
                    "insert into materias(descripcion, semestre, creditos) values " +
                            "(?, ?, ?);"
            );
            sentencia.setString(1, descripcion);
            sentencia.setInt(2, semestre);
            sentencia.setInt(3, creditos);

            int resultado = sentencia.executeUpdate();
            System.out.println("Filas afectadas (Materias): " + resultado);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //ASISTENCIAS
    public String selectAsistencias() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s | %-15s | %-15s\n", "ID Asistencia", "ID Inscripción", "Fecha"));
        sb.append("-".repeat(47));
        sb.append("\n");

        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql =
                    "select id_asistencia, id_inscripcion, fecha from asistencias order by id_asistencia desc;";
            ResultSet resultado = sentencia.executeQuery(sql);

            while (resultado.next()) {
                int id_asistencia = resultado.getInt("id_asistencia");
                int id_inscripcion = resultado.getInt("id_inscripcion");
                String fecha = resultado.getString("fecha");
                sb.append(String.format("%-12d | %-15d | %-15s\n", id_asistencia, id_inscripcion, fecha));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
    public void insertAsistencia(int id_inscripcion, String fecha) {
        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            PreparedStatement sentencia = con.prepareStatement(
                    "insert into asistencias(id_inscripcion, fecha) values (?, ?);"
            );
            sentencia.setInt(1, id_inscripcion);
            sentencia.setString(2, fecha);

            int resultado = sentencia.executeUpdate();
            System.out.println("Filas afectadas (Asistencias): " + resultado);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //INSCRIPCIONES
    public String selectInscripciones() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s | %-10s | %-12s | %-12s\n", "ID Inscripción", "ID Materia", "ID Estudiante", "Calificación"));
        sb.append("-".repeat(57));
        sb.append("\n");

        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql = "SELECT id_inscripcion, id_materia, id_estudiante, calificacion FROM inscripciones order by id_inscripcion;";
            ResultSet resultado = sentencia.executeQuery(sql);

            while (resultado.next()) {
                int id_inscripcionBD = resultado.getInt("id_inscripcion");
                int id_materia = resultado.getInt("id_materia");
                int id_estudiante = resultado.getInt("id_estudiante");
                int calificacion = resultado.getInt("calificacion");

                sb.append(String.format("%-15d | %-10d | %-12d | %-12d\n",
                        id_inscripcionBD, id_materia, id_estudiante, calificacion));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
    public void insertInscripcion(int id_materia, int id_estudiante, int calificacion) {
        try(Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            PreparedStatement sentencia = con.prepareStatement(
                    "insert into inscripciones(id_materia, id_estudiante, calificacion) " +
                            "values (?, ?, ?);"
            );
            sentencia.setInt(1, id_materia);
            sentencia.setInt(2, id_estudiante);
            sentencia.setInt(3, calificacion);

            int resultado = sentencia.executeUpdate();
            System.out.println("Filas afectadas (Inscripciones): " + resultado);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}