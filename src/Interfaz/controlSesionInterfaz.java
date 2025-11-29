package Interfaz;

import Funciones.controlSesiones;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

public class controlSesionInterfaz extends JPanel {

    private Connection conn;
    private JPanel panelLista;

    public controlSesionInterfaz(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());
        setBackground(new Color(210, 180, 140)); // café claro

        JLabel titulo = new JLabel("Control de Sesiones", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(90, 50, 30)); // café oscuro
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(new Color(235, 215, 185)); // tono café suave

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        add(scroll, BorderLayout.CENTER);

        JButton actualizar = new JButton("Actualizar");
        actualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actualizar.setForeground(Color.WHITE);
        actualizar.setBackground(new Color(120, 80, 40)); // café intenso
        actualizar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        actualizar.addActionListener(e -> cargarSesiones());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(210, 180, 140));
        panelBoton.add(actualizar);

        add(panelBoton, BorderLayout.SOUTH);

        cargarSesiones();
    }

    private void cargarSesiones() {
        panelLista.removeAll();

        ArrayList<Object[]> sesiones = controlSesiones.obtenerSesiones(conn);

        for (Object[] ses : sesiones) {

            int idSesion = (int) ses[0];
            int idEmp = (int) ses[1];
            String nombre = (String) ses[2];
            Object fecha = ses[3];

            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            fila.setBackground(Color.WHITE);
            fila.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 140, 100), 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel lbl = new JLabel(
                    "Sesión #" + idSesion +
                            " | ID Empleado: " + idEmp +
                            " | " + nombre +
                            " | Fecha: " + fecha
            );
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setForeground(new Color(80, 50, 20)); // café oscuro

            fila.add(lbl);
            panelLista.add(fila);
            panelLista.add(Box.createVerticalStrut(8));
        }

        panelLista.revalidate();
        panelLista.repaint();
    }
}
