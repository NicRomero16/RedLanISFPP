package datos;

import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

public class CargarParametros {

	private static String archivoConexion;
	private static String archivoEquipo;
	private static String archivoTipoCable;
	private static String archivoTipoEquipo;
	private static String archivoTipoPuerto;
	private static String archivoUbicacion;

	public static void parametros() throws IOException { // preguntar si tenemos q hacer nuestros propias excepciones

		Properties prop = new Properties();
		InputStream input = new FileInputStream("config.properties");

		prop.load(input);

		archivoConexion = prop.getProperty("conexion");
		archivoEquipo = prop.getProperty("equipo");
		archivoTipoCable = prop.getProperty("tipoCable");
		archivoTipoEquipo = prop.getProperty("tipoEquipo");
		archivoTipoPuerto = prop.getProperty("tipoPuerto");
		archivoUbicacion = prop.getProperty("ubicacion");
	}

	public static String getArchivoConexion() {
		return archivoConexion;
	}

	public static String getArchivoEquipo() {
		return archivoEquipo;
	}

	public static String getArchivoTipoCable() {
		return archivoTipoCable;
	}

	public static String getArchivoTipoEquipo() {
		return archivoTipoEquipo;
	}

	public static String getArchivoTipoPuerto() {
		return archivoTipoPuerto;
	}

	public static String getArchivoUbicacion() {
		return archivoUbicacion;
	}

}
