package Funciones;

import java.sql.*;
import java.util.ArrayList;


public class controlSesiones {
    public static void registrarSesion(int idEmpleado, Connection conn) {
        try {
            String sql =
                    "INSERT INTO control_ses(id_emp) " +
                            "SELECT id_emp FROM permiso_emp " +
                            "WHERE id_emp = ? AND (crud_pemp = TRUE OR admin_pemp = TRUE)";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idEmpleado);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al registrar sesión del empleado " + idEmpleado);
        }
    }
    public static ArrayList<Object[]> obtenerSesiones(Connection conn) {
        ArrayList<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT cs.id_ses, cs.id_emp, cs.fecha_ses,
                   e.nombre_emp, e.apellidop_emp, e.apellidom_emp
            FROM control_ses cs
            INNER JOIN empleado e ON cs.id_emp = e.id_emp
            ORDER BY cs.id_ses DESC;
        """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("id_ses"),
                                rs.getInt("id_emp"),
                                rs.getString("nombre_emp") + " " +
                                        rs.getString("apellidop_emp") + " " +
                                        rs.getString("apellidom_emp"),
                                rs.getDate("fecha_ses")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}

