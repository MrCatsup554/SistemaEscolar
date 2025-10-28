package org.example;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    public ObservableList<Personas> selectPersonas() {
        ObservableList<Personas> listaPersonas = FXCollections.observableArrayList();

        // Asegúrate de que conString, dbUser, dbPass estén definidos.
        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql =
                    "select id_persona, nombre, apellido," +
                            "case when sexo = 'h' then 'Hombre' when sexo = 'm' then 'Mujer' else 'Otro' end as sexo," +
                            "fh_nac, " +
                            "case when id_rol = 1 then 'Estudiante' when id_rol = 2 then 'Profesor' when id_rol = 3 then 'Directivo' else 'Administrativo' end as rol " +
                            "from personas_escuela order by id_persona desc;";

            ResultSet resultado = sentencia.executeQuery(sql);

            while (resultado.next()) {
                // Recoger los datos usando el índice de columna
                int id = resultado.getInt(1);
                String nombre = resultado.getString(2);
                String apellido = resultado.getString(3);
                String sexo = resultado.getString(4);
                String fh_nac = resultado.getString(5);
                String rol = resultado.getString(6);

                // Crear el objeto Persona y añadirlo a la lista
                listaPersonas.add(new Personas(id, nombre, apellido, sexo, fh_nac, rol));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar datos de la BD: " + e.getMessage());
        }
        return listaPersonas;
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
    public ObservableList<Materias> selectMaterias() {
        ObservableList<Materias> listaMaterias = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();

            String sql =
                    "select id_materia, descripcion, semestre, creditos from materias " +
                            "order by id_materia desc;";

            ResultSet resultado = sentencia.executeQuery(sql);

            while (resultado.next()) {
                int id_materia = resultado.getInt("id_materia");
                String descripcion = resultado.getString("descripcion");
                int semestre = resultado.getInt("semestre");
                int creditos = resultado.getInt("creditos");

                Materias nuevaMateria = new Materias(id_materia, descripcion, semestre, creditos);

                listaMaterias.add(nuevaMateria);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar datos de materias: " + e.getMessage());
            return FXCollections.observableArrayList();
        }

        return listaMaterias;
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
    public ObservableList<Asistencias> selectAsistencias() {
        ObservableList<Asistencias> listaAsistencias = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql =
                    "select id_asistencia, id_inscripcion, fecha from asistencias " +
                            "order by id_asistencia desc;";
            ResultSet resultado = sentencia.executeQuery(sql);

            while (resultado.next()) {
                int id_asistencia = resultado.getInt("id_asistencia");
                int id_inscripcion = resultado.getInt("id_inscripcion");
                String fecha = resultado.getString("fecha");

                // Crear el objeto Asistencias (clase modelo en plural)
                Asistencias nuevaAsistencia = new Asistencias(id_asistencia, id_inscripcion, fecha);

                // Añadir a la lista observable
                listaAsistencias.add(nuevaAsistencia);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar datos de asistencias: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
        return listaAsistencias;
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
    public ObservableList<Inscripciones> selectInscripciones() {
        ObservableList<Inscripciones> listaInscripciones = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql = "SELECT id_inscripcion, id_materia, id_estudiante, calificacion FROM inscripciones " +
                    "order by id_inscripcion desc;";
            ResultSet resultado = sentencia.executeQuery(sql);

            while (resultado.next()) {
                int id_inscripcionBD = resultado.getInt("id_inscripcion");
                int id_materia = resultado.getInt("id_materia");
                int id_estudiante = resultado.getInt("id_estudiante");
                int calificacion = resultado.getInt("calificacion");

                // Crear el objeto Inscripciones y añadirlo a la lista
                Inscripciones nuevaInscripcion = new Inscripciones(id_inscripcionBD, id_materia, id_estudiante, calificacion);
                listaInscripciones.add(nuevaInscripcion);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar datos de inscripciones: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
        return listaInscripciones;
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