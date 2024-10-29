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

		redGrafo = new DefaultUndirectedWeightedGraph<>(Conexion.class);

		for (Conexion conexion : conexiones) {
			if (conexion != null) {
				Equipo equipo1 = conexion.getEquipo1();
				Equipo equipo2 = conexion.getEquipo2();

				if (equipo1 != null && equipo2 != null && conexion.getTipoCable() != null) {
					int velocidadEntre = conexion.getTipoCable().getVelocidad();
					redGrafo.addVertex(equipo1);
					redGrafo.addVertex(equipo2);
					redGrafo.addEdge(equipo1, equipo2, conexion);
					redGrafo.setEdgeWeight(conexion, velocidadEntre);
				}
			}
		}
		return redGrafo;
	}

	public List<Equipo> mostrarEquiposIntermedios(Equipo origen, Equipo destino) throws EquipoInexistenteException {
		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones());
			this.actualizar = false;
		}
		List<Equipo> camino = DijkstraShortestPath.findPathBetween(redGrafo, origen, destino).getVertexList();

		if (camino.size() > 2) {
			List<Equipo> verticesIntermedios = camino.subList(1, camino.size() - 1);
			return verticesIntermedios;
		} else
			throw new EquipoInexistenteException("Los equipos no tienen equipos intermedios");
	}

	public double velocidadMaxima(Equipo origen, Equipo destino) throws ConexionInexistenteException {
		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones()); // al avanzar el codigo tenemos q hacer el patron de
			this.actualizar = false; // diseño observer
		}
		DijkstraShortestPath<Equipo, Conexion> dijkstraAlg = new DijkstraShortestPath<>(redGrafo);
		GraphPath<Equipo, Conexion> path = dijkstraAlg.getPath(origen, destino);

		if (path == null)
			throw new ConexionInexistenteException(
					"No existe una conexion entre los equipos " + origen + " y " + destino);

		List<Conexion> recorrido = path.getEdgeList();

		double velocidadMaxima = 0;

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

	// interfaz grafica
	public boolean realizarPingEquipo(Equipo equipoSelected) {

		if (equipoSelected == null)
			throw new EquipoInexistenteException("El equipo no existe");

		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones());
			this.actualizar = false;
		}
		return equipoSelected.getEstado();
	}

	public double velocidadMaximaEntreEquipos(Equipo origen, Equipo destino) {
		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones());
			this.actualizar = false;
		}
		if (origen.equals(destino))
			return 0;
		if (!redGrafo.containsVertex(origen))
			return -1;
		if (!redGrafo.containsVertex(destino))
			return -2;

		DijkstraShortestPath<Equipo, Conexion> dijkstraAlg = new DijkstraShortestPath<>(redGrafo);
		GraphPath<Equipo, Conexion> path = dijkstraAlg.getPath(origen, destino);

		List<Conexion> recorrido = path.getEdgeList();

		double velocidadMaxima = 0;

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

	public void update() {
		this.actualizar = true;
	}
	
	public Graph<Equipo, Conexion> getRed() {
		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones());
			this.actualizar = false;
		}
		return redGrafo;
	}

	public Coordinador getCoordinador() {
		return coordinador;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

}
