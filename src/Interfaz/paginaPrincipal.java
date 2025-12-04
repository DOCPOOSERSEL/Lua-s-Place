package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

import Funciones.controlSesiones;
import Interfaz.inventarioInterfaz;

public class paginaPrincipal extends JFrame {

    public void MenuPrincipal(Connection conn, int idUsuario) {

        setTitle("Luas Place - Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(90, 60, 40));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titulo = new JLabel("Luas Place - Sistema de Gestión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(255, 240, 220));
        panelSuperior.add(titulo, BorderLayout.WEST);
        add(panelSuperior, BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pestañas.setBackground(new Color(230, 210, 180));
        pestañas.setForeground(new Color(70, 40, 20));

        // ===================== CRUD Usuarios =====================
        if (controlSesiones.tienePermiso(idUsuario, "crud", conn) ||
                controlSesiones.tienePermiso(idUsuario, "admin", conn)) {

            crudUsuarioInterfaz crud = new crudUsuarioInterfaz(idUsuario);
            crud.crudEmpleadosInterfaz(conn);

            JPanel panelCrud = new JPanel(new BorderLayout());
            panelCrud.setBackground(new Color(245, 230, 210));
            panelCrud.add(crud.getContentPane(), BorderLayout.CENTER);

            pestañas.addTab("Usuarios", panelCrud);
        }

        // ===================== Productos =====================
        if (controlSesiones.tienePermiso(idUsuario, "crud", conn)) {
            JPanel panelProductos = new JPanel(new BorderLayout());
            panelProductos.setBackground(new Color(245, 230, 210));

            interfazProductos productosUI = new interfazProductos(conn);
            panelProductos.add(productosUI.crearPantalla(conn), BorderLayout.CENTER);

            pestañas.addTab("Productos", panelProductos);
        }

        // ===================== Ventas =====================
        if (controlSesiones.tienePermiso(idUsuario, "ventas", conn)) {
            interfazVentas ven = new interfazVentas(idUsuario);
            JTabbedPane panelVentas = ven.crearPanelVentas(conn);

            pestañas.addTab("Ventas", panelVentas);
        }

        // ===================== Inventario =====================
        if (controlSesiones.tienePermiso(idUsuario, "inventario", conn)) {
            inventarioInterfaz inv = new inventarioInterfaz(conn);
            JTabbedPane panelInventario = inv.crearMenuInventario(conn);

            pestañas.addTab("Inventario", panelInventario);
        }

        // ===================== Registros =====================
        if (controlSesiones.tienePermiso(idUsuario, "admin", conn)) {
            controlSesionInterfaz panelSesiones = new controlSesionInterfaz(conn);

            JPanel panelReg = new JPanel(new BorderLayout());
            panelReg.setBackground(new Color(245, 230, 210));
            panelReg.add(panelSesiones, BorderLayout.CENTER);

            pestañas.addTab("Registros", panelReg);
        }

        add(pestañas, BorderLayout.CENTER);
    }
}
