package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestorBD {

    private final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private final String USER = "PROYECTO_CONDOMINIOS";
    private final String PASSWORD = "root123";

    private Connection conexion = null;

    public GestorBD() {
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("ERROR SQLException:  " + e);

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR ClassNotFoundException:  " + e);
        }
    }

    public Connection getConexion() {
         try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("ERROR SQLException:  " + e);

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR ClassNotFoundException:  " + e);
        }
         return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public void cerrar() {
        try {
            conexion.close();
            if (conexion.isClosed() == true) {
                System.out.println("CONEXION CERRADA CON EXITO");
            } else {
                System.out.println("CONEXION NO CERRADA");
            }
        } catch (SQLException e) {
            System.err.println("ERROR SQLException:  " + e);
        } finally {
            conexion = null;
        }
    }
}
