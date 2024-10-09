package aplicacion;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import dao.secuencial.*;
import modelo.*;

public class TestCompilacion {

	public static void main(String[] args) throws IOException {
		TipoEquipoSecuencialDAO dao = new TipoEquipoSecuencialDAO();
		TipoCableSecuencialDAO dao1 = new TipoCableSecuencialDAO();
		TipoPuertoSecuencialDAO dao2 = new TipoPuertoSecuencialDAO();
		UbicacionSecuencialDAO dao3 = new UbicacionSecuencialDAO();
		ConexionSecuencialDAO dao4 = new ConexionSecuencialDAO();
//		EquipoSecuencialDAO dao5 = new EquipoSecuencialDAO();

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
		System.out.println();
		//TreeMap<String, Equipo> equipo = dao5.buscarTodos();
		//System.out.println(equipo);

		/*
		 * private String codigo; private String descripcion; private String marca;
		 * private String modelo; private TipoEquipo tipoEquipo; private Ubicacion
		 * ubicacion; private List<Puerto> puertos; private List<String> direccionesIP;
		 * private boolean estado;
		 */
	}

}
