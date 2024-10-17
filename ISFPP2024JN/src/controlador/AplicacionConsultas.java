package controlador;

import java.util.TreeMap;

import excepciones.EquipoExistenteException;
import excepciones.EquipoInexistenteException;

import interfaz.Interfaz;
import modelo.Equipo;

import negocio.Calculo;
import negocio.Empresa;

public class AplicacionConsultas {

	// l�gica
	private Empresa empresa;
	private Calculo calculo;

	// vista
	private Interfaz interfaz;

	// controlador
	private Coordinador coordinador;

	public static void main(String[] args) throws EquipoExistenteException {
		AplicacionConsultas miAplicacion = new AplicacionConsultas();
		miAplicacion.iniciar();
		//miAplicacion.consultar1();
		//miAplicacion.consultar2();
		//miAplicacion.consultar3();
		//miAplicacion.consultar4();
		//miAplicacion.consultar5();
	}

	private void iniciar() throws EquipoExistenteException {
		/* Se instancian las clases */
		empresa = Empresa.getEmpresa();
		calculo = new Calculo();
		coordinador = new Coordinador();
		interfaz = new Interfaz();
		calculo.setCoordinador(coordinador);
		interfaz.setCoordinador(coordinador);
		coordinador.setEmpresa(empresa);
		coordinador.setCalculo(calculo);
		coordinador.setInterfaz(interfaz);
		calculo.cargarDatos(coordinador.listarConexiones());

	}

	// Imprimir el grafo en pantalla
	private void consultar1() {
		coordinador.imprimirGrafo();
	}

	// Metodo para mostrar los equipos intermedios entre dos equipos conectados
	private void consultar2() {
		Equipo[] equipos = interfaz.solicitarEquipos();
		try {
			coordinador.mostrarEquiposIntermedios(equipos[0], equipos[1]);
		} catch (EquipoInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Mostrar equipos intermedios entre dos equipos conectados
	private void consultar3() {
		Equipo[] equipos = interfaz.solicitarEquipos();
		coordinador.velocidadMaxima(equipos[0], equipos[1]);
	}

	// Mostrar el estado de un equipo
	private void consultar4() {
		String ip = interfaz.ingresarIP();
		coordinador.ping(ip);
	}

	// Ver estado de los equipos conectados a la red
	private void consultar5() {
		TreeMap<String, Equipo> map = interfaz.recibirMapEquipos();
		coordinador.estadoEquipos(map);
	}
	
	private void consultar6() {
	}
}
