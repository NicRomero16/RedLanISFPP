package negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

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

	public List<Equipo> mostrarEquiposEntrePings(String ipOrigen, String ipDestino) {
		TreeMap<String, Equipo> equipos = coordinador.listarEquipos();
		Equipo eOrigen = null;
		Equipo eDestino = null;

		for (Equipo equipo : equipos.values()) {
			if (equipo.getDireccionesIP().contains(ipOrigen))
				eOrigen = equipo;
			if (equipo.getDireccionesIP().contains(ipDestino))
				eDestino = equipo;
		}

		List<Equipo> camino = DijkstraShortestPath.findPathBetween(redGrafo, eOrigen, eDestino).getVertexList();

		return camino;
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

	/**
	 * Calcula la velocidad máxima de conexión entre dos equipos.
	 *
	 * Este método utiliza el algoritmo de Dijkstra para encontrar el camino más
	 * corto entre el equipo de origen y el equipo de destino en un grafo que
	 * representa las conexiones de red. Solo se consideran las conexiones activas y
	 * cuyos tipos de puertos y cables no sean nulos. Si se encuentra algún equipo
	 * desactivado o si no hay conexiones activas, se retorna un código de error
	 * específico.
	 *
	 * @param origen  El equipo de origen desde el cual se quiere calcular la
	 *                velocidad.
	 * @param destino El equipo de destino al cual se quiere calcular la velocidad.
	 * @return La velocidad máxima de conexión entre los dos equipos, o un código de
	 *         error: -1 si el origen es igual al destino, -2 si alguno de los
	 *         equipos no existe en el grafo, -3 si no hay un camino entre los
	 *         equipos, -4 si alguno de los tipos de puertos o cables es nulo, -5 si
	 *         no hay conexiones activas entre los equipos.
	 */
	public double velocidadMaximaEntreEquipos(Equipo origen, Equipo destino) {
		if (this.actualizar) {
			this.cargarDatos(coordinador.listarConexiones());
			this.actualizar = false;
		}
		if (origen.equals(destino))
			return -1;
		if (!redGrafo.containsVertex(origen) || !redGrafo.containsVertex(destino))
			return -2;

		DijkstraShortestPath<Equipo, Conexion> dijkstraAlg = new DijkstraShortestPath<>(redGrafo);
		GraphPath<Equipo, Conexion> path = dijkstraAlg.getPath(origen, destino);

		if (path == null)
			return -3;

		List<Conexion> recorrido = path.getEdgeList();
		double velocidadMaxima = Double.MAX_VALUE;
		boolean hayConexionesActivas = false;

		for (Conexion conexion : recorrido) {
			Equipo equipo1 = conexion.getEquipo1();
			Equipo equipo2 = conexion.getEquipo2();

			if (!equipo1.getEstado() || !equipo2.getEstado()) {
				continue; // Ignora esta conexión si alguno de los equipos no está activo
			}

			// Si llega aca quiere decir que la conexión esta activa.
			hayConexionesActivas = true;

			Integer velocidadPuerto1 = null;
			Integer velocidadPuerto2 = null;
			Integer velocidadCable = null;

			if (conexion.getTipoPuerto1() != null) {
				velocidadPuerto1 = conexion.getTipoPuerto1().getVelocidad();
			}

			if (conexion.getTipoPuerto2() != null) {
				velocidadPuerto2 = conexion.getTipoPuerto2().getVelocidad();
			}

			if (conexion.getTipoCable() != null) {
				velocidadCable = conexion.getTipoCable().getVelocidad();
			}

			if (velocidadPuerto1 == null || velocidadPuerto2 == null || velocidadCable == null) {
				return -4;
			}

			int velocidadPuertos = Math.min(velocidadPuerto1, velocidadPuerto2);
			double velocidadConexion = Math.min(velocidadPuertos, velocidadCable);

			velocidadMaxima = Math.min(velocidadMaxima, velocidadConexion);
		}

		if (!hayConexionesActivas) {
			return -5;
		}

		return velocidadMaxima;
	}

	public TreeMap<String, Equipo> rangoIp(String direccionIP) {
		TreeMap<String, Equipo> equipos1 = coordinador.listarEquipos();
		TreeMap<String, Equipo> equipos2 = new TreeMap<String, Equipo>();
		for (Equipo equipo : equipos1.values()) {
			List<String> ips = equipo.getDireccionesIP();
			for (String ip : ips)
				if (ip.startsWith(direccionIP))
					equipos2.put(equipo.getCodigo(), equipo);
		}
		return equipos2;
	}

	public List<String> recibirDireccionesIP(Equipo equipo, String ip) {
		List<String> direccionesIP = new ArrayList<String>();
		for (String direc : equipo.getDireccionesIP())
			if (direc.startsWith(ip))
				direccionesIP.add(direc);
		return direccionesIP;
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
