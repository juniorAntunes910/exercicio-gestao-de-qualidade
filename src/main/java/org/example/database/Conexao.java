package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static String URL = "jdbc:postgresql://localhost:5456/gestao_qualidade";

    private static String USER = "junior";

    private static String PASSWORD = "junior";
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}


//package org.example.util;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class ConexaoBanco {
//    private static final String URL = "jdbc:postgresql://localhost:5456/java_com_testes";
//    private static final String USER = "junior";
//    private static final String PASSWORD = "junior";
//
//    public static Connection conectar() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//}