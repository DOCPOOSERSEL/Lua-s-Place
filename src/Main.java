import Datos.accesoBaseDeDatos;
import Funciones.controlSesiones;
import Interfaz.login;
import Funciones.inicioDeSesion;
import Interfaz.errorDeUsuario;
import Interfaz.paginaPrincipal;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Lo meto a aqui donde hace la conexion a la base de datos usando el admin q es Lua777
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

        } else {
            errorDeUsuario error = new errorDeUsuario();
            error.errorLogin();
            error.setVisible(true);
        }

    }
}