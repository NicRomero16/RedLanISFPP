package dao.postgres;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
        boolean loaded = false;
        // 1) Intentar classpath (src/main/resources o raiz del jar)
        try (InputStream in = ConexionPostgres.class.getClassLoader().getResourceAsStream(PROPS_PATH)) {
            if (in != null) {
                p.load(in);
                loaded = true;
            }
        } catch (IOException ignore) {}

        // 2) Intentar archivo en disco (raíz del proyecto/ejecución)
        if (!loaded) {
            File f = new File(PROPS_PATH);
            if (f.exists()) {
                try (FileReader r = new FileReader(f)) {
                    p.load(r);
                    loaded = true;
                } catch (IOException ignore) {}
            }
        }

        if (loaded) {
            URL  = p.getProperty("db.url");
            USER = p.getProperty("db.user");
            PASS = p.getProperty("db.password");
        }

        if (!loaded || URL == null || USER == null || PASS == null) {
            System.err.println("No se pudo leer postgres.properties o faltan claves. Usando valores por defecto.");
            URL  = (URL  != null ? URL  : "jdbc:postgresql://localhost:5432/RedLan");
            USER = (USER != null ? USER : "postgres");
            PASS = (PASS != null ? PASS : "postgres");
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
