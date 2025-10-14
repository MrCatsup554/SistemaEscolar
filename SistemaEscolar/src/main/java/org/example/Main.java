package org.example;

import com.jcraft.jsch.JSchException;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws JSchException, SQLException {
        ConexionBD bd = new ConexionBD();
        bd.selectPersonas(990); //
        //bd.insertPersonas("Alfredo", "Cruz", 'h', "2025-10-14", 1);
        bd.selectAsistencias(50);
        bd.selectMateria(50);
    }

}
