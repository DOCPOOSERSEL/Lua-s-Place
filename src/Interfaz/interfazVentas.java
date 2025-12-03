package Interfaz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import Funciones.ventasFunciones;

public class interfazVentas {

    private final Color cafeC = new Color(245, 230, 210);
    private final Color cafeF = new Color(120, 80, 50);
    private final Color cafeM = new Color(160, 110, 70);

    private ventasFunciones fun = new ventasFunciones();

    private int idEmpleado;

    public interfazVentas(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public JTabbedPane crearPanelVentas(Connection conn) {

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(cafeC);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("Venta", crearPanelVenta(conn));
        tabs.addTab("Registro de Ventas", crearPanelRegistro(conn));

        return tabs;
    }

    // --------------------------------------------------------------------------
    // PANEL DE VENTA
    // --------------------------------------------------------------------------
    private JPanel crearPanelVenta(Connection conn) {

        JPanel base = new JPanel(new BorderLayout());
        base.setBackground(cafeC);
        base.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Tabla de productos a elegir
        JTable tablaProd = new JTable(new DefaultTableModel(
                new String[]{"ID", "Nombre", "Precio", "Tipo"}, 0
        ));
        fun.cargarProductosYPanes(conn, tablaProd);

        tablaProd.setRowHeight(28);
        JScrollPane scrollProd = new JScrollPane(tablaProd);
        scrollProd.setBorder(BorderFactory.createLineBorder(cafeM, 2));

        // Carrito
        JTable tablaCarrito = new JTable(new DefaultTableModel(
                new String[]{"ID", "Nombre", "Precio", "Cant.", "Tipo"}, 0
        ));
        tablaCarrito.setRowHeight(28);
        JScrollPane scrollCar = new JScrollPane(tablaCarrito);
        scrollCar.setBorder(BorderFactory.createLineBorder(cafeM, 2));

        // Botón agregar al carrito
        JButton btnAgregar = botonCafe("Agregar al Carrito");
        btnAgregar.addActionListener(e -> {
            int row = tablaProd.getSelectedRow();
            if (row == -1) return;

            int id = (int) tablaProd.getValueAt(row, 0);
            String nombre = tablaProd.getValueAt(row, 1).toString();
            double precio = (double) tablaProd.getValueAt(row, 2);
            String tipo = tablaProd.getValueAt(row, 3).toString();

            String cantStr = JOptionPane.showInputDialog("Cantidad:");
            if (cantStr == null || cantStr.isEmpty()) return;
            int cant = Integer.parseInt(cantStr);

            DefaultTableModel m = (DefaultTableModel) tablaCarrito.getModel();
            m.addRow(new Object[]{id, nombre, precio, cant, tipo});
        });

        // Botón pagar
        JButton btnPagar = botonCafe("Continuar con el Pago");
        btnPagar.addActionListener(e ->
                ventanaPago(conn, tablaCarrito)
        );

        // Distribución
        JPanel botones = new JPanel();
        botones.setBackground(cafeC);
        botones.add(btnAgregar);
        botones.add(btnPagar);

        base.add(scrollProd, BorderLayout.WEST);
        base.add(scrollCar, BorderLayout.CENTER);
        base.add(botones, BorderLayout.SOUTH);

        return base;
    }

    // --------------------------------------------------------------------------
    // PANEL REGISTRO DE VENTAS
    // --------------------------------------------------------------------------
    private JPanel crearPanelRegistro(Connection conn) {

        JPanel base = new JPanel(new BorderLayout());
        base.setBackground(cafeC);

        JTable tabla = new JTable(new DefaultTableModel(
                new String[]{"ID Venta", "Cliente", "Empleado", "Fecha"}, 0
        ));

        fun.cargarRegistroVentas(conn, tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(cafeM, 2));

        base.add(scroll, BorderLayout.CENTER);
        return base;
    }

    // --------------------------------------------------------------------------
    // VENTANA DE PAGO
    // --------------------------------------------------------------------------
    private void ventanaPago(Connection conn, JTable tablaCarrito) {

        JDialog d = new JDialog();
        d.setTitle("Datos del Cliente");
        d.setSize(400, 400);
        d.setModal(true);
        d.setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(7, 2, 8, 8));
        p.setBackground(cafeC);
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField nombre = new JTextField();
        JTextField ap = new JTextField();
        JTextField am = new JTextField();
        JTextField calle = new JTextField();
        JTextField col = new JTextField();
        JTextField cp = new JTextField();

        p.add(new JLabel("Nombre:")); p.add(nombre);
        p.add(new JLabel("Apellido Paterno:")); p.add(ap);
        p.add(new JLabel("Apellido Materno:")); p.add(am);
        p.add(new JLabel("Calle:")); p.add(calle);
        p.add(new JLabel("Colonia:")); p.add(col);
        p.add(new JLabel("CP:")); p.add(cp);

        JButton btnFinalizar = botonCafe("Confirmar Venta");

        btnFinalizar.addActionListener(e -> {

            int idCliente = fun.insertarCliente(conn,
                    nombre.getText(), ap.getText(), am.getText(),
                    calle.getText(), col.getText(), cp.getText()
            );

            int idCar = fun.crearCarrito(conn, tablaCarrito);
            fun.registrarVenta(conn, idCliente, idEmpleado, idCar);
            fun.descontarInventario(conn, tablaCarrito);

            JOptionPane.showMessageDialog(null, "Venta realizada correctamente.");

            d.dispose();
        });

        p.add(new JLabel());
        p.add(btnFinalizar);

        d.add(p);
        d.setVisible(true);
    }

    private JButton botonCafe(String t) {
        JButton b = new JButton(t);
        b.setBackground(cafeM);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return b;
    }
}
