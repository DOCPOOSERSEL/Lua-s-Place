package Interfaz;

import javax.swing.*;
import java.awt.*;

public class errorDeUsuario extends JFrame {
    public void errorLogin() {
        setTitle("Error de inicio de sesión");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JLabel mensaje = new JLabel("Usuario o contraseña incorrecta");
        mensaje.setFont(new Font("Arial", Font.BOLD, 16));
        mensaje.setForeground(Color.RED);

        panel.add(mensaje);
        add(panel);
    }
}
