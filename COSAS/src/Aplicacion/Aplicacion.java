package Aplicacion;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import datos.CargarParametros;
import datos.Datos;
import logica.Logica;
import modelo.Conexiones;
import modelo.Maquina;

public class Aplicacion {
	public static void main(String[] args) throws FileNotFoundException {
		
		// Carga de parametros
		try {
			CargarParametros.parametros();
			
		} catch (IOException e) {
			System.err.print("Error al cargar parámetros");
			System.exit(-1);
		}
		
		// Cargo los datos
		TreeMap<String, Maquina> maquina = null;
		List<Conexiones> conexiones = null;

		try {
			maquina = Datos.cargarMaquinas(CargarParametros.getArchivoCompu(), CargarParametros.getArchivoRouter());
			conexiones = Datos.cargarConexiones(CargarParametros.getArchivoConex(), maquina);
			
		} catch (FileNotFoundException e) {
			System.err.print("Error al cargar archivos de datos");
			System.exit(-1);
		}
		
		for (String id : maquina.keySet()) {
			System.out.println("nodo ID: " + id);
		}
		
		for (Conexiones conex : conexiones) {
			System.out.println("Conexion de " + conex.getSourceNode().getId() + " a " + conex.getTargetNode().getId());
		}
		
		System.out.println();

		try {
			System.out.println("CARGANDO GRAFO");
			Logica l = new Logica(maquina, conexiones);
			System.out.println("GRAFO CARGADO");
			
		} catch (Exception e) {
			
			System.err.println("ERROR al cargar el grafo: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
