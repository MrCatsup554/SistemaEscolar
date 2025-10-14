package org.example;

import com.jcraft.jsch.JSchException;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws JSchException, SQLException {
        ConexionBD bd = new ConexionBD();
        bd.selectPersonas(50);
    }
}