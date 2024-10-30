package interfaz;

import java.util.Set;
import java.util.TreeMap;

import controlador.Constantes;
import controlador.Coordinador;
import excepciones.EquipoInexistenteException;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;

public class Interfaz {

	private Coordinador coordinador;

	public Interfaz() {

	}

	public TreeMap<String, Equipo> recibirMapEquipos() {
		return coordinador.listarEquipos();
	}

	public String ingresarIP() {
		return Constantes.PING;
	}

	public Equipo eliminarEquipo() {
		TreeMap<String, Equipo> equipos = recibirMapEquipos();
		Equipo equipo = equipos.get(Constantes.ELIMINAR_EQUIPO);
		return equipo;
	}

	public void buscarEquipo() {
		Equipo equipo = coordinador.buscarEquipo(Constantes.BUSCAR_EQUIPO);
		System.out.println(equipo);
	}

	public Equipo agregarNuevoEquipo() {
		Equipo equipo = new Equipo("adf", "asf", null, null, new TipoEquipo("asf", "jota"),
				new Ubicacion("A01", "Aula 1"), false);
		equipo.agregarDireccionIP("123");
		equipo.agregarPuerto(40, new TipoPuerto("cod","codeado",10));
		return equipo;
	}

	public String EquipoIpOrigen(String ipOrigen) {
		return "192.168.16.115";
	}

	public String EquipoIpDestino(String ipDestino) {
		return "192.168.16.120";
	}

	public Equipo ingresarEquipoOrigen(TreeMap<String, Equipo> equipos) {
		return equipos.get("AP01");
	}

	public Equipo ingresarEquipoDestino(TreeMap<String, Equipo> equipos) {
		return equipos.get("SWL0");
	}

	public Equipo[] solicitarEquipos() {
		Equipo origen = ingresarEquipoOrigen(coordinador.listarEquipos());
		if (origen == null)
			throw new EquipoInexistenteException("El equipo origen no existe");
		Equipo destino = ingresarEquipoDestino(coordinador.listarEquipos());
		if (destino == null)
			throw new EquipoInexistenteException("El equipo destino no existe");
		return new Equipo[] { origen, destino };
	}

	public void imprimirGrafo(Set<Conexion> edges) {
		StringBuilder resultado = new StringBuilder();

		for (Conexion edge : edges) {
			resultado.append("Conexion:  \n");

			resultado.append("Equipo origen: \n Codigo: " + edge.getEquipo1().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo1().getDescripcion() + "\n Marca: " + edge.getEquipo1().getMarca() + "\n Modelo: "
					+ edge.getEquipo1().getModelo() + "\n " + edge.getEquipo1().getTipoEquipo() + "\n "
					+ edge.getEquipo1().getUbicacion() + "\n " + edge.getEquipo1().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo1().getDireccionesIP() + "\n Estado: " + edge.getEquipo1().getEstado() + "\n "
					+ edge.getTipoPuerto1() + "\n");
			resultado.append("\nEquipo destino: \n Codigo: " + edge.getEquipo2().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo2().getDescripcion() + "\n Marca: " + edge.getEquipo2().getMarca() + "\n Modelo: "
					+ edge.getEquipo2().getModelo() + "\n " + edge.getEquipo2().getTipoEquipo() + "\n "
					+ edge.getEquipo2().getUbicacion() + "\n " + edge.getEquipo2().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo2().getDireccionesIP() + "\n Estado: " + edge.getEquipo2().getEstado() + "\n "
					+ edge.getTipoPuerto2() + "\n");
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

	public Equipo modificarNuevoEquipo() {
		Equipo equipo = new Equipo("REQW", "eLjota", null, null, new TipoEquipo("RE", "jota"),
				new Ubicacion("A01", "Aula 1"), false);
		return equipo;
	}
}
