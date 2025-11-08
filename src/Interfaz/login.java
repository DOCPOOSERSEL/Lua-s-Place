package Interfaz;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class login {
    private String usuario;
    private String contrasena;

    public boolean mostrarDialogo() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Login");
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField txtUsuario = new JTextField();
        JPasswordField txtContrasena = new JPasswordField();

        panelCampos.add(new JLabel("Id:"));
        panelCampos.add(txtUsuario);
        panelCampos.add(new JLabel("Contraseña:"));
        panelCampos.add(txtContrasena);

        JButton btnAceptar = new JButton("Aceptar");

        panel.add(panelCampos, BorderLayout.CENTER);
        panel.add(btnAceptar, BorderLayout.SOUTH);

        final boolean[] resultado = new boolean[1];

        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuario = txtUsuario.getText();
                contrasena = new String(txtContrasena.getPassword());
                resultado[0] = true;
                dialog.dispose();
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);

        return resultado[0];
    }

    // Getters para acceder a los datos
    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
}