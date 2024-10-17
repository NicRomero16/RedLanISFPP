package negocio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import controlador.Coordinador;
import excepciones.ConexionInexistenteException;
import excepciones.EquipoExistenteException;
import excepciones.EquipoInexistenteException;
import modelo.*;

public class Calculo {

	private Coordinador coordinador;

	private Graph<Equipo, Conexion> red;

	public Calculo() {
		red = new DefaultUndirectedGraph<>(Conexion.class);
	}

	public Graph<Equipo, Conexion> cargarDatos(List<Conexion> conexiones) throws EquipoExistenteException {

		red = new DefaultUndirectedWeightedGraph<>(Conexion.class);

		for (Conexion conexion : conexiones) {
			Equipo equipo1 = conexion.getEquipo1();
			Equipo equipo2 = conexion.getEquipo2();
			int velocidadEntre = conexion.getTipoCable().getVelocidad();

			red.addVertex(equipo1);
			red.addVertex(equipo2);
			red.addEdge(equipo1, equipo2, conexion);
			red.setEdgeWeight(conexion, velocidadEntre);

		}
		return red;
	}

	public List<Equipo> mostrarEquiposIntermedios(Equipo origen, Equipo destino)
			throws Exception, EquipoInexistenteException {

		List<Equipo> camino = DijkstraShortestPath.findPathBetween(red, origen, destino).getVertexList();

		if (camino.size() > 2) {
			List<Equipo> verticesIntermedios = camino.subList(1, camino.size() - 1);
			return verticesIntermedios;
		} else
			throw new Exception("Los equipos no tienen equipos intermedios");
	}

	public double velocidadMaxima(Equipo origen, Equipo destino) throws ConexionInexistenteException {

		DijkstraShortestPath<Equipo, Conexion> dijkstraAlg = new DijkstraShortestPath<>(red);
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

			velocidadMaxima = Math.min(velocidadMaxima, velocidadConexion);
		}

		return velocidadMaxima;
	}

	public TreeMap<String, Equipo> ping(TreeMap<String, Equipo> equipos) {
		TreeMap<String, Equipo> rango = new TreeMap<String, Equipo>();
		return rango;
	}

	public Coordinador getCoordinador() {
		return coordinador;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public Graph<Equipo, Conexion> getRed() {
		return red;
	}

	public void setRed(Graph<Equipo, Conexion> red) {
		this.red = red;
	}

}
