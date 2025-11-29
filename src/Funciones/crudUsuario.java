package Funciones;

import Interfaz.crudUsuarioInterfaz;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class crudUsuario {
    private crudUsuarioInterfaz vista;

    public crudUsuario(crudUsuarioInterfaz vista) {
        this.vista = vista;
    }

    public void buscarUsuario(Connection conn) {
        try {
            String sql = "SELECT * FROM empleado WHERE id_emp = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(vista.txtId.getText()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vista.txtContra.setText(rs.getString("contra_emp"));
                vista.txtNombre.setText(rs.getString("nombre_emp"));
                vista.txtApP.setText(rs.getString("apellidop_emp"));
                vista.txtApM.setText(rs.getString("apellidom_emp"));
                vista.txtFechaContrato.setText(rs.getString("fecha_contrato"));
                vista.txtFechaNac.setText(rs.getString("fecha_nac_emp"));
                vista.txtCP.setText(rs.getString("cp_emp"));
                vista.txtRFC.setText(rs.getString("rfc_emp"));
                vista.txtCalle.setText(rs.getString("calle_emp"));
                vista.txtColonia.setText(rs.getString("col_emp"));
                JOptionPane.showMessageDialog(null, "Empleado encontrado");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el empleado");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + e.getMessage());
        }
    }

    public void agregarUsuario(Connection conn) {
        try {
            String sqlEmpleado = """
            INSERT INTO empleado 
            (contra_emp, nombre_emp, apellidop_emp, apellidom_emp, fecha_contrato, fecha_nac_emp,
             cp_emp, rfc_emp, calle_emp, col_emp)
            VALUES (?,?,?,?,?,?,?,?,?,?)
            RETURNING id_emp
        """;

            PreparedStatement ps = conn.prepareStatement(sqlEmpleado);
            ps.setString(1, new String(vista.txtContra.getPassword()));
            ps.setString(2, vista.txtNombre.getText());
            ps.setString(3, vista.txtApP.getText());
            ps.setString(4, vista.txtApM.getText());
            ps.setDate(5, Date.valueOf(vista.txtFechaContrato.getText()));
            ps.setDate(6, Date.valueOf(vista.txtFechaNac.getText()));
            ps.setString(7, vista.txtCP.getText());
            ps.setString(8, vista.txtRFC.getText());
            ps.setString(9, vista.txtCalle.getText());
            ps.setString(10, vista.txtColonia.getText());

            ResultSet rs = ps.executeQuery();
            int nuevoId = -1;

            if (rs.next()) {
                nuevoId = rs.getInt("id_emp");
            }
            if (nuevoId == -1) {
                throw new Exception("No se pudo obtener el ID del empleado.");
            }

            String rol = vista.getRolSeleccionado();

            // Variables de permisos
            boolean venta = false;
            boolean inventario = false;
            boolean crud = false;
            boolean admin = false;

            switch (rol) {
                case "Empleado":
                    venta = false;
                    inventario = true;
                    crud = false;
                    admin = false;
                    break;

                case "Cajero":
                    venta = true;
                    inventario = false;
                    crud = false;
                    admin = false;
                    break;

                case "Manager":
                    venta = true;
                    inventario = true;
                    crud = true;
                    admin = true;
                    break;
            }

            String sqlPermiso = """
            INSERT INTO permiso_emp(id_emp, venta_pmp, inventario_pmp, crud_pmp, admin_pmp)
            VALUES (?,?,?,?,?)
        """;

            PreparedStatement psPermiso = conn.prepareStatement(sqlPermiso);
            psPermiso.setInt(1, nuevoId);
            psPermiso.setBoolean(2, venta);
            psPermiso.setBoolean(3, inventario);
            psPermiso.setBoolean(4, crud);
            psPermiso.setBoolean(5, admin);

            psPermiso.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Empleado agregado con éxito.\nRol: " + rol + "\nID: " + nuevoId);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar: " + e.getMessage());
        }
    }

    public void actualizarUsuario(Connection conn) {
        try {
            String sql = """
                UPDATE empleado SET 
                contra_emp=?, nombre_emp=?, apellidop_emp=?, apellidom_emp=?, 
                fecha_contrato=?, fecha_nac_emp=?, cp_emp=?, rfc_emp=?, 
                calle_emp=?, col_emp=? WHERE id_emp=?
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, new String(vista.txtContra.getPassword()));
            ps.setString(2, vista.txtNombre.getText());
            ps.setString(3, vista.txtApP.getText());
            ps.setString(4, vista.txtApM.getText());
            ps.setDate(5, Date.valueOf(vista.txtFechaContrato.getText()));
            ps.setDate(6, Date.valueOf(vista.txtFechaNac.getText()));
            ps.setString(7, vista.txtCP.getText());
            ps.setString(8, vista.txtRFC.getText());
            ps.setString(9, vista.txtCalle.getText());
            ps.setString(10, vista.txtColonia.getText());
            ps.setInt(11, Integer.parseInt(vista.txtId.getText()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Empleado actualizado exitosamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarUsuario(Connection conn) {
        try {
            String sql = "DELETE FROM empleado WHERE id_emp = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(vista.txtId.getText()));
            if (vista.txtId.getText().equals("1")){
                JOptionPane.showMessageDialog(null, "No se puede eliminar el administrador");
                return;
            }
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empleado eliminado exitosamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
    }

    //Esto para obtener los permisos de los usuarios y guardarlos para modifiacr
    public ArrayList<Object[]> obtenerPermisosUsuarios(Connection conn) {
        ArrayList<Object[]> lista = new ArrayList<>();
        String sql = """
        SELECT e.id_emp, 
               e.nombre_emp, 
               e.apellidop_emp, 
               e.apellidom_emp,
               p.venta_pemp,
               p.inventario_pemp,
               p.crud_pemp,
               p.admin_pemp
        FROM empleado e
        INNER JOIN permiso_emp p ON e.id_emp = p.id_emp
        ORDER BY e.id_emp;
    """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("id_emp"),
                        rs.getString("nombre_emp") + " " +
                                rs.getString("apellidop_emp") + " " +
                                rs.getString("apellidom_emp"),
                        rs.getBoolean("venta_pemp"),
                        rs.getBoolean("inventario_pemp"),
                        rs.getBoolean("crud_pemp"),
                        rs.getBoolean("admin_pemp")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener permisos: " + e.getMessage());
        }
        return lista;
    }


    public void actualizarPermisos(Connection conn, int idEmp, boolean venta, boolean inventario, boolean crud, boolean admin) {
        String sql = """
        UPDATE permiso_emp 
        SET venta_pemp = ?, inventario_pemp = ?, crud_pemp = ?, admin_pemp = ?
        WHERE id_emp = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, venta);
            ps.setBoolean(2, inventario);
            ps.setBoolean(3, crud);
            ps.setBoolean(4, admin);
            ps.setInt(5, idEmp);

            ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar permisos: " + e.getMessage());
        }
    }



}
