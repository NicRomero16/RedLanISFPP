package datos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import modelo.Computadoras;
import modelo.Conexiones;
import modelo.Maquina;
import modelo.Routers;

public class Datos {

	private static TreeMap<String, Maquina> cargarRouters(String archivoRouter) throws FileNotFoundException {
		Scanner read;
		TreeMap<String, Maquina> maquina = new TreeMap<String, Maquina>();
		read = new Scanner(new File(archivoRouter));
		read.useDelimiter("\\s*;\\s*"); 
		String id, ipAddress, macAddress,status, modelo, firmware, ubicacion, throughput;
		while (read.hasNext()) {
			id = read.next();
			ipAddress = read.next();
			macAddress = read.next();
			modelo = read.next();
			firmware = read.next();
			status = read.next();
			throughput = read.next();
			ubicacion = read.next();
			maquina.put(id, new Routers(id, ipAddress, macAddress, Boolean.parseBoolean(status), modelo, firmware, ubicacion, Integer.parseInt(throughput)));	
		}
		
		read.close();

		return maquina;
	}
	
	private static TreeMap<String, Maquina> cargarComputadoras(String archivoCompu) throws FileNotFoundException {
		Scanner read;
		TreeMap<String, Maquina> maquina = new TreeMap<String, Maquina>();

		read = new Scanner(new File(archivoCompu));
		read.useDelimiter("\\s*;\\s*");
		String id, ipAddress, macAddress, status, ubicacion;
		
		while (read.hasNext()) {
			id = read.next();
			ipAddress = read.next();
			macAddress = read.next();
			status = read.next();
			ubicacion = read.next();
			maquina.put(id, new Computadoras(id, ipAddress, macAddress, Boolean.parseBoolean(status), ubicacion));
			
		}
		read.close();

		return maquina;
	}
	
	public static TreeMap<String, Maquina> cargarMaquinas(String archivoCompu, String archivoRouter) throws FileNotFoundException {
		TreeMap<String, Maquina> maquina = new TreeMap<>();

		maquina.putAll(Datos.cargarRouters(archivoRouter));
		maquina.putAll(Datos.cargarComputadoras(archivoCompu));

		return maquina;
	}
	
	public static List<Conexiones> cargarConexiones(String archivoConexiones, TreeMap<String, Maquina> equipos) throws FileNotFoundException {
		
		Scanner read;
		List<Conexiones> conexiones = new ArrayList<Conexiones>();
		read = new Scanner(new File(archivoConexiones));
		read.useDelimiter("\\s*;\\s*");
		Maquina m1, m2;
		String idCompu, idRouter, tipoConexion, bandwidth, latencia, status, errorRate;
		
		while (read.hasNext()) {
			idCompu = read.next();
			idRouter = read.next();
			
			if (equipos.containsKey(idCompu) && equipos.containsKey(idRouter)) {
				m1 = equipos.get(idCompu);
				m2 = equipos.get(idRouter);
				tipoConexion = read.next();
				bandwidth = read.next();
				latencia = read.next();
				status = read.next();
				errorRate = read.next();
				conexiones.add(new Conexiones(m1, m2, tipoConexion, Integer.parseInt(bandwidth), Integer.parseInt(latencia), Boolean.parseBoolean(status), Double.parseDouble(errorRate)));
				
			} else {
				System.err.println("ERROR: Las IDs de las Maquinas en la conexión no existen en el mapa de maquina.");

			}
		}
		read.close();

		return conexiones;
	}
}
