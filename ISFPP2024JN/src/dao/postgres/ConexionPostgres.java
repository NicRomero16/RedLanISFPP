package dao.postgres;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionPostgres {

    private static final String PROPS_PATH = "postgres.properties";
    private static String URL;
    private static String USER;
    private static String PASS;

    static {
        Properties p = new Properties();
        try (FileReader r = new FileReader(PROPS_PATH)) {
            p.load(r);
            URL  = p.getProperty("db.url");
            USER = p.getProperty("db.user");
            PASS = p.getProperty("db.password");
            if (URL == null || USER == null || PASS == null) {
                throw new IllegalStateException("Faltan propiedades db.url/db.user/db.password en " + PROPS_PATH);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer " + PROPS_PATH + ". Usando valores por defecto.");
            URL  = "jdbc:postgresql://localhost:5432/RedLan";
            USER = "postgres";
            PASS = "postgres";
        }
    }

    /**
     * Devuelve un objeto Connection listo para ser usado.
     * El caller debe usar try-with-resources para cerrarlo automáticamente.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
