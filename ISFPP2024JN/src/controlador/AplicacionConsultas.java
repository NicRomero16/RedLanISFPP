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
		miAplicacion.consultar3();// Consulta 3.3
	}

	private void iniciar() throws EquipoExistenteException {
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
		calculo.cargarDatos(coordinador.listarConexiones());
	}

	private void consultar1() throws EquipoExistenteException, ConexionInexistenteException {

		// ingreso datos usuario
                              //fijarse en la clase interfaz los metodos, hay q hacerlo
		Equipo origen = interfaz.ingresarEquipoOrigen(coordinador.listarEquipos());// es un TreeMap CUIDADO
		Equipo destino = interfaz.ingresarEquipoDestino(coordinador.listarEquipos());// es un TreeMap CUIDADO

		// realiza el calculo
		calculo.cargarDatos(coordinador.listarConexiones());

		List<Conexion> recorrido = null;
		// hay que tener cuidado q Equipo es un TreeMap (origen, destino) y "recorrido"
		// es
		// una List, capaz tengamos que cambiarlo a lista si causa un error.
		// (CTRL+mover el cursor para ir a los metodos)
		recorrido = calculo.velocidadMaxima(origen, destino);

		interfaz.resultado1(recorrido);

	}

	private void consultar3() {
		// TODO Auto-generated method stub

	}

	private void consultar2() {
		// TODO Auto-generated method stub

	}
}
