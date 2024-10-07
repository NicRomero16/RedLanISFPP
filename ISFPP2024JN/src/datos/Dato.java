package datos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;
import java.lang.String;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;

public class Dato {


	public static TreeMap<String, TipoPuerto> cargarTipoPuerto(String archivoTipoPuerto) throws IOException {
		CargarParametros.parametros();
		TreeMap<String, TipoPuerto> tipoPuerto = new TreeMap<String, TipoPuerto>();

		try (BufferedReader br = new BufferedReader(new FileReader(archivoTipoPuerto))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
			//	String[] atributos1 = atributos[3].split(",");
				tipoPuerto.put(atributos[0], new TipoPuerto(atributos[0], atributos[1],Integer.parseInt(atributos[2])));
			}
		} catch (Exception ex) {
			System.out.println("Error al leer el archivo " + archivoTipoPuerto);
		}

		return tipoPuerto;
	}

	public static TreeMap<String, TipoEquipo> cargarTipoEquipo(String archivoTipoPuerto) throws IOException {
		CargarParametros.parametros();
		TreeMap<String, TipoEquipo> tipoEquipo = new TreeMap<String, TipoEquipo>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(archivoTipoPuerto))){
			String linea;
			while ((linea = br.readLine()) != null) {
				String [] atributos = linea.split(";");
				tipoEquipo.put(atributos[0],new TipoEquipo(atributos[0],atributos[1]));
			}
		}
		catch (Exception ex) {
			System.out.println("Error al leer el archivo" + archivoTipoPuerto);
		}

		return tipoEquipo;
	}

	public static TreeMap<String, TipoCable> cargarTipoCable(String archivoTipoCable) throws FileNotFoundException {

		Scanner read;
		TreeMap<String, TipoCable> tipoCable = new TreeMap<String, TipoCable>();

		read = new Scanner(new File(archivoTipoCable));
		read.useDelimiter("\\s*;\\s*");

		String codigo, descripcion, velocidad;
		while (read.hasNext()) {
			codigo = read.next();
			descripcion = read.next();
			velocidad = read.next();
			tipoCable.put(codigo, new TipoCable(codigo, descripcion, Integer.parseInt(velocidad)));
		}
		read.close();

		return tipoCable;
	}

	public static TreeMap<String, Ubicacion> cargarUbicacion(String archivoUbicacion) throws FileNotFoundException {

		Scanner read;
		TreeMap<String, Ubicacion> ubicacion = new TreeMap<String, Ubicacion>();

		read = new Scanner(new File(archivoUbicacion));
		read.useDelimiter("\\s*;\\s*");

		String codigo, descripcion;
		while (read.hasNext()) {
			codigo = read.next();
			descripcion = read.next();
			ubicacion.put(codigo, new Ubicacion(codigo, descripcion));
		}
		read.close();

		return ubicacion;
	}

	public static TreeMap<String, Equipo> cargarEquipo(String archivoEquipo) throws FileNotFoundException {

		Scanner read;
		TreeMap<String, Equipo> equipo = new TreeMap<String, Equipo>();

		read = new Scanner(new File(archivoEquipo));
		read.useDelimiter("\\s*;\\s*");

		String codigo, descripcion, marca, modelo, ubicacion;
		String[] tipoEquipo;
		String[] tipoPuerto;
		while (read.hasNext()) {
			codigo = read.next();
			descripcion = read.next();
			marca = read.next();
			modelo = read.next();

		}
		read.close();
		return equipo;
	}

	/*
	 * public static TreeMap<String, Conexion> cargarConexion(String
	 * archivoConexion) throws FileNotFoundException {
	 * 
	 * Scanner read; TreeMap<String, Conexion> conexion = new TreeMap<String,
	 * Conexion>();
	 * 
	 * read = new Scanner(new File(archivoConexion));
	 * read.useDelimiter("\\s*;\\s*");
	 * 
	 * String equipoA, equipoB,tipoCable0; while (read.hasNext()) { equipoA =
	 * read.next(); equipoB = read.next(); tipoCable0 = read.next();
	 * 
	 * Equipo equipo1 = equipoA; Equipo equipo2 = equipoB; TipoCable tipoCable = new
	 * TipoCable(tipoCable0);
	 * 
	 * conexion.put(tipoCable, new Conexion(equipo1, equipo2,tipoCable)); }
	 * read.close(); return conexion; }
	 */
}
