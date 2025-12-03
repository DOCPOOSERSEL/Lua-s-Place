package Interfaz;

import Funciones.funcionesProductos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

public class interfazProductos {

    funcionesProductos fun = new funcionesProductos();

    Color cafeF = new Color(92, 64, 51);
    Color cafeM = new Color(160, 120, 90);
    Color cafeC = new Color(245, 235, 220);

    public interfazProductos(Connection conn) {}

    public JPanel crearPantalla(Connection conn) {

        JPanel base = new JPanel(new BorderLayout());
        base.setBackground(cafeC);

        // ---------------------------
        // ENCABEZADO DELGADO (regresado como antes)
        // ---------------------------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(cafeF);
        header.setBorder(new EmptyBorder(8, 15, 8, 15)); // MÁS DELGADO

        JLabel titulo = new JLabel("Inventario de Productos y Panes");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        header.add(titulo, BorderLayout.WEST);

        // BOTÓN AGREGAR MÁS NOTABLE
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(210, 140, 90)); // claro y llamativo
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));

        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnAgregar.setBackground(new Color(230, 160, 110));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnAgregar.setBackground(new Color(210, 140, 90));
            }
        });

        header.add(btnAgregar, BorderLayout.EAST);

        base.add(header, BorderLayout.NORTH);

        // ---------------------------
        // PANEL CENTRAL
        // ---------------------------
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(cafeC);
        centro.setBorder(new EmptyBorder(10, 20, 10, 20));

        JTable tabla = new JTable(new DefaultTableModel(
                new String[]{"ID", "Nombre", "Precio", "Tipo"}, 0
        ));
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(cafeM);
        tabla.getTableHeader().setForeground(Color.WHITE);

        fun.cargarProductos(conn, tabla);
        fun.cargarPanes(conn, tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(cafeM, 2));
        centro.add(scroll, BorderLayout.CENTER);

        base.add(centro, BorderLayout.CENTER);

        // ---------------------------
        // PANEL DE DETALLES ABAJO
        // ---------------------------
        JPanel detalles = new JPanel(new BorderLayout());
        detalles.setBackground(cafeC);
        detalles.setBorder(new EmptyBorder(15, 30, 15, 30));
        base.add(detalles, BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                int id = (int) tabla.getValueAt(tabla.getSelectedRow(), 0);
                String tipo = tabla.getValueAt(tabla.getSelectedRow(), 3).toString();
                actualizarDetalles(conn, detalles, id, tipo);
            }
        });

        // BOTÓN AGREGAR (FUNCIONAL)
        btnAgregar.addActionListener(e -> abrirVentanaAgregar(conn, tabla));

        return base;
    }




    private void actualizarDetalles(Connection conn, JPanel panel, int id, String tipo) {
        panel.removeAll();
        panel.add(tipo.equals("Producto") ?
                detallesProducto(conn, id) :
                detallesPan(conn, id));
        panel.revalidate();
        panel.repaint();
    }

    private JPanel detallesProducto(Connection conn, int id) {
        JPanel p = new JPanel(new GridLayout(2, 1, 5, 5));
        p.setBackground(cafeC);

        String[] prod = fun.obtenerProducto(conn, id);

        JLabel n = new JLabel("Nombre: " + prod[0]);
        JLabel pr = new JLabel("Precio: $" + prod[1]);

        n.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pr.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        p.add(n);
        p.add(pr);

        return p;
    }

    private JPanel detallesPan(Connection conn, int id_pan) {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(cafeC);

        String[] pan = fun.obtenerPan(conn, id_pan);

        JLabel titulo = new JLabel("Pan: " + pan[0] + "   | Precio: $" + pan[1]);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setBorder(new EmptyBorder(5, 5, 10, 5));
        p.add(titulo, BorderLayout.NORTH);

        JTable tablaReceta = new JTable(new DefaultTableModel(
                new String[]{"ID Rec", "Ingrediente", "Cantidad"}, 0
        ));
        tablaReceta.setRowHeight(26);
        fun.cargarReceta(conn, tablaReceta, id_pan);

        JScrollPane scroll = new JScrollPane(tablaReceta);
        scroll.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.add(scroll, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        botones.setBackground(cafeC);

        JButton add = botonCafe("Agregar Ingrediente");
        JButton del = botonCafe("Eliminar");
        JButton mod = botonCafe("Modificar Cantidad");
        JButton back = botonCafe("Regresar");

        botones.add(add);
        botones.add(mod);
        botones.add(del);
        botones.add(back);

        p.add(botones, BorderLayout.SOUTH);

        add.addActionListener(e -> agregarIngredienteVentana(conn, id_pan, tablaReceta));
        mod.addActionListener(e -> modificarCantidadVentana(conn, id_pan, tablaReceta));

        del.addActionListener(e -> {
            int fila = tablaReceta.getSelectedRow();
            if (fila != -1) {
                int id_rec = (int) tablaReceta.getValueAt(fila, 0);
                fun.eliminarIngrediente(conn, id_rec);
                fun.cargarReceta(conn, tablaReceta, id_pan);
            }
        });

        back.addActionListener(e -> {
            p.removeAll();
            p.revalidate();
            p.repaint();
        });

        return p;
    }

    private void modificarCantidadVentana(Connection conn, int id_pan, JTable tabla) {

        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        int id_rec = (int) tabla.getValueAt(fila, 0);
        double actual = Double.parseDouble(tabla.getValueAt(fila, 2).toString());

        JDialog d = crearDialogo("Modificar Cantidad", 350, 200);

        JPanel cont = new JPanel(new GridLayout(2, 2, 8, 8));
        cont.setBackground(cafeC);
        cont.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtCant = new JTextField(String.valueOf(actual));

        cont.add(new JLabel("Cantidad nueva:"));
        cont.add(txtCant);

        JButton guardar = botonCafe("Guardar");
        cont.add(new JLabel(""));
        cont.add(guardar);

        d.add(cont);

        guardar.addActionListener(e -> {
            double nueva = Double.parseDouble(txtCant.getText());
            fun.modificarCantidadIngrediente(conn, id_rec, nueva); // CORREGIDO
            fun.cargarReceta(conn, tabla, id_pan);
            d.dispose();
        });

        d.setVisible(true);
    }

    private void agregarIngredienteVentana(Connection conn, int id_pan, JTable tabla) {

        JDialog d = crearDialogo("Agregar Ingrediente", 400, 300);

        JPanel cont = new JPanel(new GridLayout(3, 2, 8, 8));
        cont.setBackground(cafeC);
        cont.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Combo solo con nombres
        JComboBox<String> ingredientes = new JComboBox<>();

        // Lista paralela para guardar los IDs REALES
        ArrayList<Integer> ids = new ArrayList<>();

        ArrayList<String[]> inv = fun.obtenerInventario(conn);

        for (String[] i : inv) {
            ids.add(Integer.parseInt(i[0])); // guarda el ID
            ingredientes.addItem(i[1]);      // muestra solo el nombre
        }

        JTextField txtCant = new JTextField();

        cont.add(new JLabel("Ingrediente:"));
        cont.add(ingredientes);
        cont.add(new JLabel("Cantidad:"));
        cont.add(txtCant);

        JButton guardar = botonCafe("Guardar");
        cont.add(new JLabel(""));
        cont.add(guardar);

        d.add(cont);

        guardar.addActionListener(e -> {
            try {
                int index = ingredientes.getSelectedIndex(); // posición seleccionada
                int id_inv = ids.get(index);                 // obtiene ID real desde la lista paralela

                double cant = Double.parseDouble(txtCant.getText());

                fun.agregarIngrediente(conn, id_pan, id_inv, cant);
                fun.cargarReceta(conn, tabla, id_pan);

                d.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        d.setVisible(true);
    }


    private void abrirVentanaAgregar(Connection conn, JTable tabla) {

        JDialog d = crearDialogo("Agregar", 350, 220);

        JPanel cont = new JPanel(new GridLayout(2, 1, 10, 10));
        cont.setBorder(new EmptyBorder(20, 20, 20, 20));
        cont.setBackground(cafeC);

        JButton prod = botonCafe("Agregar Producto");
        JButton pan = botonCafe("Agregar Pan");

        prod.addActionListener(e -> agregarProductoVentana(conn, tabla, d));
        pan.addActionListener(e -> agregarPanVentana(conn, tabla, d));

        cont.add(prod);
        cont.add(pan);

        d.add(cont);
        d.setVisible(true);
    }

    private void agregarProductoVentana(Connection conn, JTable tabla, JDialog parent) {

        JDialog d = crearDialogo("Nuevo Producto", 400, 250);

        JPanel cont = new JPanel(new GridLayout(3, 2, 10, 10));
        cont.setBackground(cafeC);
        cont.setBorder(new EmptyBorder(15, 20, 15, 20));

        JTextField n = new JTextField();
        JTextField p = new JTextField();

        JButton guardar = botonCafe("Guardar");

        cont.add(new JLabel("Nombre:"));
        cont.add(n);
        cont.add(new JLabel("Precio:"));
        cont.add(p);
        cont.add(new JLabel(""));
        cont.add(guardar);

        d.add(cont);

        guardar.addActionListener(e -> {
            fun.agregarProducto(conn, n.getText(), Double.parseDouble(p.getText()));

            DefaultTableModel m = (DefaultTableModel) tabla.getModel();
            m.setRowCount(0);

            fun.cargarProductos(conn, tabla);
            fun.cargarPanes(conn, tabla);

            d.dispose();
            parent.dispose();
        });

        d.setVisible(true);
    }

    private void agregarPanVentana(Connection conn, JTable tabla, JDialog parent) {

        JDialog d = crearDialogo("Nuevo Pan", 400, 250);

        JPanel cont = new JPanel(new GridLayout(3, 2, 10, 10));
        cont.setBackground(cafeC);
        cont.setBorder(new EmptyBorder(15, 20, 15, 20));

        JTextField n = new JTextField();
        JTextField p = new JTextField();

        JButton guardar = botonCafe("Guardar");

        cont.add(new JLabel("Nombre Pan:"));
        cont.add(n);
        cont.add(new JLabel("Precio:"));
        cont.add(p);
        cont.add(new JLabel(""));
        cont.add(guardar);

        d.add(cont);

        guardar.addActionListener(e -> {
            fun.agregarPan(conn, n.getText(), Double.parseDouble(p.getText()));

            DefaultTableModel m = (DefaultTableModel) tabla.getModel();
            m.setRowCount(0);

            fun.cargarProductos(conn, tabla);
            fun.cargarPanes(conn, tabla);

            d.dispose();
            parent.dispose();
        });

        d.setVisible(true);
    }

    private JButton botonCafe(String t) {
        JButton b = new JButton(t);
        b.setBackground(cafeF);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(cafeM); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(cafeF); }
        });

        return b;
    }

    private JDialog crearDialogo(String titulo, int w, int h) {
        JDialog d = new JDialog();
        d.setTitle(titulo);
        d.setSize(w, h);
        d.setLocationRelativeTo(null);
        d.setModal(true);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(cafeC);
        return d;
    }
}
