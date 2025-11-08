package Datos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class accesoBaseDeDatos {
    private static final String URL = "jdbc:postgresql://localhost:5432/Luas_Place";
    private static final String USER = "postgres";
    private static final String PASSWORD = "09030521";

    private static Connection conexion = null;

    // Método para obtener la conexión y con esa operar sobre la base de datos
    public static Connection getConnection() {
        if (conexion != null) {
            return conexion; // si ya está abierta, la devuelve
        }

        try {
            // Evita un error por la conexion
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC de PostgreSQL no encontrado");
        }

        return conexion;
    }

    // Método para cerrar la conexión
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
