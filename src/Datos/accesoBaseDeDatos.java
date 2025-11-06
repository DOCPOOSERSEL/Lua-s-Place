package Datos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class accesoBaseDeDatos {
    private static final String URL = "jdbc:postgresql://localhost:5432/Luas_Place";


    public static Connection getConnection(String USER, String PASSWORD) {
        try {
            Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a PostgreSQL");
            return conexion;
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}
