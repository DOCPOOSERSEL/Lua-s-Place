package Funciones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class inventarioFuncion{

    // ============================================================
    // CARGAR TABLA INVENTARIO
    // ============================================================

    public void cargarInventario(Connection conn, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        String sql = "SELECT id_inv, nombre_inv, cantidad_inv, costo_inv FROM inventario ORDER BY id_inv ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id_inv"),
                        rs.getString("nombre_inv"),
                        rs.getInt("cantidad_inv"),
                        rs.getDouble("costo_inv")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar inventario.");
        }
    }

    // ============================================================
    // AGREGAR CANTIDAD
    // ============================================================

    public void agregarCantidad(Connection conn, int idInv, int cantidad) {
        String sql = "UPDATE inventario SET cantidad_inv = cantidad_inv + ? WHERE id_inv = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idInv);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cantidad agregada.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar cantidad.");
        }
    }

    // ============================================================
    // RESTAR CANTIDAD
    // ============================================================

    public void restarCantidad(Connection conn, int idInv, int cantidad) {
        String sql = "UPDATE inventario SET cantidad_inv = GREATEST(cantidad_inv - ?, 0) WHERE id_inv = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idInv);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cantidad reducida.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al reducir cantidad.");
        }
    }


    public void agregarInventario(Connection conn, String nombre, int cantidad, double costo, int idProv) {

        String sql = "INSERT INTO inventario (nombre_inv, cantidad_inv, costo_inv, id_prov) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, cantidad);
            ps.setDouble(3, costo);
            ps.setInt(4, idProv);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Nuevo producto agregado al inventario.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar producto al inventario.");
        }
    }

    public void cargarProveedores(Connection conn, JComboBox<String> combo) {

        combo.removeAllItems();

        String sql = "SELECT id_prov, nombre_prov FROM proveedor ORDER BY nombre_prov ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                combo.addItem(rs.getString("nombre_prov"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar proveedores.");
        }
    }

    public void agregarProveedor(Connection conn, String nombre, String calle, String ciudad, String cp, String tel, String fisMor) {
        String sql = "INSERT INTO proveedor (nombre_prov, calle_prov, ciudad_prov, cp_prov, telefono_prov, fis_mor_prov) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, calle);
            ps.setString(3, ciudad);
            ps.setString(4, cp);
            ps.setString(5, tel);
            ps.setString(6, fisMor);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Proveedor agregado.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar proveedor.");
        }
    }

    // ============================================================
    // CARGAR TABLA DE PROVEEDORES
    // ============================================================

    public void cargarTablaProveedores(Connection conn, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        String sql = "SELECT * FROM proveedor ORDER BY id_prov ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id_prov"),
                        rs.getString("nombre_prov"),
                        rs.getString("calle_prov"),
                        rs.getString("ciudad_prov"),
                        rs.getString("cp_prov"),
                        rs.getString("telefono_prov"),
                        rs.getString("fis_mor_prov")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar proveedores.");
        }
    }
}
