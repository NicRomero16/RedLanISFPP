package controlador;

import java.util.TreeMap;

import interfaz.Interfaz;
import modelo.Equipo;
import negocio.Calculo;
import negocio.ConexionInexistenteException;
import negocio.Empresa;
import negocio.EquipoExistenteException;

public class AplicacionConsultas {

	// l�gica
	private Empresa empresa;
	private Calculo calculo;

	// vista
	private Interfaz interfaz;

	// controlador
	private Coordinador coordinador;

	public static void main(String[] args) throws EquipoExistenteException, ConexionInexistenteException {
		AplicacionConsultas miAplicacion = new AplicacionConsultas();
		miAplicacion.iniciar();
		miAplicacion.consultar1(); 
		miAplicacion.consultar2();
		miAplicacion.consultar3();
		miAplicacion.consultar4();
		miAplicacion.consultar5();
	}

	private void iniciar() throws EquipoExistenteException, ConexionInexistenteException {
		/* Se instancian las clases */
		empresa = Empresa.getEmpresa();
		calculo = new Calculo();
		coordinador = new Coordinador();
		interfaz = new Interfaz();
		/* Se establecen las relaciones entre clases */
		calculo.setCoordinador(coordinador);
		interfaz.setCoordinador(coordinador);
		/* Se establecen relaciones con la clase coordinador */
		coordinador.setEmpresa(empresa);
		coordinador.setCalculo(calculo);
		coordinador.setInterfaz(interfaz);
		calculo.cargarDatos(coordinador.listarConexiones());
	}

	// Imprimir el grafo en pantalla
	private void consultar1() {
		coordinador.imprimirGrafo();
	}

	// Consultar la velocidad maxima dados 2 equipos
	private void consultar2() throws EquipoExistenteException, ConexionInexistenteException {
		Equipo origen = interfaz.ingresarEquipoOrigen(coordinador.listarEquipos());
		Equipo destino = interfaz.ingresarEquipoDestino(coordinador.listarEquipos());
		coordinador.velocidadMax(origen, destino);
	}

	// Mostrar equipos intermedios entre dos equipos conectados
	private void consultar3() {
		Equipo origen = interfaz.ingresarEquipoOrigen(coordinador.listarEquipos());
		Equipo destino = interfaz.ingresarEquipoDestino(coordinador.listarEquipos());
		coordinador.mostrarEquiposIntermedios(origen, destino);

	}

	// Consultar ping
	private void consultar4() {
		String ip = interfaz.ingresarIP();
		coordinador.ping(ip);
	}
	
	// Ver estado de los equipos conectados a la red
	private void consultar5(){
		TreeMap<String,Equipo> map= interfaz.recibirMapEquipos();
		coordinador.estadoEquipos(map);
	}
}
