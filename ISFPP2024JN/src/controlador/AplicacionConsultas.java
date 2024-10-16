package controlador;

import java.util.List;

import interfaz.Interfaz;
import modelo.Conexion;
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
		miAplicacion.consultar1();// Consulta 3.1
		miAplicacion.consultar2();// Consulta 3.2
		// miAplicacion.consultar3();// Consulta 3.3
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
		coordinador.imprimirGrafo();
	}

	private void consultar1() throws EquipoExistenteException, ConexionInexistenteException {

		// ingreso datos usuario
		// fijarse en la clase interfaz los metodos, hay q hacerlo
		Equipo origen = interfaz.ingresarEquipoOrigen(coordinador.listarEquipos());
		Equipo destino = interfaz.ingresarEquipoDestino(coordinador.listarEquipos());

		coordinador.velocidadMax(origen, destino);
	}

	// 3.2 Realizar un ping a un equipo.
//	Realizar un ping a un rango de IP. 
//	Realizar un mapa del estado actual de los equipos conectados a la red.
	private void consultar2() {
		
		Equipo origen = interfaz.ingresarEquipoOrigen(coordinador.listarEquipos());
		Equipo destino = interfaz.ingresarEquipoDestino(coordinador.listarEquipos());
		coordinador.mostrarEquipos(origen, destino);
		
	}

	private void consultar3() {

	}

}
