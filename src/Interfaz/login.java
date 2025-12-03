package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class login {

    private String usuario;
    private String contrasena;

    // ===== PALETA DE COLORES =====
    private final Color CAFE_OSCURO = new Color(95, 60, 35);
    private final Color CAFE_MEDIO = new Color(150, 100, 60);
    private final Color CAFE_CLARO = new Color(240, 220, 200);

    // ===== BOTÓN ESTILO CAFÉ =====
    private JButton crearBotonCafe(String texto) {
        JButton btn = new JButton(texto);

        btn.setBackground(CAFE_OSCURO);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btn.setPreferredSize(new Dimension(120, 32));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(CAFE_MEDIO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(CAFE_OSCURO);
            }
        });

        return btn;
    }

    // ===== LOGIN COMPLETO MEJORADO =====
    public boolean mostrarDialogo() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Inicio de Sesión");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(CAFE_CLARO);

        // =====================================
        //     ICONO (AHORA VA ARRIBA)
        // =====================================
        JLabel icono = new JLabel();
        icono.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            Image img = new ImageIcon(
                    getClass().getResource("coffee-wrench-logo-design-template-free-vector.jpg")
            ).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);

            icono.setIcon(new ImageIcon(img));

        } catch (Exception e) {
            icono.setText("[Icono no encontrado]");
            icono.setFont(new Font("Arial", Font.BOLD, 14));
            icono.setForeground(CAFE_OSCURO);
        }

        panel.add(icono, BorderLayout.NORTH);

        // =====================================
        //     TÍTULO (DEBAJO DEL ICONO)
        // =====================================
        JLabel titulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(CAFE_OSCURO);
        panel.add(titulo, BorderLayout.CENTER);

        // =====================================
        //     CAMPOS DE LOGIN
        // =====================================
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCampos.setOpaque(false);

        JTextField txtUsuario = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();

        JCheckBox chkMostrar = new JCheckBox("Mostrar contraseña");
        chkMostrar.setOpaque(false);
        chkMostrar.setForeground(CAFE_OSCURO);

        chkMostrar.addActionListener(e -> {
            if (chkMostrar.isSelected()) txtContrasena.setEchoChar((char) 0);
            else txtContrasena.setEchoChar('*');
        });

        panelCampos.add(new JLabel("ID Usuario:"));
        panelCampos.add(txtUsuario);
        panelCampos.add(new JLabel("Contraseña:"));
        panelCampos.add(txtContrasena);
        panelCampos.add(new JLabel(""));
        panelCampos.add(chkMostrar);

        // Contenedor para centrar los campos
        JPanel contCentro = new JPanel(new BorderLayout());
        contCentro.setOpaque(false);
        contCentro.add(panelCampos, BorderLayout.CENTER);

        panel.add(contCentro, BorderLayout.SOUTH);

        // =====================================
        //     BOTONES
        // =====================================
        JButton btnAceptar = crearBotonCafe("Ingresar");
        JButton btnCancelar = crearBotonCafe("Cancelar");

        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        // Se agrega al final (SUR global del diálogo)
        JPanel contBotones = new JPanel(new BorderLayout());
        contBotones.setOpaque(false);
        contBotones.add(panelBotones, BorderLayout.SOUTH);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(contBotones, BorderLayout.SOUTH);

        // =====================================
        //     LÓGICA DE VALIDACIÓN
        // =====================================
        final boolean[] resultado = new boolean[1];

        btnAceptar.addActionListener(e -> {
            String u = txtUsuario.getText().trim();
            String c = new String(txtContrasena.getPassword());

            if (u.isEmpty() || c.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios.");
                return;
            }

            usuario = u;
            contrasena = c;
            resultado[0] = true;
            dialog.dispose();
        });

        btnCancelar.addActionListener(e -> {
            usuario = null;
            contrasena = null;
            resultado[0] = false;
            dialog.dispose();
        });

        dialog.setVisible(true);
        return resultado[0];
    }

    public String getUsuario() { return usuario; }
    public String getContrasena() { return contrasena; }
}
