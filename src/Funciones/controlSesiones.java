package Funciones;

import java.sql.*;
import java.util.ArrayList;

public class controlSesiones {

    public static void registrarSesion(int idEmpleado, Connection conn) {
        try {
            String sql = """
                INSERT INTO control_ses (id_emp)
                SELECT e.id_emp
                FROM empleado e
                INNER JOIN permiso_detalle pd ON e.rol_emp = pd.id_pemp
                WHERE e.id_emp = ? AND (pd.crud_perm = TRUE OR pd.admin_perm = TRUE)
            """;

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idEmpleado);
            int filasInsertadas = pst.executeUpdate();

            if (filasInsertadas > 0) {
                System.out.println("Sesión registrada correctamente para empleado " + idEmpleado);
            } else {
                System.out.println("Empleado " + idEmpleado + " no tiene permisos para registrar sesión.");
            }

        } catch (SQLException e) {
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
            ORDER BY cs.id_ses DESC
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

    // =======================================================
    // MÉTODO PARA VERIFICAR PERMISO
    // =======================================================
    public static boolean tienePermiso(int idEmpleado, String permiso, Connection conn) {
        String columna;
        switch (permiso.toLowerCase()) {
            case "venta":
                columna = "venta_perm";
                break;
            case "inventario":
                columna = "inventario_perm";
                break;
            case "crud":
                columna = "crud_perm";
                break;
            case "admin":
                columna = "admin_perm";
                break;
            default:
                throw new IllegalArgumentException("Permiso no válido: " + permiso);
        }

        String sql = """
            SELECT pd.%s
            FROM empleado e
            INNER JOIN permiso_detalle pd ON e.rol_emp = pd.id_pemp
            WHERE e.id_emp = ?
        """.formatted(columna);

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idEmpleado);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Si ocurre un error o no existe, retorna false
    }
}
