package Funciones;

import Interfaz.crudUsuarioInterfaz;
import javax.swing.*;
import java.sql.*;

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
            String sql = """
                INSERT INTO empleado 
                (contra_emp, nombre_emp, apellidop_emp, apellidom_emp, fecha_contrato, fecha_nac_emp,
                 cp_emp, rfc_emp, calle_emp, col_emp)
                VALUES (?,?,?,?,?,?,?,?,?,?)
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
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Empleado agregado exitosamente");
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
}
