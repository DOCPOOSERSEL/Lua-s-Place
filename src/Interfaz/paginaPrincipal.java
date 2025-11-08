package Interfaz;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class paginaPrincipal extends JFrame{
    public void MenuPrincipal(Connection conn) {
        setTitle("Luas Place - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 255)); // color suave

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titulo = new JLabel("Bienvenido a Luas Place");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(50, 50, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titulo, gbc);


        // Botón CRUD Clientes
        JButton btnClientes = new JButton("CRUD Clientes");
        btnClientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crudUsuarioInterfaz crud = new crudUsuarioInterfaz();
                crud.crudEmpleadosInterfaz(conn);
                crud.setVisible(true);
            }
        });
        gbc.gridy = 1;
        panel.add(btnClientes, gbc);

        // Botón Consultas Personalizadas
        JButton btnConsultas = new JButton("Consultas Personalizadas");
        btnConsultas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnConsultas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        gbc.gridy = 2;
        panel.add(btnConsultas, gbc);

        add(panel);
    }
}
