package org.example;

import com.jcraft.jsch.JSchException;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws JSchException {
        ConexionBD bd = new ConexionBD();
        //Operaciones select
        bd.selectPersonas(990);
        bd.selectAsistencias(121);
        bd.selectMateria(50);
        bd.selectInscripciones(50);

        //Operaciones insert
        //bd.insertPersonas("Alfredo", "Cruz", 'h', "2025-10-14", 1);
    }

}
