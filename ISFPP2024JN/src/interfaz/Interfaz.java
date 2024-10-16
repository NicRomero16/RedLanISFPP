package interfaz;

import java.util.Set;
import java.util.TreeMap;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;

public class Interfaz {
	// main->controlador->calculo->controlador->interfaz->consola
	private Coordinador coordinador;

	public Interfaz() {

	}

	public TreeMap<String, Equipo> recibirMapEquipos() {
		return coordinador.listarEquipos();
	}

	public String ingresarIP() {
		return "192.168.16.126";
	}

	public Equipo ingresarEquipoOrigen(TreeMap<String, Equipo> listarEquipos) {
		return listarEquipos.get("SWAM");
	}

	public Equipo ingresarEquipoDestino(TreeMap<String, Equipo> listarEquipos) {
		return listarEquipos.get("AP09");
	}

	public void imprimirGrafo(Set<Conexion> edges) {
		StringBuilder resultado = new StringBuilder();

		// Iterar sobre todas las aristas
		for (Conexion edge : edges) {
			resultado.append("Conexion:  \n");

			resultado.append("Equipo origen: \n Codigo: " + edge.getEquipo1().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo1().getDescripcion() + "\n Marca: " + edge.getEquipo1().getMarca() + "\n Modelo: "
					+ edge.getEquipo1().getModelo() + "\n " + edge.getEquipo1().getTipoEquipo() + "\n "
					+ edge.getEquipo1().getUbicacion() + "\n " + edge.getEquipo1().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo1().getDireccionesIP() + "\n " + edge.getTipoPuerto1() + "\n");
			resultado.append("\nEquipo destino: \n Codigo: " + edge.getEquipo2().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo2().getDescripcion() + "\n Marca: " + edge.getEquipo2().getMarca() + "\n Modelo: "
					+ edge.getEquipo2().getModelo() + "\n " + edge.getEquipo2().getTipoEquipo() + "\n "
					+ edge.getEquipo2().getUbicacion() + "\n " + edge.getEquipo2().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo2().getDireccionesIP() + "\n " + edge.getTipoPuerto2() + "\n");
			resultado.append(" \nTipo de cable de la conexion: \n " + edge.getTipoCable() + "\n");
			resultado.append(
					"=========================================================================================================");

		}
		System.out.println(resultado.toString());
	}

	public void mostrarEquiposIntermedios(String resultado) {
		System.out.println(resultado);
		System.out.println(
				"=========================================================================================================");
	}

	// imprime en pantalla la conexion entre dos equipos
	// junto con la velocidad que tiene de una a la otra(que seria el resultado)
	public void mostrarVelocidadMaxima(String resultado) {
		System.out.println(resultado);
		System.out.println(
				"=========================================================================================================");
	}

	public void imprimirEquipo(Equipo equipo) {
		System.out.println(equipo);
	}

	public void imprimirPing(String resultado) {
		System.out.println(resultado);
		System.out.println(
				"=========================================================================================================");
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

}
