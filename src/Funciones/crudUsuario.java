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

    // ------------------- BUSCAR -------------------
    public void buscarUsuario(Connection conn) {
        try {
            String sql = "SELECT * FROM empleado WHERE id_emp = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(vista.txtId.getText()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vista.txtContra.setText("********");
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

    // ------------------- AGREGAR -------------------
    public void agregarUsuario(Connection conn) {
        try {
            String contraseñaPlano = new String(vista.txtContra.getPassword());
            String contraseñaCifrada = inicioDeSesion.cifrar(contraseñaPlano);

            String rolSeleccionado = vista.getRolSeleccionado();

            // Obtener id del rol desde permiso_emp
            int idRol = -1;
            String sqlRol = "SELECT id_pemp FROM permiso_emp WHERE nombre_rol = ?";
            PreparedStatement psRol = conn.prepareStatement(sqlRol);
            psRol.setString(1, rolSeleccionado);
            ResultSet rsRol = psRol.executeQuery();
            if (rsRol.next()) idRol = rsRol.getInt("id_pemp");
            if (idRol == -1) throw new Exception("No se encontró el rol seleccionado");

            String sqlEmpleado = """
                INSERT INTO empleado
                (contra_emp, nombre_emp, apellidop_emp, apellidom_emp, fecha_contrato, fecha_nac_emp,
                 cp_emp, rfc_emp, calle_emp, col_emp, contrato_emp, rol_emp)
                VALUES (?,?,?,?,?,?,?,?,?,?,TRUE,?)
                RETURNING id_emp
            """;

            PreparedStatement ps = conn.prepareStatement(sqlEmpleado);
            ps.setString(1, contraseñaCifrada);
            ps.setString(2, vista.txtNombre.getText());
            ps.setString(3, vista.txtApP.getText());
            ps.setString(4, vista.txtApM.getText());
            ps.setDate(5, Date.valueOf(vista.txtFechaContrato.getText()));
            ps.setDate(6, Date.valueOf(vista.txtFechaNac.getText()));
            ps.setString(7, vista.txtCP.getText());
            ps.setString(8, vista.txtRFC.getText());
            ps.setString(9, vista.txtCalle.getText());
            ps.setString(10, vista.txtColonia.getText());
            ps.setInt(11, idRol);

            ResultSet rs = ps.executeQuery();
            int nuevoId = -1;
            if (rs.next()) nuevoId = rs.getInt("id_emp");

            JOptionPane.showMessageDialog(null,
                    "Empleado agregado con éxito.\nRol: " + rolSeleccionado + "\nID: " + nuevoId);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar: " + e.getMessage());
        }
    }

    // ------------------- ACTUALIZAR -------------------
    public void actualizarUsuario(Connection conn, int idUsuarioSesion) {
        try {
            int idEmpleadoModificar = Integer.parseInt(vista.txtId.getText());
            if (idEmpleadoModificar == 1 && idUsuarioSesion != 1) {
                JOptionPane.showMessageDialog(null, "No tienes permisos para modificar al super Usuario.");
                return;
            }

            String contraseñaPlano = new String(vista.txtContra.getPassword());
            String contraseñaCifrada = inicioDeSesion.cifrar(contraseñaPlano);

            String sql = """
                UPDATE empleado SET 
                contra_emp=?, nombre_emp=?, apellidop_emp=?, apellidom_emp=?, 
                fecha_contrato=?, fecha_nac_emp=?, cp_emp=?, rfc_emp=?, 
                calle_emp=?, col_emp=? 
                WHERE id_emp=?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, contraseñaCifrada);
            ps.setString(2, vista.txtNombre.getText());
            ps.setString(3, vista.txtApP.getText());
            ps.setString(4, vista.txtApM.getText());
            ps.setDate(5, Date.valueOf(vista.txtFechaContrato.getText()));
            ps.setDate(6, Date.valueOf(vista.txtFechaNac.getText()));
            ps.setString(7, vista.txtCP.getText());
            ps.setString(8, vista.txtRFC.getText());
            ps.setString(9, vista.txtCalle.getText());
            ps.setString(10, vista.txtColonia.getText());
            ps.setInt(11, idEmpleadoModificar);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empleado actualizado exitosamente");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
        }
    }

    // ------------------- ELIMINAR -------------------
    public void eliminarUsuario(Connection conn, int idUsuarioSesion) {
        try {
            int idEmpleadoEliminar = Integer.parseInt(vista.txtId.getText());
            if (idEmpleadoEliminar == 1 && idUsuarioSesion != 1) {
                JOptionPane.showMessageDialog(null, "No tienes permisos para desactivar al administrador.");
                return;
            }
            if (idEmpleadoEliminar == 1 && idUsuarioSesion == 1) {
                JOptionPane.showMessageDialog(null, "No puedes desactivar al super Usuario");
                return;
            }

            String sql = "UPDATE empleado SET contrato_emp = FALSE WHERE id_emp = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idEmpleadoEliminar);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Empleado desactivado (contrato en FALSE).");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al desactivar: " + e.getMessage());
        }
    }

    // ------------------- PERMISOS -------------------

    // Devuelve lista de roles con sus permisos desde permiso_emp + permiso_detalle
    public ArrayList<Object[]> obtenerRoles(Connection conn) {
        ArrayList<Object[]> roles = new ArrayList<>();
        try {
            String sql = """
                SELECT p.id_pemp, p.nombre_rol, d.venta_perm, d.inventario_perm, d.crud_perm, d.admin_perm
                FROM permiso_emp p
                LEFT JOIN permiso_detalle d ON p.id_pemp = d.id_pemp
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                roles.add(new Object[]{
                        rs.getInt("id_pemp"),
                        rs.getString("nombre_rol"),
                        rs.getBoolean("venta_perm"),
                        rs.getBoolean("inventario_perm"),
                        rs.getBoolean("crud_perm"),
                        rs.getBoolean("admin_perm")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener roles: " + e.getMessage());
        }
        return roles;
    }

    // Devuelve lista de usuarios con su rol (id, nombreCompleto, rol)
    public ArrayList<Object[]> obtenerPermisosUsuarios(Connection conn) {
        ArrayList<Object[]> usuarios = new ArrayList<>();
        try {
            String sql = """
                SELECT u.id_emp, u.nombre_emp, u.apellidop_emp, u.apellidom_emp, p.nombre_rol
                FROM empleado u
                LEFT JOIN permiso_emp p ON u.rol_emp = p.id_pemp
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuarios.add(new Object[]{
                        rs.getInt("id_emp"),
                        rs.getString("nombre_emp") + " " + rs.getString("apellidop_emp") + " " + rs.getString("apellidom_emp"),
                        rs.getString("nombre_rol")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Actualiza rol de un usuario
    public void actualizarRolUsuario(Connection conn, int idEmp, String nuevoRol) {
        try {
            String sqlIdRol = "SELECT id_pemp FROM permiso_emp WHERE nombre_rol=?";
            PreparedStatement ps1 = conn.prepareStatement(sqlIdRol);
            ps1.setString(1, nuevoRol);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                int idRol = rs.getInt("id_pemp");
                String sqlUpdate = "UPDATE empleado SET rol_emp=? WHERE id_emp=?";
                PreparedStatement ps2 = conn.prepareStatement(sqlUpdate);
                ps2.setInt(1, idRol);
                ps2.setInt(2, idEmp);
                ps2.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Actualiza permisos de un rol
    public void actualizarPermisosRol(Connection conn, int idRol, boolean venta, boolean inventario, boolean crud, boolean admin) {
        try {
            String sqlCheck = "SELECT id_det FROM permiso_detalle WHERE id_pemp=?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setInt(1, idRol);
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next()) {
                // Actualizar
                String sqlUpdate = "UPDATE permiso_detalle SET venta_perm=?, inventario_perm=?, crud_perm=?, admin_perm=? WHERE id_pemp=?";
                PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                psUpdate.setBoolean(1, venta);
                psUpdate.setBoolean(2, inventario);
                psUpdate.setBoolean(3, crud);
                psUpdate.setBoolean(4, admin);
                psUpdate.setInt(5, idRol);
                psUpdate.executeUpdate();
            } else {
                // Insertar si no existe
                String sqlInsert = "INSERT INTO permiso_detalle (id_pemp, venta_perm, inventario_perm, crud_perm, admin_perm) VALUES (?,?,?,?,?)";
                PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                psInsert.setInt(1, idRol);
                psInsert.setBoolean(2, venta);
                psInsert.setBoolean(3, inventario);
                psInsert.setBoolean(4, crud);
                psInsert.setBoolean(5, admin);
                psInsert.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
