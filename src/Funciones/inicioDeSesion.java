package Funciones;
import Datos.accesoBaseDeDatos;
import java.sql.*;

public class inicioDeSesion {
    public static boolean verificarContraseña(int idEmp, String contraseñaIngresada,Connection conn) {
        String sql = "SELECT contra_emp FROM empleado WHERE id_emp = ?";
        String contraseñaBD = "";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmp);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    contraseñaBD = rs.getString("contra_emp");
                } else {
                    System.out.println("No existe el empleado con id" + idEmp);
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar contraseña: " + e.getMessage());
            return false;
        }

        // Comparar la contraseña ingresada con la almacenada
        return contraseñaIngresada.equals(contraseñaBD);
    }
}
