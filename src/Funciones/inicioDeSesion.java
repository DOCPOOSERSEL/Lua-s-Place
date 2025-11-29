package Funciones;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;

public class inicioDeSesion {


    public static String cifrar(String password) {
        try {
            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 60000, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            byte[] combo = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, combo, 0, salt.length);
            System.arraycopy(hash, 0, combo, salt.length, hash.length);

            return Base64.getEncoder().encodeToString(combo);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean verificarHash(String password, String hashBase64) {
        try {
            byte[] combo = Base64.getDecoder().decode(hashBase64);
            byte[] salt = Arrays.copyOfRange(combo, 0, 16);
            byte[] hashBD = Arrays.copyOfRange(combo, 16, combo.length);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 60000, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] nuevoHash = skf.generateSecret(spec).getEncoded();

            return Arrays.equals(hashBD, nuevoHash);

        } catch (Exception e) {
            return false;
        }
    }


    public static boolean verificarContraseña(int idEmp, String contraseñaIngresada, Connection conn) {
        String sql = "SELECT contra_emp FROM empleado WHERE id_emp = ?";
        String hashBD = "";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmp);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return false;
            }

            hashBD = rs.getString("contra_emp");

        } catch (SQLException e) {
            return false;
        }

        return verificarHash(contraseñaIngresada, hashBD);
    }


    public static int obtenerIdPorContrasena(String contrasena, Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT id_emp, contra_emp FROM empleado");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (verificarHash(contrasena, rs.getString("contra_emp"))) {
                    return rs.getInt("id_emp");
                }
            }

        } catch (Exception e) { }
        return -1;
    }

    public static void cifrarTodasLasContras(Connection conn) {
        String sqlS = "SELECT id_emp, contra_emp FROM empleado";
        String sqlU = "UPDATE empleado SET contra_emp=? WHERE id_emp=?";

        try (
                PreparedStatement psS = conn.prepareStatement(sqlS);
                ResultSet rs = psS.executeQuery();
                PreparedStatement psU = conn.prepareStatement(sqlU)
        ) {

            while (rs.next()) {
                int id = rs.getInt("id_emp");
                String pass = rs.getString("contra_emp");

                if (pass.length() > 40) continue;  // ya es hash

                String nuevo = cifrar(pass);

                psU.setString(1, nuevo);
                psU.setInt(2, id);
                psU.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
