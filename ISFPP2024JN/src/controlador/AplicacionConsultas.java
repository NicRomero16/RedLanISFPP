package controlador;

import java.util.TreeMap;

import gui.AplicacionGui;
import interfaz.Interfaz;
import modelo.Equipo;

import negocio.Calculo;
import negocio.Red;

public class AplicacionConsultas {

	private Red red;
	private Calculo calculo;
	private Interfaz interfaz;
	private Coordinador coordinador;
	private AplicacionGui appGui;

	public static void main(String[] args) {
		AplicacionConsultas miAplicacion = new AplicacionConsultas();

		miAplicacion.iniciar();
		// miAplicacion.consultar1();
		/*
		 * miAplicacion.consultar2(); miAplicacion.consultar3();
		 * miAplicacion.consultar4(); miAplicacion.consultar5();
		 * miAplicacion.estadoEquipos(); miAplicacion.agregarEquipo();
		 *
		 */
		// miAplicacion.buscarEquipo();
		 //miAplicacion.eliminarEquipo();
	}

	private void iniciar() {
		red = Red.getRed();
		calculo = new Calculo();
		coordinador = new Coordinador(red);
		interfaz = new Interfaz();
		calculo.setCoordinador(coordinador);
		interfaz.setCoordinador(coordinador);
		appGui = new AplicacionGui(coordinador);
		coordinador.setEmpresa(red);
		coordinador.setCalculo(calculo);
		coordinador.setInterfaz(interfaz);

		calculo.cargarDatos(coordinador.listarConexiones());
	}

	// Imprimir el grafo en pantalla
	private void consultar1() {
		coordinador.imprimirGrafo();
	}

	// Dado dos equipos mostrar todos los equipos intermedios y sus conexiones.
	private void consultar2() {
		Equipo[] equipos = interfaz.solicitarEquipos();
		coordinador.mostrarEquiposIntermedios(equipos[0], equipos[1]);

	}

	// Calcular la velocidad máxima de acuerdo al tipo de puerto y cables por donde
	// se transmiten los datos.
	private void consultar3() {
		Equipo[] equipos = interfaz.solicitarEquipos();
		coordinador.velocidadMaxima(equipos[0], equipos[1]);
	}

	// Realizar un ping a un equipo.
	private void consultar4() {
		String ip = interfaz.ingresarIP();
		coordinador.ping(ip);
	}

	// Realizar un ping a un rango de IP.
	private void consultar5() {

	}

	// Realizar un mapa del estado actual de los equipos conectados a la red.
	private void estadoEquipos() {
		TreeMap<String, Equipo> map = interfaz.recibirMapEquipos();
		coordinador.estadoEquipos(map);
	}

	// Agregar un equipo
	private void agregarEquipo() {
		Equipo equipo = interfaz.agregarNuevoEquipo();
		coordinador.agregarEquipo(equipo);
	}

	// Eliminar un equipo
	private void eliminarEquipo() {
		Equipo equipo = interfaz.eliminarEquipo();
		coordinador.eliminarEquipo(equipo);
	}

	// Buscar un equipo
	private void buscarEquipo() {
		interfaz.buscarEquipo();
	}
}
