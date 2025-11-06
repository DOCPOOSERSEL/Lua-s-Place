import Datos.accesoBaseDeDatos;
import Interfaz.Login;

public class Main {
    public static void main(String[] args) {
        Login login = new Login();
        String miUsuario = "", miContrasena = "";
        if (login.mostrarDialogo()) {
            miUsuario = login.getUsuario();
            miContrasena = login.getContrasena();

            System.out.println("Usuario: " + miUsuario);
            System.out.println("Contraseña: " + miContrasena);
        }
        // Lo meto a aqui donde hace la conexion a la base de datos usando el admin q es Lua777
        accesoBaseDeDatos.getConnection(miUsuario, miContrasena);
    }
}