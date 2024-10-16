package aplicacion;

import dao.secuencial.*;
import dao.*;
import modelo.*;
import java.util.TreeMap;
import java.util.List;
import controlador.Coordinador;
import interfaz.Interfaz;
import negocio.Calculo;
import negocio.Empresa;
import negocio.EquipoExistenteException;

public class TestCompilacion {

	public static void main(String[] args) throws EquipoExistenteException {

		TipoEquipoSecuencialDAO dao = new TipoEquipoSecuencialDAO();
		TipoCableSecuencialDAO dao1 = new TipoCableSecuencialDAO();
		TipoPuertoSecuencialDAO dao2 = new TipoPuertoSecuencialDAO();
		UbicacionSecuencialDAO dao3 = new UbicacionSecuencialDAO();
		ConexionSecuencialDAO dao4 = new ConexionSecuencialDAO();
		EquipoSecuencialDAO dao5 = new EquipoSecuencialDAO();

		TreeMap<String, TipoEquipo> tipoEquipo = dao.buscarTodos();
		TreeMap<String, TipoCable> tipoCable = dao1.buscarTodos();
		TreeMap<String, TipoPuerto> tipoPuerto = dao2.buscarTodos();
		TreeMap<String, Ubicacion> ubicacion = dao3.buscarTodos();
		List<Conexion> conexiones = dao4.buscarTodos();
		TreeMap<String, Equipo> equipos = dao5.buscarTodos();
		/*
		 * System.out.println("Equipos cargados:"); for (Equipo e : equipos.values()) {
		 * System.out.println(e); }
		 * 
		 * System.out.println(); System.out.println("Conexiones cargadas:"); for
		 * (Conexion conexion1 : conexiones) { System.out.println(conexion1); }
		 * 
		 * System.out.println();
		 * 
		 * System.out.println("Tipos de equipos cargados:"); for (TipoEquipo te :
		 * tipoEquipos.values()) { System.out.println(te); }
		 * 
		 * System.out.println();
		 * 
		 * System.out.println("Tipos de cables cargados:"); for (TipoCable cables :
		 * tipoCables.values()) { System.out.println(cables); } System.out.println();
		 * 
		 * System.out.println("Tipos de puertos cargados:"); for (TipoPuerto puertos :
		 * tipoPuertos.values()) { System.out.println(puertos); }
		 * 
		 * System.out.println();
		 * 
		 * System.out.println("Ubicaciones cargadas:"); for (Ubicacion ubi :
		 * ubicaciones.values()) { System.out.println(ubi); }
		 */
		/*/ Coordinador coordinador = new Coordinador();
		// Empresa empresa = Empresa.getEmpresa();
		// coordinador.setEmpresa(empresa);
		// Calculo calculo = new Calculo();
		// coordinador.setCalculo(calculo);
		// coordinador.listarEquipos();
		 */
	}
}
