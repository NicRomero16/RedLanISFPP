package datos;


import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;


public class CargarParametros {
    private static String archivoCompu;
    private static String archivoRouter;
    private static String archivoConex;

	public static void parametros() throws IOException {
		
		Properties prop = new Properties();		
			InputStream input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			// get the property value
			archivoCompu = prop.getProperty("computadoras");		
			archivoRouter = prop.getProperty("routers");
			archivoConex = prop.getProperty("conexiones");
	}

    public static String getArchivoCompu() {
        return archivoCompu;
    }

    public static String getArchivoRouter() {
        return archivoRouter;
    }

    public static String getArchivoConex() {
        return archivoConex;
    }
}
