package negocio;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import controlador.Coordinador;
import excepciones.ConexionInexistenteException;
import excepciones.EquipoInexistenteException;
import modelo.Conexion;
import modelo.Equipo;

public class Calculo {

	private Coordinador coordinador;

	private Graph<Equipo, Conexion> redGrafo;
	private boolean actualizar;

	public Calculo() {
		redGrafo = new DefaultUndirectedGraph<>(Conexion.class);
	}

	public Graph<Equipo, Conexion> cargarDatos(List<Conexion> conexiones) {

		this.actualizar = false;
		redGrafo = new DefaultUndirectedWeightedGraph<>(Conexion.class);

		for (Conexion conexion : conexiones) {
			Equipo equipo1 = conexion.getEquipo1();
			Equipo equipo2 = conexion.getEquipo2();
			int velocidadEntre = conexion.getTipoCable().getVelocidad();

			redGrafo.addVertex(equipo1);
			redGrafo.addVertex(equipo2);
			redGrafo.addEdge(equipo1, equipo2, conexion);
			redGrafo.setEdgeWeight(conexion, velocidadEntre);

		}
		return redGrafo;
	}

	public List<Equipo> mostrarEquiposIntermedios(Equipo origen, Equipo destino) throws EquipoInexistenteException {
		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones());

		}
		List<Equipo> camino = DijkstraShortestPath.findPathBetween(redGrafo, origen, destino).getVertexList();

		if (camino.size() > 2) {
			List<Equipo> verticesIntermedios = camino.subList(1, camino.size() - 1);
			return verticesIntermedios;
		} else
			throw new EquipoInexistenteException("Los equipos no tienen equipos intermedios");
	}

	public String velocidadMaximaEntreEquipos(Equipo origen, Equipo destino) throws ConexionInexistenteException {
		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones());
		}
		DijkstraShortestPath<Equipo, Conexion> dijkstraAlg = new DijkstraShortestPath<>(redGrafo);
		GraphPath<Equipo, Conexion> path = dijkstraAlg.getPath(origen, destino);

		List<Conexion> recorrido = path.getEdgeList();
		if (recorrido.isEmpty())
			throw new ConexionInexistenteException(
					"No existe una conexion entre " + origen.getCodigo() + " y " + destino.getCodigo());

		double velocidadMaxima = 0;

		for (Conexion conexion : recorrido) {

			int velocidadPuerto1 = conexion.getTipoPuerto1().getVelocidad();
			int velocidadPuerto2 = conexion.getTipoPuerto2().getVelocidad();

			int velocidadPuertos = Math.min(velocidadPuerto1, velocidadPuerto2);

			int velocidadCable = conexion.getTipoCable().getVelocidad();

			int velocidadConexion = Math.min(velocidadPuertos, velocidadCable);

			velocidadMaxima = velocidadConexion;
		}

		return "Velocidad maxima entre " + origen.getCodigo() + " y " + destino.getCodigo() + " es de "
				+ velocidadMaxima;
	}

	public double velocidadMaxima(Equipo origen, Equipo destino) throws ConexionInexistenteException {
		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones()); // al avanzar el codigo tenemos q hacer el patron de
																// diseño observer
		}
		DijkstraShortestPath<Equipo, Conexion> dijkstraAlg = new DijkstraShortestPath<>(redGrafo);
		GraphPath<Equipo, Conexion> path = dijkstraAlg.getPath(origen, destino);

		if (path == null)
			throw new ConexionInexistenteException(
					"No existe una conexion entre los equipos " + origen + " y " + destino);

		List<Conexion> recorrido = path.getEdgeList();

		double velocidadMaxima = Double.MAX_VALUE;

		for (Conexion conexion : recorrido) {

			int velocidadPuerto1 = conexion.getTipoPuerto1().getVelocidad();
			int velocidadPuerto2 = conexion.getTipoPuerto2().getVelocidad();

			int velocidadPuertos = Math.min(velocidadPuerto1, velocidadPuerto2);

			int velocidadCable = conexion.getTipoCable().getVelocidad();

			int velocidadConexion = Math.min(velocidadPuertos, velocidadCable);

			velocidadMaxima = velocidadConexion;
		}

		return velocidadMaxima;
	}

	public Coordinador getCoordinador() {
		return coordinador;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public Graph<Equipo, Conexion> getRed() {
		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones());
		}
		return redGrafo;
	}

	public void update() {
		this.actualizar = true;
	}

	// interfaz grafica
	public String realizarPingEquipo(Equipo equipoSelected) {

		// Verificar el estado del equipo y devolver el mensaje correspondiente
		if (equipoSelected == null)
			return "El equipo no existe";
		if (equipoSelected.getEstado()) {
			return " Equipo: " + equipoSelected.getCodigo() + "\n Descripcion: " + equipoSelected.getDescripcion()
					+ " \n está activo.";
		} else {
			return " Equipo: " + equipoSelected.getCodigo() + "\n descripcion: " + equipoSelected.getDescripcion()
					+ " \n está inactivo.";
		}
	}

}
