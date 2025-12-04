package Interfaz;

import Funciones.crudUsuario;
import java.sql.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class crudUsuarioInterfaz extends JFrame {
    //Campos compartidos para funciones dinámicas
    public JTextField txtId, txtNombre, txtApP, txtApM, txtCP, txtRFC, txtCalle, txtColonia, txtFechaNac, txtFechaContrato;
    public JPasswordField txtContra;

    // Campos por pestaña
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

    public JComboBox<String> comboRolAgregar, comboRolActualizar;
    public String rolSeleccionado;
    private int idUsuarioEnSesion;

    // Permisos del usuario en sesión
    private boolean permisoCRUD = false;
    private boolean permisoADMIN = false;

    // ===========================================================
    // COLORES
    // ===========================================================
    private Color cafeClaro = new Color(210, 180, 140);
    private Color cafeSuave = new Color(235, 215, 185);
    private Color cafeOscuro = new Color(90, 50, 30);
    private Color cafeIntenso = new Color(120, 80, 40);
    private Color blanco = new Color(245, 245, 220);

    public crudUsuarioInterfaz(int idUsuario) {
        this.idUsuarioEnSesion = idUsuario;
    }

    public void setRolSeleccionado(String rol) { this.rolSeleccionado = rol; }
    public String getRolSeleccionado() { return rolSeleccionado; }

    // ===========================================================
    // TRAER PERMISOS DEL USUARIO EN SESIÓN
    // ===========================================================
    private void cargarPermisosUsuario(Connection conn) {
        try {
            String sql =
                    "SELECT r.crud_perm, r.admin_perm " +
                            "FROM empleado e " +
                            "JOIN rol r ON e.id_rol = r.id_rol " +
                            "WHERE e.id_emp = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuarioEnSesion);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                permisoCRUD = rs.getBoolean("crud_perm");
                permisoADMIN = rs.getBoolean("admin_perm");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ===========================================================
    // INTERFAZ PRINCIPAL
    // ===========================================================
    public void crudEmpleadosInterfaz(Connection conn) {

        // 📌 CARGAMOS PERMISOS DEL USUARIO
        cargarPermisosUsuario(conn);

        setTitle("CRUD de Empleados - Luas Place");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(cafeClaro);

        JTabbedPane pestañas = new JTabbedPane(JTabbedPane.LEFT);
        pestañas.setBackground(cafeIntenso);
        pestañas.setForeground(Color.WHITE);
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // SIEMPRE visible
        pestañas.add("Buscar / Eliminar", crearPanelBuscar(conn));

        // SOLO SI TIENE CRUD
        if (permisoCRUD) {
            pestañas.add("Agregar", crearPanelAgregar(conn));
            pestañas.add("Actualizar", crearPanelActualizar(conn));
        }

        // SOLO SI ES ADMIN
        if (permisoADMIN) {
            pestañas.add("Permisos", crearPanelPermisos(conn));
        }

        add(pestañas, BorderLayout.CENTER);
    }

    private JPanel crearPanelConFondo(JPanel original) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(cafeSuave);
        original.setBackground(cafeSuave);
        contenedor.add(original, BorderLayout.CENTER);
        return contenedor;
    }

    // ===========================================================
    // PANEL BUSCAR / ELIMINAR
    // ===========================================================
    private JPanel crearPanelBuscar(Connection conn) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cafeSuave);

        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.setBackground(cafeClaro);
        panelSuperior.add(new JLabel("ID Empleado:"));
        txtIdBuscar = new JTextField(10);
        txtIdBuscar.setBackground(blanco);
        txtIdBuscar.setForeground(cafeOscuro);
        panelSuperior.add(txtIdBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(cafeIntenso);
        btnBuscar.setForeground(Color.WHITE);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(cafeIntenso);
        btnEliminar.setForeground(Color.WHITE);

        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnEliminar);
        panel.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(9, 2, 5, 5));
        panelDatos.setBackground(cafeSuave);

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
                txtNombreBuscar, txtApPBuscar, txtApMBuscar,
                txtFechaContratoBuscar, txtFechaNacBuscar,
                txtCPBuscar, txtRFCBuscar, txtCalleBuscar, txtColoniaBuscar
        };

        for (JTextField txt : campos) {
            txt.setEditable(false);
            txt.setBackground(blanco);
            txt.setForeground(cafeOscuro);
        }

        panelDatos.add(new JLabel("Nombre:")); panelDatos.add(txtNombreBuscar);
        panelDatos.add(new JLabel("Apellido Paterno:")); panelDatos.add(txtApPBuscar);
        panelDatos.add(new JLabel("Apellido Materno:")); panelDatos.add(txtApMBuscar);
        panelDatos.add(new JLabel("Fecha Contrato:")); panelDatos.add(txtFechaContratoBuscar);
        panelDatos.add(new JLabel("Fecha Nacimiento:")); panelDatos.add(txtFechaNacBuscar);
        panelDatos.add(new JLabel("Código Postal:")); panelDatos.add(txtCPBuscar);
        panelDatos.add(new JLabel("RFC:")); panelDatos.add(txtRFCBuscar);
        panelDatos.add(new JLabel("Calle:")); panelDatos.add(txtCalleBuscar);
        panelDatos.add(new JLabel("Colonia:")); panelDatos.add(txtColoniaBuscar);

        panel.add(panelDatos, BorderLayout.CENTER);

        JPanel panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(cafeSuave);
        JScrollPane scroll = new JScrollPane(panelLista);
        panel.add(scroll, BorderLayout.SOUTH);

        Runnable cargarLista = () -> {
            panelLista.removeAll();
            try {
                String sql =
                        "SELECT id_emp, nombre_emp, apellidop_emp, fecha_contrato, contrato_emp " +
                                "FROM empleado ORDER BY id_emp";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id_emp");
                    String nombre = rs.getString("nombre_emp") + " " + rs.getString("apellidop_emp");
                    String fecha = rs.getString("fecha_contrato");
                    boolean activo = rs.getBoolean("contrato_emp");
                    JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    fila.setBackground(cafeSuave);
                    fila.setBorder(BorderFactory.createLineBorder(cafeOscuro, 1));
                    JLabel lbl = new JLabel("ID: " + id + " | Nombre: " + nombre + " | Fecha: " + fecha + " | Activo: " + (activo ? "Sí" : "No"));
                    lbl.setForeground(cafeOscuro);
                    fila.add(lbl);
                    panelLista.add(fila);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
            panelLista.revalidate();
            panelLista.repaint();
        };
        cargarLista.run();

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

    // ===========================================================
    // PANEL AGREGAR
    // ===========================================================
    private JPanel crearPanelAgregar(Connection conn) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelDatos = new JPanel(new GridLayout(11, 2, 5, 5));
        panelDatos.setBackground(cafeSuave);

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

        JTextField[] campos = {
                txtNombreAgregar, txtApPAgregar, txtApMAgregar, txtFechaContratoAgregar,
                txtFechaNacAgregar, txtCPAgregar, txtRFCAgregar, txtCalleAgregar, txtColoniaAgregar
        };

        for (JTextField txt : campos) {
            txt.setBackground(blanco);
            txt.setForeground(cafeOscuro);
        }

        txtContraAgregar.setBackground(blanco);
        txtContraAgregar.setForeground(cafeOscuro);

        comboRolAgregar = new JComboBox<>();

        crudUsuario func = new crudUsuario(this);
        ArrayList<Object[]> roles = func.obtenerRoles(conn);
        for (Object[] r : roles) comboRolAgregar.addItem((String) r[1]);

        panelDatos.add(new JLabel("Contraseña:")); panelDatos.add(txtContraAgregar);
        panelDatos.add(new JLabel("Nombre:")); panelDatos.add(txtNombreAgregar);
        panelDatos.add(new JLabel("Apellido Paterno:")); panelDatos.add(txtApPAgregar);
        panelDatos.add(new JLabel("Apellido Materno:")); panelDatos.add(txtApMAgregar);
        panelDatos.add(new JLabel("Fecha Contrato (YYYY-MM-DD):")); panelDatos.add(txtFechaContratoAgregar);
        panelDatos.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD):")); panelDatos.add(txtFechaNacAgregar);
        panelDatos.add(new JLabel("Código Postal:")); panelDatos.add(txtCPAgregar);
        panelDatos.add(new JLabel("RFC:")); panelDatos.add(txtRFCAgregar);
        panelDatos.add(new JLabel("Calle:")); panelDatos.add(txtCalleAgregar);
        panelDatos.add(new JLabel("Colonia:")); panelDatos.add(txtColoniaAgregar);
        panelDatos.add(new JLabel("Rol:")); panelDatos.add(comboRolAgregar);

        panel.add(panelDatos, BorderLayout.CENTER);

        JButton btnAgregar = new JButton("Agregar Empleado");
        btnAgregar.setBackground(cafeIntenso);
        btnAgregar.setForeground(Color.WHITE);
        panel.add(btnAgregar, BorderLayout.SOUTH);

        crudUsuario acciones = new crudUsuario(this);

        btnAgregar.addActionListener(e -> {
            asignarCamposAgregar();
            setRolSeleccionado(comboRolAgregar.getSelectedItem().toString());
            acciones.agregarUsuario(conn);
        });

        return panel;
    }

    // ===========================================================
    // PANEL ACTUALIZAR
    // ===========================================================
    private JPanel crearPanelActualizar(Connection conn) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cafeSuave);

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSuperior.setBackground(cafeClaro);
        JLabel lblId = new JLabel("ID Empleado a Actualizar:");
        lblId.setForeground(cafeOscuro);
        txtIdActualizar = new JTextField(10);
        txtIdActualizar.setBackground(blanco);
        txtIdActualizar.setForeground(cafeOscuro);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(cafeIntenso);
        btnBuscar.setForeground(Color.WHITE);

        panelSuperior.add(lblId);
        panelSuperior.add(txtIdActualizar);
        panelSuperior.add(btnBuscar);
        panel.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBackground(cafeSuave);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

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

        JTextField[] camposActualizar = {
                txtNombreActualizar, txtApPActualizar, txtApMActualizar,
                txtFechaContratoActualizar, txtFechaNacActualizar,
                txtCPActualizar, txtRFCActualizar, txtCalleActualizar, txtColoniaActualizar
        };

        for (JTextField txt : camposActualizar) {
            txt.setBackground(blanco);
            txt.setForeground(cafeOscuro);
        }

        txtContraActualizar.setBackground(blanco);
        txtContraActualizar.setForeground(cafeOscuro);

        comboRolActualizar = new JComboBox<>();
        crudUsuario func = new crudUsuario(this);
        ArrayList<Object[]> roles = func.obtenerRoles(conn);
        for (Object[] r : roles) comboRolActualizar.addItem((String) r[1]);

        java.util.List<JComponent[]> listaCampos = java.util.List.of(
                new JComponent[]{new JLabel("Contraseña:"), txtContraActualizar},
                new JComponent[]{new JLabel("Nombre:"), txtNombreActualizar},
                new JComponent[]{new JLabel("Apellido Paterno:"), txtApPActualizar},
                new JComponent[]{new JLabel("Apellido Materno:"), txtApMActualizar},
                new JComponent[]{new JLabel("Fecha Contrato (YYYY-MM-DD):"), txtFechaContratoActualizar},
                new JComponent[]{new JLabel("Fecha Nacimiento (YYYY-MM-DD):"), txtFechaNacActualizar},
                new JComponent[]{new JLabel("Código Postal:"), txtCPActualizar},
                new JComponent[]{new JLabel("RFC:"), txtRFCActualizar},
                new JComponent[]{new JLabel("Calle:"), txtCalleActualizar},
                new JComponent[]{new JLabel("Colonia:"), txtColoniaActualizar},
                new JComponent[]{new JLabel("Rol:"), comboRolActualizar}
        );

        for (JComponent[] c : listaCampos) {
            gbc.gridx = 0;
            panelDatos.add(c[0], gbc);
            gbc.gridx = 1;
            panelDatos.add(c[1], gbc);
            gbc.gridy++;
            if (c[0] instanceof JLabel) ((JLabel)c[0]).setForeground(cafeOscuro);
        }

        panel.add(panelDatos, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar Empleado");
        btnActualizar.setBackground(cafeIntenso);
        btnActualizar.setForeground(Color.WHITE);

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(cafeSuave);
        panelBoton.add(btnActualizar);
        panel.add(panelBoton, BorderLayout.SOUTH);

        crudUsuario acciones = new crudUsuario(this);

        btnBuscar.addActionListener(e -> {
            asignarCamposActualizar();
            acciones.buscarUsuario(conn);
        });

        btnActualizar.addActionListener(e -> {
            asignarCamposActualizar();
            setRolSeleccionado(comboRolActualizar.getSelectedItem().toString());
            acciones.actualizarUsuario(conn, idUsuarioEnSesion);
        });

        return panel;
    }

    // ===========================================================
    // PANEL PERMISOS
    // ===========================================================
    private JPanel crearPanelPermisos(Connection conn) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cafeSuave);

        JTabbedPane pestañasPermisos = new JTabbedPane(JTabbedPane.TOP);
        pestañasPermisos.setBackground(cafeIntenso);

        // --- Cambiar Rol ---
        JPanel panelRoles = new JPanel();
        panelRoles.setLayout(new BoxLayout(panelRoles, BoxLayout.Y_AXIS));
        panelRoles.setBackground(cafeSuave);

        crudUsuario funcUsuarios = new crudUsuario(this);
        ArrayList<Object[]> usuarios = funcUsuarios.obtenerPermisosUsuarios(conn);
        ArrayList<JComboBox<String>> combosUsuarios = new ArrayList<>();

        for (Object[] u : usuarios) {
            int idEmp = (int) u[0];
            String nombreEmp = (String) u[1];
            String rolActual = (String) u[2];

            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
            fila.setBackground(cafeSuave);
            fila.setBorder(BorderFactory.createLineBorder(cafeIntenso));

            JLabel lbl = new JLabel("ID: " + idEmp + " | " + nombreEmp);
            lbl.setForeground(cafeOscuro);
            fila.add(lbl);

            JComboBox<String> comboRol = new JComboBox<>();
            ArrayList<Object[]> roles = funcUsuarios.obtenerRoles(conn);
            for (Object[] r : roles) comboRol.addItem((String) r[1]);
            comboRol.setSelectedItem(rolActual);
            combosUsuarios.add(comboRol);
            fila.add(comboRol);

            panelRoles.add(fila);
        }

        JButton btnGuardarRoles = new JButton("Guardar Cambios de Roles");
        btnGuardarRoles.setBackground(cafeIntenso);
        btnGuardarRoles.setForeground(Color.WHITE);
        btnGuardarRoles.addActionListener(e -> {
            for (int i = 0; i < usuarios.size(); i++) {
                int idEmp = (int) usuarios.get(i)[0];
                String nuevoRol = (String) combosUsuarios.get(i).getSelectedItem();
                funcUsuarios.actualizarRolUsuario(conn, idEmp, nuevoRol);
            }
            JOptionPane.showMessageDialog(null, "Roles actualizados correctamente.");
        });

        panelRoles.add(btnGuardarRoles);

        // --- Permisos por Rol ---
        JPanel panelPermisosRol = new JPanel();
        panelPermisosRol.setLayout(new BoxLayout(panelPermisosRol, BoxLayout.Y_AXIS));
        panelPermisosRol.setBackground(cafeSuave);

        ArrayList<Object[]> roles = funcUsuarios.obtenerRoles(conn);
        ArrayList<JCheckBox[]> listaChecks = new ArrayList<>();

        for (Object[] r : roles) {
            int idRol = (int) r[0];
            String nombreRol = (String) r[1];
            boolean venta = (boolean) r[2];
            boolean inventario = (boolean) r[3];
            boolean crud = (boolean) r[4];
            boolean admin = (boolean) r[5];

            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
            fila.setBackground(cafeSuave);
            fila.setBorder(BorderFactory.createLineBorder(cafeIntenso));

            JLabel lbl = new JLabel(nombreRol);
            lbl.setForeground(cafeOscuro);
            fila.add(lbl);

            JCheckBox[] checks = new JCheckBox[4];
            checks[0] = new JCheckBox("Venta", venta);
            checks[1] = new JCheckBox("Inventario", inventario);
            checks[2] = new JCheckBox("CRUD", crud);
            checks[3] = new JCheckBox("Admin", admin);

            for (JCheckBox cb : checks) {
                cb.setBackground(cafeSuave);
                cb.setForeground(cafeOscuro);
                fila.add(cb);
            }

            listaChecks.add(checks);
            panelPermisosRol.add(fila);
        }

        JButton btnGuardarPermisos = new JButton("Guardar Permisos");
        btnGuardarPermisos.setBackground(cafeIntenso);
        btnGuardarPermisos.setForeground(Color.WHITE);
        btnGuardarPermisos.addActionListener(e -> {
            for (int i = 0; i < roles.size(); i++) {
                int idRol = (int) roles.get(i)[0];
                JCheckBox[] checks = listaChecks.get(i);
                funcUsuarios.actualizarPermisosRol(
                        conn,
                        idRol,
                        checks[0].isSelected(),
                        checks[1].isSelected(),
                        checks[2].isSelected(),
                        checks[3].isSelected()
                );
            }
            JOptionPane.showMessageDialog(null, "Permisos actualizados correctamente.");
        });

        panelPermisosRol.add(btnGuardarPermisos);

        pestañasPermisos.add("Roles de Usuarios", panelRoles);
        pestañasPermisos.add("Permisos por Rol", panelPermisosRol);
        panel.add(pestañasPermisos, BorderLayout.CENTER);

        return panel;
    }

    // ===========================================================
    // ASIGNAR CAMPOS
    // ===========================================================
    private void asignarCamposBusqueda() {
        txtNombre = txtNombreBuscar;
        txtApP = txtApPBuscar;
        txtApM = txtApMBuscar;
        txtFechaContrato = txtFechaContratoBuscar;
        txtFechaNac = txtFechaNacBuscar;
        txtCP = txtCPBuscar;
        txtRFC = txtRFCBuscar;
        txtCalle = txtCalleBuscar;
        txtColonia = txtColoniaBuscar;
        txtContra = txtContraBuscar;
        txtId = txtIdBuscar;
    }

    private void asignarCamposAgregar() {
        txtNombre = txtNombreAgregar;
        txtApP = txtApPAgregar;
        txtApM = txtApMAgregar;
        txtFechaContrato = txtFechaContratoAgregar;
        txtFechaNac = txtFechaNacAgregar;
        txtCP = txtCPAgregar;
        txtRFC = txtRFCAgregar;
        txtCalle = txtCalleAgregar;
        txtColonia = txtColoniaAgregar;
        txtContra = txtContraAgregar;
    }

    private void asignarCamposActualizar() {
        txtNombre = txtNombreActualizar;
        txtApP = txtApPActualizar;
        txtApM = txtApMActualizar;
        txtFechaContrato = txtFechaContratoActualizar;
        txtFechaNac = txtFechaNacActualizar;
        txtCP = txtCPActualizar;
        txtRFC = txtRFCActualizar;
        txtCalle = txtCalleActualizar;
        txtColonia = txtColoniaActualizar;
        txtContra = txtContraActualizar;
        txtId = txtIdActualizar;
    }
}
