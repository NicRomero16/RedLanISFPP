package datos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.lang.String;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;

public class CargaDatos {

	public static TreeMap<String, TipoPuerto> cargarTipoPuerto(String archivoTipoPuerto) throws IOException {
		CargarParametros.parametros();
		TreeMap<String, TipoPuerto> tipoPuerto = new TreeMap<String, TipoPuerto>();

		try (BufferedReader br = new BufferedReader(new FileReader(archivoTipoPuerto))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
				// String[] atributos1 = atributos[3].split(",");
				tipoPuerto.put(atributos[0],
						new TipoPuerto(atributos[0], atributos[1], Integer.parseInt(atributos[2])));
			}
		} catch (Exception ex) {
			System.out.println("Error al leer el archivo " + archivoTipoPuerto);
		}

		return tipoPuerto;
	}

	public static TreeMap<String, TipoEquipo> cargarTipoEquipo(String archivoTipoPuerto) throws IOException {
		CargarParametros.parametros();
		TreeMap<String, TipoEquipo> tipoEquipo = new TreeMap<String, TipoEquipo>();

		try (BufferedReader br = new BufferedReader(new FileReader(archivoTipoPuerto))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
				tipoEquipo.put(atributos[0], new TipoEquipo(atributos[0], atributos[1]));
			}
		} catch (Exception ex) {
			System.out.println("Error al leer el archivo" + archivoTipoPuerto);
		}

		return tipoEquipo;
	}

	public static TreeMap<String, TipoCable> cargarTipoCable(String archivoTipoCable) throws IOException {

		CargarParametros.parametros();
		TreeMap<String, TipoCable> tipoCable = new TreeMap<String, TipoCable>();

		try (BufferedReader br = new BufferedReader(new FileReader(archivoTipoCable))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
				tipoCable.put(atributos[0], new TipoCable(atributos[0], atributos[1], Integer.parseInt(atributos[2])));
			}
		} catch (Exception ex) {
			System.out.println("Error al leer el archivo" + archivoTipoCable);
		}

		return tipoCable;
	}

	public static TreeMap<String, Ubicacion> cargarUbicacion(String archivoUbicacion) throws IOException {
		CargarParametros.parametros();
		TreeMap<String, Ubicacion> ubicacion = new TreeMap<String, Ubicacion>();

		try (BufferedReader br = new BufferedReader(new FileReader(archivoUbicacion))) {
			String linea;
			while ((linea = br.readLine()) == null) {
				String[] atributos = linea.split(";");
				ubicacion.put(atributos[0], new Ubicacion(atributos[0], atributos[1]));
			}
		} catch (Exception ex) {
			System.out.println("Error al leer el archivo" + archivoUbicacion);
		}
		return ubicacion;
	}

	public static TreeMap<String, Equipo> cargarEquipo(String archivoEquipo, TreeMap<String, Ubicacion> ubicaciones,
			TreeMap<String, TipoEquipo> tipos) throws FileNotFoundException {

		Scanner read;
		TreeMap<String, Equipo> equipo = new TreeMap<String, Equipo>();

		read = new Scanner(new File(archivoEquipo));
		read.useDelimiter("\\s*;\\s*");

		String codigo, descripcion, marca, modelo;
		TipoEquipo tipoEquipo;
		Ubicacion ubicacion;
		boolean estado;
		while (read.hasNext()) {
			codigo = read.next();
			descripcion = read.next();
			marca = read.next();
			modelo = read.next();
			tipoEquipo = tipos.get(read.next());
			ubicacion = ubicaciones.get(read.next());
			estado = read.nextBoolean();
			
		}
		read.close();
		return equipo;
	}

	public static List<Conexion> cargarConexiones(String fileName, TreeMap<String, Equipo> equipos)
			throws FileNotFoundException {
		Scanner read;
		List<Conexion> conexiones = new ArrayList<Conexion>();

		read = new Scanner(new File(fileName));
		read.useDelimiter("\\s*;\\s*");
		Equipo e1, e2;
		String codigo, descripcion;
		int velocidad;
		while (read.hasNext()) {
			e1 = equipos.get(read.next());
			e2 = equipos.get(read.next());
			codigo = read.next();
			descripcion = read.next();
			velocidad = Integer.parseInt(read.next());
			TipoCable tc = new TipoCable(codigo, descripcion, velocidad);
			conexiones.add(0, new Conexion(e1, e2, tc));
		}
		read.close();

		return conexiones;
	}
}
