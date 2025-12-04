package Interfaz;

import Funciones.controlSesiones;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class inventarioInterfaz {

    private Color cafeFuerte = new Color(90, 60, 40);
    private Color cafeClaro = new Color(230, 210, 180);

    private int idUsuario; // <-- Se agrega para permisos

    // ===== Constructor =====
    public inventarioInterfaz(Connection conn, int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // ======== PANEL PRINCIPAL DEL INVENTARIO ========
    public JTabbedPane crearMenuInventario(Connection conn) {

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(cafeClaro);
        tabs.setForeground(cafeFuerte);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ===== PERMISOS =====
        boolean puedeInventario = controlSesiones.tienePermiso(idUsuario, "inventario", conn);
        boolean puedeCRUD = controlSesiones.tienePermiso(idUsuario, "crud", conn);

        // ===== PESTAÑA INVENTARIO =====
        if (puedeInventario) {
            tabs.addTab("Inventario", panelInventario(conn));
        }

        // ===== PESTAÑA NUEVO PRODUCTO (CRUD requerido) =====
        if (puedeCRUD) {
            tabs.addTab("Nuevo Producto", panelNuevoProducto(conn));
        }

        // ===== PESTAÑA PROVEEDORES (CRUD requerido) =====
        if (puedeCRUD) {
            tabs.addTab("Proveedores", panelProveedores(conn));
        }

        return tabs;
    }

    // ======= Métodos originales =======
    public JPanel panelInventario(Connection conn) {
        JPanel panel = new JPanel();
        panel.setBackground(cafeClaro);
        panel.add(new JLabel("Aquí va tu inventario"));
        return panel;
    }

    public JPanel panelNuevoProducto(Connection conn) {
        JPanel panel = new JPanel();
        panel.setBackground(cafeClaro);
        panel.add(new JLabel("Aquí va Nuevo Producto"));
        return panel;
    }

    public JPanel panelProveedores(Connection conn) {
        JPanel panel = new JPanel();
        panel.setBackground(cafeClaro);
        panel.add(new JLabel("Aquí va Proveedores"));
        return panel;
    }
}
