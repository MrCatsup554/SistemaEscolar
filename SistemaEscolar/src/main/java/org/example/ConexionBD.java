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

    public ConexionBD() throws JSchException{
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

    public String selectPersona(){
        StringBuilder sb = new StringBuilder();
        try(Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
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

            while(resultado.next()){
                String id_persona = resultado.getString(1);
                String nombre = resultado.getString(2);
                String apellido = resultado.getString(3);
                String sexo = resultado.getString(4);
                String fh_nac = resultado.getString(5);
                String rol = resultado.getString(6);

                sb.append(String.format("%-5s | %-15s | %-15s | %-8s | %-12s | %-15s\n",
                        id_persona, nombre, apellido, sexo, fh_nac, rol));

                //System.out.println(sb.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public void selectMateria(int id_materia){
        try(Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql =
                    "select descripcion, semestre, creditos from materias where id_materia = " + id_materia + ";";
            ResultSet resultado = sentencia.executeQuery(sql);


            while(resultado.next()){
                String descripcion = resultado.getString(1);
                String semestre = resultado.getString(2);
                String creditos = resultado.getString(2);
                System.out.println("Materias: " + descripcion + " " + semestre + " " + creditos);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void selectAsistencias(int id_asistencia) {
        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql =
                    "select id_inscripcion, fecha  from asistencias  where id_asistencia= " + id_asistencia + ";";
            ResultSet resultado = sentencia.executeQuery(sql);

            while (resultado.next()) {
                String id_inscripcion = resultado.getString(1);
                String fecha = resultado.getString(2);
                System.out.println("Asistencia: " + id_inscripcion + " " + fecha);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void selectInscripciones(int id_inscripcion) {
        try (Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();
            String sql = "SELECT * FROM inscripciones;";
            ResultSet resultado = sentencia.executeQuery(sql);

            System.out.println("----Tabla Inscripciones----");
            while (resultado.next()) {
                int id_inscripcionBD = resultado.getInt("id_inscripcion");
                int id_materia = resultado.getInt("id_materia");
                int id_estudiante = resultado.getInt("id_estudiante");
                int calificacion = resultado.getInt("calificacion");

                System.out.println(
                        "ID: " + id_inscripcionBD +
                                " | Materia: " +  id_materia +
                                " | Estudiante: " + id_estudiante +
                                " | Calificacion: " + calificacion
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertPersonas(String nombre, String apellido, char sexo, String fh_nac, int id_rol){

        try(Connection con = DriverManager.getConnection(conString, dbUser, dbPass)) {
            Statement sentencia = con.createStatement();

            String sqlInsert =
                    "insert into personas_escuela(nombre, apellido, sexo, fh_nac, id_rol) values(" +
                    "'" + nombre + "'," +
                    "'" + apellido + "'," +
                    "'" + sexo + "'," +
                    "'" + fh_nac + "'," +
                    id_rol + ");";

            int filasAfectadas = sentencia.executeUpdate(sqlInsert);
            System.out.println("Se cambiaron " + filasAfectadas + " filas");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}