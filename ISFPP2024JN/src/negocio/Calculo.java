package negocio;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import controlador.Coordinador;
import excepciones.ConexionInexistenteException;
import excepciones.EquipoExistenteException;
import excepciones.EquipoInexistenteException;
import modelo.Conexion;
import modelo.Equipo;

public class Calculo {

	private Coordinador coordinador;

	private Graph<Equipo, Conexion> red;
	private boolean actualizar;
	
	public Calculo() {
		red = new DefaultUndirectedGraph<>(Conexion.class);
	}

	public Graph<Equipo, Conexion> cargarDatos(List<Conexion> conexiones) throws EquipoExistenteException {
		
		this.actualizar = false;
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

	public List<Equipo> mostrarEquiposIntermedios(Equipo origen, Equipo destino) throws EquipoInexistenteException {
		if(this.actualizar)
			try {
				this.cargarDatos(coordinador.listarConexiones());
			} catch (EquipoExistenteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		List<Equipo> camino = DijkstraShortestPath.findPathBetween(red, origen, destino).getVertexList();

		if (camino.size() > 2) {
			List<Equipo> verticesIntermedios = camino.subList(1, camino.size() - 1);
			return verticesIntermedios;
		} else
			throw new EquipoInexistenteException("Los equipos no tienen equipos intermedios");
	}

	public double velocidadMaxima(Equipo origen, Equipo destino) throws ConexionInexistenteException {
		if(this.actualizar)
			try {
				this.cargarDatos(coordinador.listarConexiones()); //al avanzar el codigo tenemos q hacer el patron de diseño observer
			} catch (EquipoExistenteException e) {
				e.printStackTrace();
			}
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

	public Coordinador getCoordinador() {
		return coordinador;
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public Graph<Equipo, Conexion> getRed() {
		if(this.actualizar)
			try {
				this.cargarDatos(coordinador.listarConexiones());
			} catch (EquipoExistenteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return red;
	}

	
	public void update() {
		this.actualizar = true;
	}
	
}
