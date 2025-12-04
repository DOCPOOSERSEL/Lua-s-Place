import Datos.accesoBaseDeDatos;
import Funciones.controlSesiones;
import Interfaz.login;
import Funciones.inicioDeSesion;
import Interfaz.errorDeUsuario;
import Interfaz.paginaPrincipal;

import java.sql.Connection;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        Connection conn = accesoBaseDeDatos.getConnection();
        login loginUsuario = new login();
        String miUsuario = "", miContrasena = "";

        if (loginUsuario.mostrarDialogo()) {
            miUsuario = loginUsuario.getUsuario();
            miContrasena = loginUsuario.getContrasena();
        }

        if (inicioDeSesion.verificarContraseña(Integer.parseInt(miUsuario), miContrasena, conn)) {
            paginaPrincipal pagina = new paginaPrincipal();
            controlSesiones.registrarSesion(Integer.parseInt(miUsuario), conn);
            pagina.MenuPrincipal(conn, Integer.parseInt(miUsuario));
            pagina.setVisible(true);

            // Timer de 30 minutos para cerrar sesión y regresar al login
            Timer timer = new Timer(30 * 60 * 1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pagina.dispose(); // Cierra la ventana principal
                    login nuevoLogin = new login();
                    String usuarioNuevo = "", contrasenaNueva = "";
                    if (nuevoLogin.mostrarDialogo()) { // <-- usar mostrarDialogo
                        usuarioNuevo = nuevoLogin.getUsuario();
                        contrasenaNueva = nuevoLogin.getContrasena();
                    }
                    // Opcional: volver a abrir paginaPrincipal si login correcto
                    if (inicioDeSesion.verificarContraseña(Integer.parseInt(usuarioNuevo), contrasenaNueva, conn)) {
                        paginaPrincipal nuevaPagina = new paginaPrincipal();
                        controlSesiones.registrarSesion(Integer.parseInt(usuarioNuevo), conn);
                        nuevaPagina.MenuPrincipal(conn, Integer.parseInt(usuarioNuevo));
                        nuevaPagina.setVisible(true);
                    } else {
                        errorDeUsuario error = new errorDeUsuario();
                        error.errorLogin();
                        error.setVisible(true);
                    }
                }
            });
            timer.setRepeats(false); // Solo se ejecuta una vez
            timer.start();

        } else {
            errorDeUsuario error = new errorDeUsuario();
            error.errorLogin();
            error.setVisible(true);
        }
    }
}
