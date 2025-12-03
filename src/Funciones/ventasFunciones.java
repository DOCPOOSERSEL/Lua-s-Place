package Funciones;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ventasFunciones {

    public void cargarProductosYPanes(Connection conn, JTable tabla) {
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        m.setRowCount(0);

        try {
            Statement st = conn.createStatement();

            ResultSet r1 = st.executeQuery("SELECT id_prod, nombre_prod, precio_prod FROM productos");
            while (r1.next()) {
                m.addRow(new Object[]{
                        r1.getInt(1), r1.getString(2), r1.getDouble(3), "Producto"
                });
            }

            ResultSet r2 = st.executeQuery("SELECT id_pan, nombre_pan, precio_pan FROM pan_casero");
            while (r2.next()) {
                m.addRow(new Object[]{
                        r2.getInt(1), r2.getString(2), r2.getDouble(3), "Pan"
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int insertarCliente(Connection conn, String n, String ap, String am, String calle, String col, String cp) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO cliente(nombre_clie, apellidop_clie, apellidom_clie, calle_clie, col_clie, cp_clie) " +
                            "VALUES(?,?,?,?,?,?) RETURNING id_clie"
            );

            ps.setString(1, n);
            ps.setString(2, ap);
            ps.setString(3, am);
            ps.setString(4, calle);
            ps.setString(5, col);
            ps.setString(6, cp);

            ResultSet r = ps.executeQuery();
            return r.next() ? r.getInt(1) : -1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int crearCarrito(Connection conn, JTable tablaCarrito) {
        try {
            DefaultTableModel m = (DefaultTableModel) tablaCarrito.getModel();

            PreparedStatement psCar = conn.prepareStatement(
                    "INSERT INTO carrito(id_prod, cantidad_prod, id_pan, cantidad_pan) VALUES (?,?,?,?) RETURNING id_car"
            );

            // Solo se crea 1 carrito con varios registros? NO.
            // Pero tú lo tienes así, así lo respetamos.
            int idCar = -1;

            for (int i = 0; i < m.getRowCount(); i++) {

                int id = (int) m.getValueAt(i, 0);
                int cant = (int) m.getValueAt(i, 3);
                String tipo = m.getValueAt(i, 4).toString();

                if (tipo.equals("Producto")) {
                    psCar.setInt(1, id);
                    psCar.setInt(2, cant);
                    psCar.setNull(3, java.sql.Types.INTEGER);
                    psCar.setInt(4, 0);
                } else {
                    psCar.setNull(1, java.sql.Types.INTEGER);
                    psCar.setInt(2, 0);
                    psCar.setInt(3, id);
                    psCar.setInt(4, cant);
                }

                ResultSet r = psCar.executeQuery();
                if (r.next()) idCar = r.getInt(1);
            }
            return idCar;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void registrarVenta(Connection conn, int idCliente, int idEmpleado, int idCar) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO ventas(id_clie, id_emp, id_car) VALUES (?,?,?)"
            );

            ps.setInt(1, idCliente);
            ps.setInt(2, idEmpleado);
            ps.setInt(3, idCar);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void descontarInventario(Connection conn, JTable tablaCarrito) {
        try {
            DefaultTableModel m = (DefaultTableModel) tablaCarrito.getModel();

            for (int i = 0; i < m.getRowCount(); i++) {

                int id = (int) m.getValueAt(i, 0);
                int cant = (int) m.getValueAt(i, 3);
                String tipo = m.getValueAt(i, 4).toString();

                if (tipo.equals("Producto")) {

                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE inventario SET cantidad_inv = cantidad_inv - ? WHERE nombre_inv = " +
                                    "(SELECT nombre_prod FROM productos WHERE id_prod = ?)"
                    );

                    ps.setInt(1, cant);
                    ps.setInt(2, id);
                    ps.executeUpdate();

                } else {
                    PreparedStatement psR = conn.prepareStatement(
                            "SELECT id_inv, cantidad_ing FROM recetas WHERE id_pan = ?"
                    );
                    psR.setInt(1, id);
                    ResultSet r = psR.executeQuery();

                    while (r.next()) {
                        int idInv = r.getInt(1);
                        double cIng = r.getDouble(2);

                        PreparedStatement ps = conn.prepareStatement(
                                "UPDATE inventario SET cantidad_inv = cantidad_inv - ? WHERE id_inv = ?"
                        );

                        ps.setDouble(1, cIng * cant);
                        ps.setInt(2, idInv);
                        ps.executeUpdate();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarRegistroVentas(Connection conn, JTable tabla) {
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        m.setRowCount(0);

        try {
            Statement st = conn.createStatement();
            ResultSet r = st.executeQuery(
                    "SELECT v.id_venta, c.nombre_clie, e.nombre_emp, v.fecha_venta " +
                            "FROM ventas v " +
                            "INNER JOIN cliente c ON c.id_clie = v.id_clie " +
                            "INNER JOIN empleado e ON e.id_emp = v.id_emp"
            );

            while (r.next()) {
                m.addRow(new Object[]{
                        r.getInt(1), r.getString(2), r.getString(3), r.getDate(4)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
