package aplicacion;

import java.io.IOException;

import java.util.List;
import java.util.TreeMap;

import controlador.Coordinador;
import negocio.Calculo;
import dao.*;
import dao.secuencial.*;
import modelo.*;

public class TestCompilacion {

	public static void main(String[] args) throws IOException {

		TipoEquipoSecuencialDAO dao = new TipoEquipoSecuencialDAO();
		TipoCableSecuencialDAO dao1 = new TipoCableSecuencialDAO();
		TipoPuertoSecuencialDAO dao2 = new TipoPuertoSecuencialDAO();
		UbicacionSecuencialDAO dao3 = new UbicacionSecuencialDAO();
		ConexionSecuencialDAO dao4 = new ConexionSecuencialDAO();
		EquipoSecuencialDAO dao5 = new EquipoSecuencialDAO();

//		TreeMap<String, TipoEquipo> tipoEquipo = dao.buscarTodos();
//		System.out.println(tipoEquipo);
//		System.out.println();
//		TreeMap<String, TipoCable> tipoCable = dao1.buscarTodos();
//		System.out.println(tipoCable);
//		System.out.println();
//		TreeMap<String, TipoPuerto> tipoPuerto = dao2.buscarTodos();
//		System.out.println(tipoPuerto);
//		System.out.println();
//		TreeMap<String, Ubicacion> ubicacion = dao3.buscarTodos();
//		System.out.println(ubicacion);
//		System.out.println();
		List<Conexion> conexion = dao4.buscarTodos();
		System.out.println(conexion);
		//TreeMap<String, Equipo> equipo = dao5.buscarTodos();
		//System.out.println(equipo);
		TreeMap<String, TipoEquipo> tipoEquipos = dao.buscarTodos();
		TreeMap<String, TipoCable> tipoCables = dao1.buscarTodos();
		TreeMap<String, TipoPuerto> tipoPuertos = dao2.buscarTodos();
		TreeMap<String, Ubicacion> ubicaciones = dao3.buscarTodos();
		List<Conexion> conexiones = dao4.buscarTodos();
		TreeMap<String, Equipo> equipos = dao5.buscarTodos();

		System.out.println("Equipos cargados:");
		for (Equipo equipo : equipos.values()) {
			System.out.println(equipo);
		}
		
		System.out.println();

		System.out.println("Conexiones cargadas:");
		for (Conexion conexion1 : conexiones) {
			System.out.println(conexion1);
		}
		
		System.out.println();

		System.out.println("Tipos de equipos cargados:");
		for (TipoEquipo tipoEquipo : tipoEquipos.values()) {
			System.out.println(tipoEquipo);
		}
		
		System.out.println();
		
		System.out.println("Tipos de cables cargados:");
		for (TipoCable cables : tipoCables.values()) {
			System.out.println(cables);
		}

		System.out.println();
		
		System.out.println("Tipos de puertos cargados:");
		for (TipoPuerto puertos : tipoPuertos.values()) {
			System.out.println(puertos);
		}

		System.out.println();
		
		System.out.println("Ubicaciones cargadas:");
		for (Ubicacion ubi : ubicaciones.values()) {
			System.out.println(ubi);
		}
	}

}
