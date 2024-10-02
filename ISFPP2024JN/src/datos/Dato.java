package datos;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;
import java.io.File;
import modelo.Conexion;

public class Dato {

	// ArchivoTexto = ruta del archivo
	public static TreeMap<String, Conexion> cargarConexiones(String archivoConexion) throws FileNotFoundException {

		Scanner read;
		TreeMap<String, Conexion> conexion = new TreeMap<String, Conexion>();

		read = new Scanner(new File(archivoConexion));
		read.useDelimiter("\\s*;\\s*");
		
		
		
		return conexion;
	}
	
	public static TreeMap<String, Conexion> cargarEquipo(String archivoConexion) throws FileNotFoundException {

		Scanner read;
		TreeMap<String, Conexion> conexion = new TreeMap<String, Conexion>();

		read = new Scanner(new File(archivoConexion));
		read.useDelimiter("\\s*;\\s*");
		
		
		
		return conexion;
	}
	
	public static TreeMap<String, Conexion> cargarRed(String archivoConexion) throws FileNotFoundException {

		Scanner read;
		TreeMap<String, Conexion> conexion = new TreeMap<String, Conexion>();

		read = new Scanner(new File(archivoConexion));
		read.useDelimiter("\\s*;\\s*");
		
		
		
		return conexion;
	}
	
	public static TreeMap<String, Conexion> cargarUbicacion(String archivoConexion) throws FileNotFoundException {

		Scanner read;
		TreeMap<String, Conexion> conexion = new TreeMap<String, Conexion>();

		read = new Scanner(new File(archivoConexion));
		read.useDelimiter("\\s*;\\s*");
		
		
		
		return conexion;
	}
}
