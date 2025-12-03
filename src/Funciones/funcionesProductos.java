package Funciones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class funcionesProductos {

    // =====================================================
    // CARGAR PRODUCTOS
    // =====================================================
    public void cargarProductos(Connection conn, JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        String sql = "SELECT id_prod, nombre_prod, precio_prod FROM productos";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_prod"),
                        rs.getString("nombre_prod"),
                        rs.getDouble("precio_prod"),
                        "Producto"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error cargando productos: " + e.getMessage());
        }
    }

    // =====================================================
    // CARGAR PANES
    // =====================================================
    public void cargarPanes(Connection conn, JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        String sql = "SELECT id_pan, nombre_pan, precio_pan FROM pan_casero";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_pan"),
                        rs.getString("nombre_pan"),
                        rs.getDouble("precio_pan"),
                        "Pan"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error cargando panes: " + e.getMessage());
        }
    }

    // =====================================================
    // OBTENER PRODUCTO
    // =====================================================
    public String[] obtenerProducto(Connection conn, int id) {
        String sql = "SELECT nombre_prod, precio_prod FROM productos WHERE id_prod=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new String[]{
                        rs.getString("nombre_prod"),
                        rs.getDouble("precio_prod") + ""
                };
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error obteniendo producto: " + e.getMessage());
        }
        return null;
    }

    // =====================================================
    // OBTENER PAN
    // =====================================================
    public String[] obtenerPan(Connection conn, int id) {
        String sql = "SELECT nombre_pan, precio_pan FROM pan_casero WHERE id_pan=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new String[]{
                        rs.getString("nombre_pan"),
                        rs.getDouble("precio_pan") + ""
                };
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error obteniendo pan: " + e.getMessage());
        }
        return null;
    }

    // =====================================================
    // CARGAR RECETA
    // =====================================================
    public void cargarReceta(Connection conn, JTable tabla, int id_pan) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        String sql =
                "SELECT r.id_receta, i.nombre_inv, r.cantidad_ing " +
                        "FROM recetas r " +
                        "JOIN inventario i ON r.id_inv = i.id_inv " +
                        "WHERE r.id_pan=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_pan);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_receta"),
                        rs.getString("nombre_inv"),
                        rs.getDouble("cantidad_ing")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error cargando receta: " + e.getMessage());
        }
    }

    // =====================================================
    // LISTA DE INVENTARIO (SIN NÚMEROS FEOS)
    // =====================================================
    public ArrayList<String[]> obtenerInventario(Connection conn) {
        ArrayList<String[]> lista = new ArrayList<>();

        String sql = "SELECT id_inv, nombre_inv FROM inventario";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getInt("id_inv") + "",
                        rs.getString("nombre_inv")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error obteniendo inventario: " + e.getMessage());
        }
        return lista;
    }

    // =====================================================
    // AGREGAR INGREDIENTE
    // =====================================================
    public void agregarIngrediente(Connection conn, int id_pan, int id_inv, double cantidad) {
        String sql =
                "INSERT INTO recetas(id_pan, id_inv, cantidad_ing) VALUES(?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id_pan);
            ps.setInt(2, id_inv);
            ps.setDouble(3, cantidad);
            ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error agregando ingrediente: " + e.getMessage());
        }
    }

    // =====================================================
    // ELIMINAR INGREDIENTE
    // =====================================================
    public void eliminarIngrediente(Connection conn, int id_rec) {
        String sql = "DELETE FROM recetas WHERE id_receta=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_rec);
            ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error eliminando ingrediente: " + e.getMessage());
        }
    }

    // =====================================================
    // AGREGAR PRODUCTO
    // =====================================================
    public void agregarProducto(Connection conn, String nombre, double precio) {
        String sql =
                "INSERT INTO productos(nombre_prod, precio_prod) VALUES(?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error agregando producto: " + e.getMessage());
        }
    }

    // =====================================================
    // AGREGAR PAN
    // =====================================================
    public int agregarPan(Connection conn, String nombre, double precio) {
        String sql =
                "INSERT INTO pan_casero(nombre_pan, precio_pan) VALUES(?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.executeUpdate();

            ResultSet k = ps.getGeneratedKeys();
            if (k.next()) return k.getInt(1);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error agregando pan: " + e.getMessage());
        }
        return -1;
    }

    // =====================================================
    // MODIFICAR CANTIDAD DE INGREDIENTE (FUNCION REAL)
    // =====================================================
    public void modificarCantidadIngrediente(Connection conn, int id_receta, double nuevaCantidad) {

        String sql = "UPDATE recetas SET cantidad_ing=? WHERE id_receta=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, nuevaCantidad);
            ps.setInt(2, id_receta);
            ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error modificando cantidad: " + e.getMessage());
        }
    }

}

