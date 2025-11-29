package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class paginaPrincipal extends JFrame {

    public void MenuPrincipal(Connection conn, int idUsuario) {

        setTitle("Luas Place - Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(90, 60, 40));  // Café oscuro
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titulo = new JLabel("Luas Place - Sistema de Gestión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(255, 240, 220)); // Beige suave
        panelSuperior.add(titulo, BorderLayout.WEST);
        add(panelSuperior, BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pestañas.setBackground(new Color(230, 210, 180));  // Beige café claro
        pestañas.setForeground(new Color(70, 40, 20));     // Texto café oscuro

        // CRUD Usuarios
        crudUsuarioInterfaz crud = new crudUsuarioInterfaz();
        crud.crudEmpleadosInterfaz(conn);

        JPanel panelCrud = new JPanel(new BorderLayout());
        panelCrud.setBackground(new Color(245, 230, 210));   // Fondo cálido
        panelCrud.add(crud.getContentPane(), BorderLayout.CENTER);
        pestañas.addTab("Usuarios", panelCrud);

        // Panel PAN
        JPanel panelPan = new JPanel(new BorderLayout());
        panelPan.setBackground(new Color(245, 230, 210));
        panelPan.add(new JLabel("Interfaz de PAN (No funcional todavía)", SwingConstants.CENTER));
        pestañas.addTab("Pan", panelPan);

        // Panel Ventas
        JPanel panelVentas = new JPanel(new BorderLayout());
        panelVentas.setBackground(new Color(245, 230, 210));
        panelVentas.add(new JLabel("Interfaz de Ventas (En construcción)", SwingConstants.CENTER));
        pestañas.addTab("Ventas", panelVentas);

        // Panel Inventario
        JPanel panelInv = new JPanel(new BorderLayout());
        panelInv.setBackground(new Color(245, 230, 210));
        panelInv.add(new JLabel("Interfaz de Inventario (Próximamente)", SwingConstants.CENTER));
        pestañas.addTab("Inventario", panelInv);

        // === Panel Registros ===
        JPanel panelReg = new JPanel(new BorderLayout());
        panelReg.setBackground(new Color(245, 230, 210));
        panelReg.add(new JLabel("Registros del sistema (Log)", SwingConstants.CENTER));
        pestañas.addTab("Registros", panelReg);

        add(pestañas, BorderLayout.CENTER);
    }
}
