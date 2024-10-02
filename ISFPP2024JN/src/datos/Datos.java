package datos;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import red.Conexion;

public class Datos {

	private static TreeMap<String, Conexion> cargarConexiones(String archivoConexion) throws FileNotFoundException {

		Scanner read;
		TreeMap<String, Conexion> conexion = new TreeMap<String, Conexion>();

		read = new Scanner(new File(archivoConexion));
     read.useDelimiter("\\s*;\\s*");
     
	}
}
