package Interfaz;

import Funciones.inventarioFuncion;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;

public class inventarioInterfaz {

    inventarioFuncion fun = new inventarioFuncion();

    Color cafeFuerte = new Color(92, 64, 51);
    Color cafeMedio = new Color(160, 120, 90);
    Color cafeClaro = new Color(245, 235, 220);

    public inventarioInterfaz(Connection conn) {}

    public JTabbedPane crearMenuInventario(Connection conn) {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(cafeClaro);
        tabs.setForeground(cafeFuerte);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.add("Inventario", panelInventario(conn));
        tabs.add("Nuevo Producto", panelNuevoProducto(conn));
        tabs.add("Proveedores", panelProveedores(conn));

        return tabs;
    }


    private JPanel panelInventario(Connection conn) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cafeClaro);

        JTable tabla = new JTable(new DefaultTableModel(
                new String[]{"ID", "Nombre", "Cantidad", "Costo"}, 0
        ));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(25);

        tabla.setBackground(Color.WHITE);
        tabla.setForeground(cafeFuerte);

        tabla.getTableHeader().setBackground(cafeMedio);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(cafeMedio));

        fun.cargarInventario(conn, tabla);

        JPanel acciones = new JPanel();
        acciones.setBackground(cafeMedio);

        JButton btnMas = botonCafe("+ Agregar Cant.");
        JButton btnMenos = botonCafe("- Restar Cant.");

        acciones.add(btnMas);
        acciones.add(btnMenos);

        btnMas.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona un producto.");
                return;
            }

            int id = (int) tabla.getValueAt(fila, 0);
            int cant = Integer.parseInt(JOptionPane.showInputDialog("Cantidad a agregar:"));

            fun.agregarCantidad(conn, id, cant);
            fun.cargarInventario(conn, tabla);
        });

        btnMenos.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona un producto.");
                return;
            }

            int id = (int) tabla.getValueAt(fila, 0);
            int cant = Integer.parseInt(JOptionPane.showInputDialog("Cantidad a restar:"));

            fun.restarCantidad(conn, id, cant);
            fun.cargarInventario(conn, tabla);
        });

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel panelNuevoProducto(Connection conn) {

        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(cafeClaro);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cafeMedio, 2),
                new EmptyBorder(25, 35, 25, 35)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Agregar Nuevo Producto");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(cafeFuerte);

        JTextField txtNombre = new JTextField();
        JTextField txtCantidad = new JTextField();
        JTextField txtCosto = new JTextField();
        JComboBox<String> comboProv = new JComboBox<>();

        // === CAMPOS EN BLANCO PROFESIONALES ===
        txtNombre.setBackground(Color.WHITE);
        txtCantidad.setBackground(Color.WHITE);
        txtCosto.setBackground(Color.WHITE);
        comboProv.setBackground(Color.WHITE);

        txtNombre.setForeground(cafeFuerte);
        txtCantidad.setForeground(cafeFuerte);
        txtCosto.setForeground(cafeFuerte);
        comboProv.setForeground(cafeFuerte);

        fun.cargarProveedores(conn, comboProv);

        JButton btnGuardar = botonCafe("Guardar");

        // Título
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        card.add(titulo, c);

        c.gridwidth = 1;
        c.gridy++;

        card.add(labelForm("Nombre:"), c);     c.gridx = 1; card.add(txtNombre, c);
        c.gridy++; c.gridx = 0;
        card.add(labelForm("Cantidad:"), c);   c.gridx = 1; card.add(txtCantidad, c);
        c.gridy++; c.gridx = 0;
        card.add(labelForm("Costo:"), c);      c.gridx = 1; card.add(txtCosto, c);
        c.gridy++; c.gridx = 0;
        card.add(labelForm("Proveedor:"), c);  c.gridx = 1; card.add(comboProv, c);

        // Botón
        c.gridy++; c.gridx = 0; c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        card.add(btnGuardar, c);

        btnGuardar.addActionListener(e -> {

            String nombre = txtNombre.getText();
            int cant = Integer.parseInt(txtCantidad.getText());
            double costo = Double.parseDouble(txtCosto.getText());
            int idProv = Integer.parseInt(comboProv.getSelectedItem().toString().split(" - ")[0]);

            fun.agregarInventario(conn, nombre, cant, costo, idProv);

            txtNombre.setText("");
            txtCantidad.setText("");
            txtCosto.setText("");
        });

        base.add(card);

        return base;
    }

    private JLabel labelForm(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        l.setForeground(cafeFuerte);
        return l;
    }


    private JPanel panelProveedores(Connection conn) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cafeClaro);

        JTable tabla = new JTable(new DefaultTableModel(
                new String[]{"ID","Nombre","Calle","Ciudad","CP","Teléfono","F/M"},0
        ));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.setBackground(Color.WHITE);
        tabla.setForeground(cafeFuerte);

        tabla.getTableHeader().setBackground(cafeMedio);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        fun.cargarTablaProveedores(conn, tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(cafeMedio));

        JPanel form = new JPanel(new GridLayout(7,2,5,5));
        form.setBorder(new EmptyBorder(15,15,15,15));
        form.setBackground(cafeClaro);

        JTextField txtNom = new JTextField();
        JTextField txtCalle = new JTextField();
        JTextField txtCiudad = new JTextField();
        JTextField txtCP = new JTextField();
        JTextField txtTel = new JTextField();
        JComboBox<String> combo = new JComboBox<>(new String[]{"f","m"});

        // === CAMPOS BLANCOS COMO DEBE SER ===
        txtNom.setBackground(Color.WHITE);
        txtCalle.setBackground(Color.WHITE);
        txtCiudad.setBackground(Color.WHITE);
        txtCP.setBackground(Color.WHITE);
        txtTel.setBackground(Color.WHITE);
        combo.setBackground(Color.WHITE);

        txtNom.setForeground(cafeFuerte);
        txtCalle.setForeground(cafeFuerte);
        txtCiudad.setForeground(cafeFuerte);
        txtCP.setForeground(cafeFuerte);
        txtTel.setForeground(cafeFuerte);
        combo.setForeground(cafeFuerte);

        JButton btnGuardar = botonCafe("Agregar Proveedor");

        form.add(labelForm("Nombre:")); form.add(txtNom);
        form.add(labelForm("Calle:")); form.add(txtCalle);
        form.add(labelForm("Ciudad:")); form.add(txtCiudad);
        form.add(labelForm("CP:")); form.add(txtCP);
        form.add(labelForm("Teléfono:")); form.add(txtTel);
        form.add(labelForm("F/M:")); form.add(combo);
        form.add(new JLabel("")); form.add(btnGuardar);

        btnGuardar.addActionListener(e -> {

            fun.agregarProveedor(conn,
                    txtNom.getText(),
                    txtCalle.getText(),
                    txtCiudad.getText(),
                    txtCP.getText(),
                    txtTel.getText(),
                    combo.getSelectedItem().toString()
            );

            fun.cargarTablaProveedores(conn, tabla);

            txtNom.setText("");
            txtCalle.setText("");
            txtCiudad.setText("");
            txtCP.setText("");
            txtTel.setText("");
        });

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(form, BorderLayout.SOUTH);

        return panel;
    }

    private JButton botonCafe(String txt) {
        JButton b = new JButton(txt);
        b.setBackground(cafeFuerte);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(cafeMedio);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(cafeFuerte);
            }
        });

        return b;
    }
}
