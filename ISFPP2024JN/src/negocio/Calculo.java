package negocio;

import java.util.List;
import java.util.Set;
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

	// metodo para calcular la velocidad de conexion mas rapida entre dos equipos
	public List<Conexion> velocidadMaxima(Equipo origen, Equipo destino) throws ConexionInexistenteException {

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
		// Imprimir el resultado
		System.out.println("Velocidad máxima entre " + origen.getCodigo() + " y " + destino.getCodigo() + ": "
				+ velocidadMaxima + " Mbps");

		return recorrido; // Devolver el recorrido para ser mostrado por la interfaz
	}
	
	public TreeMap<String, Equipo> mostrarPing(TreeMap<String, Equipo> equipos) {
		TreeMap<String, Equipo> rango = new TreeMap<String, Equipo>();
		for (Equipo equipo : equipos.values())
			rango.put(equipo.getCodigo(), equipo.getDireccionesIP());
		return rango;
	}

	public void imprimirGrafo() {
		Set<Conexion> edges = red.edgeSet();

		// Iterar sobre todas las aristas
		for (Conexion edge : edges) {
			System.out.println("Conexion: ");
			System.out.println("Equipo origen: \n Codigo: " + edge.getEquipo1().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo1().getDescripcion() + "\n Marca: " + edge.getEquipo1().getMarca() + "\n Modelo: "
					+ edge.getEquipo1().getModelo() + "\n " + edge.getEquipo1().getTipoEquipo() + "\n "
					+ edge.getEquipo1().getUbicacion() + "\n " + edge.getEquipo1().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo1().getDireccionesIP() + "\n " + edge.getTipoPuerto1() + "\n");
			System.out.println("Equipo destino: \n Codigo: " + edge.getEquipo2().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo2().getDescripcion() + "\n Marca: " + edge.getEquipo2().getMarca() + "\n Modelo: "
					+ edge.getEquipo2().getModelo() + "\n " + edge.getEquipo2().getTipoEquipo() + "\n "
					+ edge.getEquipo2().getUbicacion() + "\n " + edge.getEquipo2().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo2().getDireccionesIP() + "\n " + edge.getTipoPuerto2() + "\n");
			System.out.println(" Tipo de cable de la conexion: \n " + edge.getTipoCable());
			System.out.println(
					"=========================================================================================================");

		}
	}
	
	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public Graph<Equipo, Conexion> getRed() {
		return red;
	}
}
