package dao;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import dao.secuencial.TipoEquipoSecuencialDAO;
import dao.secuencial.TipoPuertoSecuencialDAO;
import dao.postgres.TipoEquipoPostgresDAO;
import dao.postgres.TipoPuertoPostgresDAO;
import dao.secuencial.UbicacionSecuencialDAO;
import dao.postgres.UbicacionPostgresDAO;
import dao.secuencial.TipoCableSecuencialDAO;
import dao.postgres.TipoCablePostgresDAO;
import dao.EquipoDAO;
import dao.secuencial.EquipoSecuencialDAO;
import dao.postgres.EquipoPostgresDAO;
import dao.ConexionDAO;
import dao.postgres.ConexionPostgresDAO;
import dao.secuencial.ConexionSecuencialDAO;

// **********************************************
// Asegúrate de que las interfaces TipoEquipoDAO y UbicacionDAO estén importadas
// **********************************************

public class FactoryDAO {
    
    // Constante para la clave y el archivo
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static final String IMPLEMENTACION;
    
    // 🛠️ BLOQUE ESTÁTICO: Carga la configuración desde el sistema de archivos (raíz del proyecto)
    static {
        Properties props = new Properties();
        String impl = "";
        boolean loaded = false;
        // 1) Classpath
        try (InputStream in = FactoryDAO.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            if (in != null) {
                props.load(in);
                loaded = true;
            }
        } catch (IOException ignore) {}
        // 2) Archivo en disco
        if (!loaded) {
            try (FileReader reader = new FileReader(CONFIG_FILE_PATH)) {
                props.load(reader);
                loaded = true;
            } catch (IOException ignore) {}
        }
        if (loaded) {
            impl = props.getProperty("dao.implementacion");
        } else {
            System.err.println("ERROR: No se pudo cargar config.properties ni desde classpath ni desde disco. Usando SECUENCIAL como fallback.");
            impl = "SECUENCIAL"; // Valor de fallback
        }
        // El valor final se almacena en mayúsculas para simplificar las comparaciones
        IMPLEMENTACION = impl != null ? impl.toUpperCase().trim() : "SECUENCIAL";
        System.out.println("Configuración de persistencia cargada: " + IMPLEMENTACION);
    }
        
    // Método Factory para TipoEquipoDAO
    public static TipoEquipoDAO getTipoEquipoDAO() {
        // Ahora usamos .equals() ya que convertimos a mayúsculas en el static block
        if ("SECUENCIAL".equals(IMPLEMENTACION)) { 
            return new TipoEquipoSecuencialDAO();
        } else if ("POSTGRES".equals(IMPLEMENTACION)) {
            return new TipoEquipoPostgresDAO();
        }
        throw new RuntimeException("Implementación DAO de TipoEquipo no válida: " + IMPLEMENTACION);
    }
    
    // Método Factory para UbicacionDAO
    public static UbicacionDAO getUbicacionDAO() {
        if ("SECUENCIAL".equals(IMPLEMENTACION)) {
            return new UbicacionSecuencialDAO();
        } else if ("POSTGRES".equals(IMPLEMENTACION)) {
            return new UbicacionPostgresDAO();
        }
        throw new RuntimeException("Implementación DAO de Ubicacion no válida: " + IMPLEMENTACION);
    }
    
    public static TipoPuertoDAO getTipoPuertoDAO() {
        if ("SECUENCIAL".equals(IMPLEMENTACION)) {
            return new TipoPuertoSecuencialDAO();
        } else if ("POSTGRES".equals(IMPLEMENTACION)) {
            return new TipoPuertoPostgresDAO();
        }
        throw new RuntimeException("Implementación DAO de TipoPuerto no válida: " + IMPLEMENTACION);
    }
    
    public static TipoCableDAO getTipoCableDAO() {
        if ("SECUENCIAL".equals(IMPLEMENTACION)) {
            return new TipoCableSecuencialDAO();
        } else if ("POSTGRES".equals(IMPLEMENTACION)) {
            return new TipoCablePostgresDAO();
        }
        throw new RuntimeException("Implementación DAO de TipoCable no válida: " + IMPLEMENTACION);
    }

    public static EquipoDAO getEquipoDAO() {
        if ("SECUENCIAL".equals(IMPLEMENTACION)) {
            return new EquipoSecuencialDAO();
        } else if ("POSTGRES".equals(IMPLEMENTACION)) {
            return new EquipoPostgresDAO();
        }
        throw new RuntimeException("Implementación DAO de Equipo no válida: " + IMPLEMENTACION);
    }

    public static ConexionDAO getConexionDAO() {
        if ("SECUENCIAL".equals(IMPLEMENTACION)) {
            return new ConexionSecuencialDAO();
        } else if ("POSTGRES".equals(IMPLEMENTACION)) {
            return new ConexionPostgresDAO();
        }
        throw new RuntimeException("Implementación DAO de Conexion no válida: " + IMPLEMENTACION);
    }
}