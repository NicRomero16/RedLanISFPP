package dao.postgres; 

import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {

    public static void main(String[] args) {
        System.out.println("Intentando establecer la conexión con PostgreSQL...");

        // try-with-resources se encarga de cerrar la conexión automáticamente
        try (Connection connection = ConexionPostgres.getConnection()) {

            System.out.println("✅ ¡Conexión exitosa a la base de datos RedLan!");

            // Mostrar metadatos de la conexión
            System.out.println("Base de datos: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("Versión: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Usuario: " + connection.getMetaData().getUserName());
            System.out.println("URL: " + connection.getMetaData().getURL());

        } catch (SQLException e) {
            System.out.println("❌ ERROR: No se pudo establecer la conexión.");
            System.err.println("Mensaje de error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
