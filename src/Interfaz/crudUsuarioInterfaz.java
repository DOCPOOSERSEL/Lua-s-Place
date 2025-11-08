package Interfaz;

import Funciones.crudUsuario;
import java.sql.Connection;
import java.awt.*;
import javax.swing.*;

public class crudUsuarioInterfaz extends JFrame {

    // Campos públicos para acceso desde Funciones
    public JTextField txtId, txtNombre, txtApP, txtApM, txtCP, txtRFC, txtCalle, txtColonia, txtFechaNac, txtFechaContrato;
    public JPasswordField txtContra;
    private JButton btnBuscar, btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    public void crudEmpleadosInterfaz(Connection conn) {
        setTitle("CRUD de Empleados");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelDatos = new JPanel(new GridLayout(12, 2, 5, 5));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelDatos.add(new JLabel("ID Empleado:"));
        txtId = new JTextField();
        panelDatos.add(txtId);

        panelDatos.add(new JLabel("Contraseña:"));
        txtContra = new JPasswordField();
        panelDatos.add(txtContra);

        panelDatos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelDatos.add(txtNombre);

        panelDatos.add(new JLabel("Apellido Paterno:"));
        txtApP = new JTextField();
        panelDatos.add(txtApP);

        panelDatos.add(new JLabel("Apellido Materno:"));
        txtApM = new JTextField();
        panelDatos.add(txtApM);

        panelDatos.add(new JLabel("Fecha de Contrato (YYYY-MM-DD):"));
        txtFechaContrato = new JTextField();
        panelDatos.add(txtFechaContrato);

        panelDatos.add(new JLabel("Fecha de Nacimiento (YYYY-MM-DD):"));
        txtFechaNac = new JTextField();
        panelDatos.add(txtFechaNac);

        panelDatos.add(new JLabel("Código Postal:"));
        txtCP = new JTextField();
        panelDatos.add(txtCP);

        panelDatos.add(new JLabel("RFC:"));
        txtRFC = new JTextField();
        panelDatos.add(txtRFC);

        panelDatos.add(new JLabel("Calle:"));
        txtCalle = new JTextField();
        panelDatos.add(txtCalle);

        panelDatos.add(new JLabel("Colonia:"));
        txtColonia = new JTextField();
        panelDatos.add(txtColonia);

        add(panelDatos, BorderLayout.CENTER);

        // ----- Botones -----
        JPanel panelBotones = new JPanel();
        btnBuscar = new JButton("Buscar");
        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnBuscar);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);

        // ----- Acciones -----
        crudUsuario acciones = new crudUsuario(this);
        btnBuscar.addActionListener(e -> acciones.buscarUsuario(conn));
        btnAgregar.addActionListener(e -> acciones.agregarUsuario(conn));
        btnActualizar.addActionListener(e -> acciones.actualizarUsuario(conn));
        btnEliminar.addActionListener(e -> acciones.eliminarUsuario(conn));
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtContra.setText("");
        txtNombre.setText("");
        txtApP.setText("");
        txtApM.setText("");
        txtFechaContrato.setText("");
        txtFechaNac.setText("");
        txtCP.setText("");
        txtRFC.setText("");
        txtCalle.setText("");
        txtColonia.setText("");
    }
}
