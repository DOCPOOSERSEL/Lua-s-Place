package Interfaz;

import Funciones.crudUsuario;
import java.sql.*;
import java.awt.*;
import javax.swing.*;

public class crudUsuarioInterfaz extends JFrame {
    //Texto compartido para uso dinamico de mis funciones y para que no se pasen la pinche informacion entre pestañas
    public JTextField txtId, txtNombre, txtApP, txtApM, txtCP, txtRFC, txtCalle, txtColonia, txtFechaNac, txtFechaContrato;
    public JPasswordField txtContra;

    // Campos propios de cada pestaña
    private JTextField txtIdBuscar, txtIdActualizar;
    private JPasswordField txtContraBuscar, txtContraAgregar, txtContraActualizar;
    private JTextField txtNombreBuscar, txtNombreAgregar, txtNombreActualizar;
    private JTextField txtApPBuscar, txtApPAgregar, txtApPActualizar;
    private JTextField txtApMBuscar, txtApMAgregar, txtApMActualizar;
    private JTextField txtFechaContratoBuscar, txtFechaContratoAgregar, txtFechaContratoActualizar;
    private JTextField txtFechaNacBuscar, txtFechaNacAgregar, txtFechaNacActualizar;
    private JTextField txtCPBuscar, txtCPAgregar, txtCPActualizar;
    private JTextField txtRFCBuscar, txtRFCAgregar, txtRFCActualizar;
    private JTextField txtCalleBuscar, txtCalleAgregar, txtCalleActualizar;
    private JTextField txtColoniaBuscar, txtColoniaAgregar, txtColoniaActualizar;

    public void crudEmpleadosInterfaz(Connection conn) {
        setTitle("CRUD de Empleados");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.add("Buscar / Eliminar", crearPanelBuscar(conn));
        pestañas.add("Agregar", crearPanelAgregar(conn));
        pestañas.add("Actualizar", crearPanelActualizar(conn));

        add(pestañas, BorderLayout.CENTER);
    }


    private JPanel crearPanelBuscar(Connection conn) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel(new FlowLayout());

        panelSuperior.add(new JLabel("ID Empleado:"));
        txtIdBuscar = new JTextField(10);
        panelSuperior.add(txtIdBuscar);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnEliminar = new JButton("Eliminar");
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnEliminar);
        panel.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(10, 2, 5, 5));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Información del Empleado"));

        txtContraBuscar = new JPasswordField();
        txtNombreBuscar = new JTextField();
        txtApPBuscar = new JTextField();
        txtApMBuscar = new JTextField();
        txtFechaContratoBuscar = new JTextField();
        txtFechaNacBuscar = new JTextField();
        txtCPBuscar = new JTextField();
        txtRFCBuscar = new JTextField();
        txtCalleBuscar = new JTextField();
        txtColoniaBuscar = new JTextField();

        txtContraBuscar.setEditable(false);
        txtNombreBuscar.setEditable(false);
        txtApPBuscar.setEditable(false);
        txtApMBuscar.setEditable(false);
        txtFechaContratoBuscar.setEditable(false);
        txtFechaNacBuscar.setEditable(false);
        txtCPBuscar.setEditable(false);
        txtRFCBuscar.setEditable(false);
        txtCalleBuscar.setEditable(false);
        txtColoniaBuscar.setEditable(false);


        panelDatos.add(new JLabel("Contraseña:"));
        panelDatos.add(txtContraBuscar);
        panelDatos.add(new JLabel("Nombre:"));
        panelDatos.add(txtNombreBuscar);
        panelDatos.add(new JLabel("Apellido Paterno:"));
        panelDatos.add(txtApPBuscar);
        panelDatos.add(new JLabel("Apellido Materno:"));
        panelDatos.add(txtApMBuscar);
        panelDatos.add(new JLabel("Fecha de Contrato:"));
        panelDatos.add(txtFechaContratoBuscar);
        panelDatos.add(new JLabel("Fecha de Nacimiento:"));
        panelDatos.add(txtFechaNacBuscar);
        panelDatos.add(new JLabel("Código Postal:"));
        panelDatos.add(txtCPBuscar);
        panelDatos.add(new JLabel("RFC:"));
        panelDatos.add(txtRFCBuscar);
        panelDatos.add(new JLabel("Calle:"));
        panelDatos.add(txtCalleBuscar);
        panelDatos.add(new JLabel("Colonia:"));
        panelDatos.add(txtColoniaBuscar);

        panel.add(panelDatos, BorderLayout.CENTER);

        crudUsuario acciones = new crudUsuario(this);
        btnBuscar.addActionListener(e -> {
            asignarCamposBusqueda(); // conecta los campos específicos
            acciones.buscarUsuario(conn);
        });
        btnEliminar.addActionListener(e -> {
            asignarCamposBusqueda();
            acciones.eliminarUsuario(conn);
        });

        return panel;
    }


    private JPanel crearPanelAgregar(Connection conn) {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelDatos = new JPanel(new GridLayout(10, 2, 5, 5));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Nuevo Empleado"));

        txtContraAgregar = new JPasswordField();
        txtNombreAgregar = new JTextField();
        txtApPAgregar = new JTextField();
        txtApMAgregar = new JTextField();
        txtFechaContratoAgregar = new JTextField();
        txtFechaNacAgregar = new JTextField();
        txtCPAgregar = new JTextField();
        txtRFCAgregar = new JTextField();
        txtCalleAgregar = new JTextField();
        txtColoniaAgregar = new JTextField();

        panelDatos.add(new JLabel("Contraseña:"));
        panelDatos.add(txtContraAgregar);
        panelDatos.add(new JLabel("Nombre:"));
        panelDatos.add(txtNombreAgregar);
        panelDatos.add(new JLabel("Apellido Paterno:"));
        panelDatos.add(txtApPAgregar);
        panelDatos.add(new JLabel("Apellido Materno:"));
        panelDatos.add(txtApMAgregar);
        panelDatos.add(new JLabel("Fecha de Contrato (YYYY-MM-DD):"));
        panelDatos.add(txtFechaContratoAgregar);
        panelDatos.add(new JLabel("Fecha de Nacimiento (YYYY-MM-DD):"));
        panelDatos.add(txtFechaNacAgregar);
        panelDatos.add(new JLabel("Código Postal:"));
        panelDatos.add(txtCPAgregar);
        panelDatos.add(new JLabel("RFC:"));
        panelDatos.add(txtRFCAgregar);
        panelDatos.add(new JLabel("Calle:"));
        panelDatos.add(txtCalleAgregar);
        panelDatos.add(new JLabel("Colonia:"));
        panelDatos.add(txtColoniaAgregar);

        panel.add(panelDatos, BorderLayout.CENTER);

        JButton btnAgregar = new JButton("Agregar Empleado");
        panel.add(btnAgregar, BorderLayout.SOUTH);

        crudUsuario acciones = new crudUsuario(this);
        btnAgregar.addActionListener(e -> {
            asignarCamposAgregar();
            acciones.agregarUsuario(conn);
        });

        return panel;
    }


    private JPanel crearPanelActualizar(Connection conn) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.add(new JLabel("ID Empleado a Actualizar:"));
        txtIdActualizar = new JTextField(10);
        panelSuperior.add(txtIdActualizar);

        JButton btnBuscar = new JButton("Buscar");
        panelSuperior.add(btnBuscar);
        panel.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(10, 2, 5, 5));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Actualizar Información"));

        txtContraActualizar = new JPasswordField();
        txtNombreActualizar = new JTextField();
        txtApPActualizar = new JTextField();
        txtApMActualizar = new JTextField();
        txtFechaContratoActualizar = new JTextField();
        txtFechaNacActualizar = new JTextField();
        txtCPActualizar = new JTextField();
        txtRFCActualizar = new JTextField();
        txtCalleActualizar = new JTextField();
        txtColoniaActualizar = new JTextField();

        panelDatos.add(new JLabel("Contraseña:"));
        panelDatos.add(txtContraActualizar);
        panelDatos.add(new JLabel("Nombre:"));
        panelDatos.add(txtNombreActualizar);
        panelDatos.add(new JLabel("Apellido Paterno:"));
        panelDatos.add(txtApPActualizar);
        panelDatos.add(new JLabel("Apellido Materno:"));
        panelDatos.add(txtApMActualizar);
        panelDatos.add(new JLabel("Fecha de Contrato (YYYY-MM-DD):"));
        panelDatos.add(txtFechaContratoActualizar);
        panelDatos.add(new JLabel("Fecha de Nacimiento (YYYY-MM-DD):"));
        panelDatos.add(txtFechaNacActualizar);
        panelDatos.add(new JLabel("Código Postal:"));
        panelDatos.add(txtCPActualizar);
        panelDatos.add(new JLabel("RFC:"));
        panelDatos.add(txtRFCActualizar);
        panelDatos.add(new JLabel("Calle:"));
        panelDatos.add(txtCalleActualizar);
        panelDatos.add(new JLabel("Colonia:"));
        panelDatos.add(txtColoniaActualizar);

        panel.add(panelDatos, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar Empleado");
        panel.add(btnActualizar, BorderLayout.SOUTH);

        crudUsuario acciones = new crudUsuario(this);

        btnBuscar.addActionListener(e -> {
            asignarCamposActualizar();
            acciones.buscarUsuario(conn);
        });

        btnActualizar.addActionListener(e -> {
            asignarCamposActualizar();
            acciones.actualizarUsuario(conn);
        });

        return panel;
    }


    //Da lugar a los campos q estan estaticos para que las pestañas no se pasen informacion y los metodos tomen de las
    // mismos campos de texto ya matenmeeeeeeeeeeeeeeeeeeeeeeee
    private void asignarCamposBusqueda() {
        txtId = txtIdBuscar;
        txtContra = txtContraBuscar;
        txtNombre = txtNombreBuscar;
        txtApP = txtApPBuscar;
        txtApM = txtApMBuscar;
        txtFechaContrato = txtFechaContratoBuscar;
        txtFechaNac = txtFechaNacBuscar;
        txtCP = txtCPBuscar;
        txtRFC = txtRFCBuscar;
        txtCalle = txtCalleBuscar;
        txtColonia = txtColoniaBuscar;
    }

    private void asignarCamposAgregar() {
        txtContra = txtContraAgregar;
        txtNombre = txtNombreAgregar;
        txtApP = txtApPAgregar;
        txtApM = txtApMAgregar;
        txtFechaContrato = txtFechaContratoAgregar;
        txtFechaNac = txtFechaNacAgregar;
        txtCP = txtCPAgregar;
        txtRFC = txtRFCAgregar;
        txtCalle = txtCalleAgregar;
        txtColonia = txtColoniaAgregar;
    }

    private void asignarCamposActualizar() {
        txtId = txtIdActualizar;
        txtContra = txtContraActualizar;
        txtNombre = txtNombreActualizar;
        txtApP = txtApPActualizar;
        txtApM = txtApMActualizar;
        txtFechaContrato = txtFechaContratoActualizar;
        txtFechaNac = txtFechaNacActualizar;
        txtCP = txtCPActualizar;
        txtRFC = txtRFCActualizar;
        txtCalle = txtCalleActualizar;
        txtColonia = txtColoniaActualizar;
    }
}

//Llevo semanas haciendo namas este pedasoooooooa sdohasfoihasfihasfas
//Agregar botones paa buscar por ciudad o asi q ya namas llame a unas funciones que cambien pero ya me canseeeeeeefirijoperiojaejioñ
