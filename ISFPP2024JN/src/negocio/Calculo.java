package negocio;

import java.util.List;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import controlador.Coordinador;
import modelo.*;

public class Calculo {

	private Coordinador coordinador;

	private Graph<Equipo, Conexion> red;

	public Calculo() {
	}

	public Graph<Equipo, Conexion> cargarDatos(List<Conexion> conexiones) throws EquipoExistenteException {

		// Inicializar el grafo no dirigido y ponderado
		red = new DefaultUndirectedWeightedGraph<>(Conexion.class);

		// Procesar cada conexión
		for (Conexion conexion : conexiones) {
			Equipo equipo1 = conexion.getEquipo1();
			Equipo equipo2 = conexion.getEquipo2();
			int velocidadEntre = conexion.getTipoCable().getVelocidad();

			// Agregar los equipos al grafo
			red.addVertex(equipo1);
			red.addVertex(equipo2);
			// Agrega la conexion entre los equipos
			red.addEdge(equipo1, equipo2, conexion);
			// Se asigna una velocidad a la conexion
			red.setEdgeWeight(conexion, velocidadEntre);

		}
		return red;
	}

	public List<Equipo> mostrarEquiposIntermedios(Equipo origen, Equipo destino) {
		// Verificar si ambos equipos están en la red
		if (!red.containsVertex(origen) || !red.containsVertex(destino)) {
			throw new EquipoInexistenteException("Uno de los equipos no existe en la red.");
		}
		// Busca el camino mas corto entre los dos vertices
		List<Equipo> camino = DijkstraShortestPath.findPathBetween(red, origen, destino).getVertexList();

		if (camino.size() > 1) {
			List<Equipo> verticesIntermedios = camino.subList(1, camino.size() - 1);
			return verticesIntermedios;
		}
		return null;
	}
	
	// metodo para calcular la velocidad de conexion mas rapida entre dos equipos
	public double velocidadMaxima(Equipo origen, Equipo destino) throws ConexionInexistenteException {

		// Verificar si ambos equipos están en la red
		if (!red.containsVertex(origen) || !red.containsVertex(destino)) {
			throw new EquipoInexistenteException("Uno de los equipos no existe en la red.");
		}

		// Utilizamos el algoritmo de Dijkstra para encontrar el camino más corto entre
		// origen y destino
		DijkstraShortestPath<Equipo, Conexion> dijkstraAlg = new DijkstraShortestPath<>(red);
		GraphPath<Equipo, Conexion> path = dijkstraAlg.getPath(origen, destino);

		// Si no existe un camino entre los equipos
		if (path == null) {
			throw new ConexionInexistenteException("No existe un camino entre los equipos " + origen + " y " + destino);
		}

		// Obtener la lista de conexiones (aristas) en el camino
		List<Conexion> recorrido = path.getEdgeList();

		// Inicializar la velocidad máxima posible (cuello de botella)
		double velocidadMaxima = Double.MAX_VALUE;

		// Recorrer todas las conexiones en el camino
		for (Conexion conexion : recorrido) {
			// Obtener los tipos de puerto de ambos equipos
			int velocidadPuerto1 = conexion.getTipoPuerto1().getVelocidad();
			int velocidadPuerto2 = conexion.getTipoPuerto2().getVelocidad();

			// La velocidad de esta conexión estará limitada por el puerto más lento
			int velocidadPuertos = Math.min(velocidadPuerto1, velocidadPuerto2);

			// Obtener la velocidad máxima del cable que conecta los dos equipos
			int velocidadCable = conexion.getTipoCable().getVelocidad();

			// La velocidad máxima de la conexión estará limitada por la velocidad del cable
			// y los puertos
			int velocidadConexion = Math.min(velocidadPuertos, velocidadCable);

			// Actualizar la velocidad máxima considerando el cuello de botella
			velocidadMaxima = Math.min(velocidadMaxima, velocidadConexion);
		}

		return velocidadMaxima; // Devolver el recorrido para ser mostrado por la interfaz
	}

	public TreeMap<String, Equipo> ping(TreeMap<String, Equipo> equipos) {
		TreeMap<String, Equipo> rango = new TreeMap<String, Equipo>();
		return rango;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public Graph<Equipo, Conexion> getRed() {
		return red;
	}
}
