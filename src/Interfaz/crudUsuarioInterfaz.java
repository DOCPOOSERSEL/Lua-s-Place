package Interfaz;

import Funciones.crudUsuario;
import java.sql.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class crudUsuarioInterfaz extends JFrame {
    //Texto compartido para uso dinamico de mis funciones y para que no se pasen la pinche informacion entre pestañas
    public JTextField txtId, txtNombre, txtApP, txtApM, txtCP, txtRFC, txtCalle, txtColonia, txtFechaNac, txtFechaContrato;
    public JPasswordField txtContra;

    // Campos propios de cada sub pestaña
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
    public JComboBox<String> comboRolAgregar;
    public String rolSeleccionado;
    private int idUsuarioEnSesion;

    public crudUsuarioInterfaz(int idUsuario) {
        this.idUsuarioEnSesion = idUsuario;
    }


    public void setRolSeleccionado(String rol) {
        this.rolSeleccionado = rol;
    }

    public String getRolSeleccionado() {
        return rolSeleccionado;
    }

    public crudUsuarioInterfaz() {

        UIManager.put("TabbedPane.selected", new Color(130, 80, 50));
        UIManager.put("TabbedPane.contentAreaColor", new Color(240, 225, 210));
        UIManager.put("TabbedPane.focus", new Color(150, 100, 70));
        UIManager.put("TabbedPane.tabsOpaque", false);

        UIManager.put("Panel.background", new Color(245, 235, 220));
        UIManager.put("Button.background", new Color(200, 170, 140));
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Label.foreground", new Color(60, 40, 20));
        UIManager.put("TextField.background", new Color(255, 250, 240));
        UIManager.put("PasswordField.background", new Color(255, 250, 240));

    }

    private JPanel crearPanelConFondo(JPanel original) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(245, 235, 220));
        original.setBackground(new Color(250, 240, 230));
        contenedor.add(original, BorderLayout.CENTER);
        return contenedor;
    }

    public void crudEmpleadosInterfaz(Connection conn) {
        setTitle("CRUD de Empleados - Luas Place");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Fondo general
        getContentPane().setBackground(new Color(245, 235, 220));


        JTabbedPane pestañas = new JTabbedPane(JTabbedPane.LEFT);
        pestañas.setBackground(new Color(220, 200, 180));
        pestañas.setForeground(Color.BLACK);
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 14));

        pestañas.add("Buscar / Eliminar", crearPanelConFondo(crearPanelBuscar(conn)));
        pestañas.add("Agregar", crearPanelConFondo(crearPanelAgregar(conn)));
        pestañas.add("Actualizar", crearPanelConFondo(crearPanelActualizar(conn)));
        pestañas.add("Permisos", crearPanelConFondo(crearPanelPermisos(conn)));

        add(pestañas, BorderLayout.CENTER);
    }


    private JPanel crearPanelBuscar(Connection conn) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 235, 220));

        // ===========================================================
        //      PANEL SUPERIOR (Buscar / Eliminar)
        // ===========================================================
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.setBackground(new Color(240, 225, 205));

        panelSuperior.add(new JLabel("ID Empleado:"));
        txtIdBuscar = new JTextField(10);
        panelSuperior.add(txtIdBuscar);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnEliminar = new JButton("Eliminar");
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnEliminar);
        panel.add(panelSuperior, BorderLayout.NORTH);

        // Estilo a botones café
        JButton[] botones = {btnBuscar, btnEliminar};
        for (JButton btn : botones) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(110, 70, 40));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(140, 90, 55));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(110, 70, 40));
                }
            });
        }

        // ===========================================================
        //      PANEL DE DATOS DEL EMPLEADO BUSCADO
        // ===========================================================
        JPanel panelDatos = new JPanel(new GridLayout(9, 2, 5, 5));
        panelDatos.setBackground(new Color(255, 245, 230));
        panelDatos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 110, 80), 2, true),
                "Información del Empleado",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(90, 55, 30)
        ));

        txtNombreBuscar = new JTextField();
        txtApPBuscar = new JTextField();
        txtApMBuscar = new JTextField();
        txtFechaContratoBuscar = new JTextField();
        txtFechaNacBuscar = new JTextField();
        txtCPBuscar = new JTextField();
        txtRFCBuscar = new JTextField();
        txtCalleBuscar = new JTextField();
        txtColoniaBuscar = new JTextField();

        JTextField[] campos = {
                txtNombreBuscar, txtApPBuscar, txtApMBuscar, txtFechaContratoBuscar,
                txtFechaNacBuscar, txtCPBuscar, txtRFCBuscar, txtCalleBuscar, txtColoniaBuscar
        };
        for (JTextField txt : campos) txt.setEditable(false);

        panelDatos.add(new JLabel("Nombre:")); panelDatos.add(txtNombreBuscar);
        panelDatos.add(new JLabel("Apellido Paterno:")); panelDatos.add(txtApPBuscar);
        panelDatos.add(new JLabel("Apellido Materno:")); panelDatos.add(txtApMBuscar);
        panelDatos.add(new JLabel("Fecha de Contrato:")); panelDatos.add(txtFechaContratoBuscar);
        panelDatos.add(new JLabel("Fecha de Nacimiento:")); panelDatos.add(txtFechaNacBuscar);
        panelDatos.add(new JLabel("Código Postal:")); panelDatos.add(txtCPBuscar);
        panelDatos.add(new JLabel("RFC:")); panelDatos.add(txtRFCBuscar);
        panelDatos.add(new JLabel("Calle:")); panelDatos.add(txtCalleBuscar);
        panelDatos.add(new JLabel("Colonia:")); panelDatos.add(txtColoniaBuscar);

        panel.add(panelDatos, BorderLayout.CENTER);

        // ===========================================================
        //      LISTA DE TODOS LOS EMPLEADOS (ID, Nombre, Fecha, Activo)
        // ===========================================================
        JPanel panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(new Color(245, 235, 220));

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(120, 80, 50), 2, true),
                "Lista de Empleados",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(90, 55, 30)
        ));

        panel.add(scroll, BorderLayout.SOUTH);

        Runnable cargarLista = () -> {
            panelLista.removeAll();

            try {
                String sql = "SELECT id_emp, nombre_emp, apellidop_emp, fecha_contrato, contrato_emp FROM empleado ORDER BY id_emp";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id_emp");
                    String nombre = rs.getString("nombre_emp") + " " + rs.getString("apellidop_emp");
                    String fecha = rs.getString("fecha_contrato");
                    boolean activo = rs.getBoolean("contrato_emp");

                    JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    fila.setBackground(new Color(255, 248, 236));

                    fila.add(new JLabel("ID: " + id + " | "));
                    fila.add(new JLabel("Nombre: " + nombre + " | "));
                    fila.add(new JLabel("Fecha: " + fecha + " | "));
                    fila.add(new JLabel("Activo: " + (activo ? "Sí" : "No")));

                    panelLista.add(fila);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            panelLista.revalidate();
            panelLista.repaint();
        };

        cargarLista.run();

        // ===========================================================
        //      ACCIONES DE BOTONES
        // ===========================================================
        crudUsuario acciones = new crudUsuario(this);

        btnBuscar.addActionListener(e -> {
            asignarCamposBusqueda();
            acciones.buscarUsuario(conn);
        });

        btnEliminar.addActionListener(e -> {
            asignarCamposBusqueda();
            acciones.eliminarUsuario(conn, idUsuarioEnSesion);
            cargarLista.run();
        });

        return panel;
    }


    private JPanel crearPanelAgregar(Connection conn) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelDatos = new JPanel(new GridLayout(11, 2, 5, 5));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Nuevo Empleado"));

        panel.setBackground(new Color(245, 235, 220));
        panelDatos.setBackground(new Color(255, 245, 230));

        panelDatos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 110, 80), 2, true),
                "Nuevo Empleado",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(90, 55, 30)
        ));

        for (Component comp : panelDatos.getComponents()) {
            if (comp instanceof JLabel)
                comp.setForeground(new Color(90, 55, 30));

            if (comp instanceof JTextField || comp instanceof JPasswordField || comp instanceof JComboBox)
                comp.setBackground(new Color(255, 250, 240));
        }

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

        comboRolAgregar = new JComboBox<>(new String[] {
                "Empleado",
                "Cajero",
                "Manager",
                "Administrador"
        });

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

        panelDatos.add(new JLabel("Rol del Empleado:"));
        panelDatos.add(comboRolAgregar);

        panel.add(panelDatos, BorderLayout.CENTER);

        JButton btnAgregar = new JButton("Agregar Empleado");
        panel.add(btnAgregar, BorderLayout.SOUTH);

        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBackground(new Color(110, 70, 40));
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(8,15,8,15));

        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAgregar.setBackground(new Color(140, 90, 55));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAgregar.setBackground(new Color(110, 70, 40));
            }
        });

        crudUsuario funcionUsuario = new crudUsuario(this);

        btnAgregar.addActionListener(e -> {
            asignarCamposAgregar();
            setRolSeleccionado(comboRolAgregar.getSelectedItem().toString());
            funcionUsuario.agregarUsuario(conn);
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

        panel.setBackground(new Color(245, 235, 220));
        panelSuperior.setBackground(new Color(240, 225, 205));
        panelDatos.setBackground(new Color(255, 245, 230));

        panelDatos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 110, 80), 2, true),
                "Actualizar Información",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(90, 55, 30)
        ));

        for (Component comp : panelDatos.getComponents()) {
            if (comp instanceof JLabel)
                comp.setForeground(new Color(90, 55, 30));

            if (comp instanceof JTextField || comp instanceof JPasswordField)
                comp.setBackground(new Color(255, 250, 240));
        }

        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setBackground(new Color(110, 70, 40));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscar.setBackground(new Color(140, 90, 55));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscar.setBackground(new Color(110, 70, 40));
            }
        });


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

        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(110, 70, 40));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorder(BorderFactory.createEmptyBorder(8,15,8,15));

        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnActualizar.setBackground(new Color(140, 90, 55));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnActualizar.setBackground(new Color(110, 70, 40));
            }
        });


        crudUsuario acciones = new crudUsuario(this);

        btnBuscar.addActionListener(e -> {
            asignarCamposActualizar();
            acciones.buscarUsuario(conn);
        });

        btnActualizar.addActionListener(e -> {
            asignarCamposActualizar();
            acciones.actualizarUsuario(conn, idUsuarioEnSesion);
        });

        return panel;
    }

    private JPanel crearPanelPermisos(Connection conn) {
        crudUsuario funcUsuarios = new crudUsuario(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(120, 80, 50), 2, true),
                "Gestión de Permisos",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(90, 55, 30)
        ));
        panel.setBackground(new Color(240, 225, 205));

        JPanel panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(new Color(240, 225, 205));

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        ArrayList<JCheckBox[]> listaChecks = new ArrayList<>();

        // ===== MÉTODO PARA CARGAR USUARIOS =====
        Runnable cargarUsuarios = () -> {
            panelLista.removeAll();
            listaChecks.clear();

            ArrayList<Object[]> listaUsuarios = funcUsuarios.obtenerPermisosUsuarios(conn);

            for (Object[] usuario : listaUsuarios) {
                int idEmp = (int) usuario[0];
                String nombreEmp = (String) usuario[1];

                boolean venta = (boolean) usuario[2];
                boolean inventario = (boolean) usuario[3];
                boolean crud = (boolean) usuario[4];
                boolean admin = (boolean) usuario[5];

                JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
                fila.setBackground(new Color(255, 245, 230));
                fila.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 150, 120)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));

                JLabel lblUsuario = new JLabel("ID: " + idEmp + "  |  " + nombreEmp);
                lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblUsuario.setForeground(new Color(90, 55, 30));
                fila.add(lblUsuario);

                JCheckBox chkVenta = new JCheckBox("Venta", venta);
                JCheckBox chkInventario = new JCheckBox("Inventario", inventario);
                JCheckBox chkCrud = new JCheckBox("CRUD", crud);
                JCheckBox chkAdmin = new JCheckBox("Admin", admin);

                JCheckBox[] conjuntoChecks = {chkVenta, chkInventario, chkCrud, chkAdmin};
                listaChecks.add(conjuntoChecks);

                for (JCheckBox c : conjuntoChecks) {
                    c.setBackground(new Color(255, 245, 230));
                    c.setForeground(new Color(110, 70, 40));
                    fila.add(c);
                }

                fila.putClientProperty("id_emp", idEmp);

                panelLista.add(fila);
                panelLista.add(Box.createVerticalStrut(8));
            }

            panelLista.revalidate();
            panelLista.repaint();
        };

        // Primera carga
        cargarUsuarios.run();

        // ===== BOTÓN GUARDAR CAMBIOS =====
        JButton btnActualizar = new JButton("Guardar Cambios");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(110, 70, 40));
        btnActualizar.setFocusPainted(false);

        btnActualizar.addActionListener(e -> {
            for (int i = 0; i < panelLista.getComponentCount(); i += 2) {
                JPanel fila = (JPanel) panelLista.getComponent(i);
                int idEmp = (int) fila.getClientProperty("id_emp");

                JCheckBox[] boxes = listaChecks.get(i / 2);

                funcUsuarios.actualizarPermisos(
                        conn,
                        idEmp,
                        boxes[0].isSelected(),
                        boxes[1].isSelected(),
                        boxes[2].isSelected(),
                        boxes[3].isSelected()
                );
            }

            JOptionPane.showMessageDialog(null,
                    "Permisos actualizados correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ===== BOTÓN REFRESCAR TABLA =====
        JButton btnRefrescar = new JButton("Actualizar Lista");
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefrescar.setBackground(new Color(150, 100, 60));
        btnRefrescar.setForeground(Color.WHITE);

        btnRefrescar.addActionListener(e -> {
            cargarUsuarios.run();

        });

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(240, 225, 205));
        panelBotones.add(btnActualizar);
        panelBotones.add(btnRefrescar);

        panel.add(panelBotones, BorderLayout.SOUTH);

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

//Llevo dias haciendo namas este pedasoooooooa sdohasfoihasfihasfas
//Agregar botones paa buscar por ciudad o asi q ya namas llame a unas funciones que cambien pero ya me canseeeeeeefirijoperiojaejioñ
