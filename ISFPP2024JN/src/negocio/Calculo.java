package negocio;

import java.util.List;
import java.util.TreeMap;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;

public class Calculo {

	private Coordinador coordinador;

	private Graph<Equipo, DefaultWeightedEdge> red;
	private TreeMap<String, Equipo> vertices;

	public Calculo() {
		this.vertices = new TreeMap<>();
	}

	public void cargarEquipos(TreeMap<String, Equipo> eq, List<Conexion> conexiones) throws EquipoExistenteException {

		red = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

		// Cargar equipos (vértices)
		for (Equipo equipo : eq.values()) {
			red.addVertex(equipo);
			vertices.put(equipo.getCodigo(), equipo); // Crea un mapa temporal para almacenar los equipos por código
		}

		// Cargar conexiones
		for (Conexion conexion : conexiones) {
			Equipo equipo1 = conexion.getEquipo1();
			Equipo equipo2 = conexion.getEquipo2();
			double peso = conexion.getTipoCable().getVelocidad();

			// Verificar que ambos equipos existan en el grafo
			if (red.containsVertex(equipo1) && red.containsVertex(equipo2)) {
				// verificar si el equipo1 es = al equipo1
				if (red.containsVertex(equipo1) != red.containsVertex(equipo2)) {
					// Verificar si la arista ya existe
					if (red.getEdge(equipo1, equipo2) == null) {
						// Agregar la arista y establecer el peso
						DefaultWeightedEdge edge = red.addEdge(equipo1, equipo2);
						red.setEdgeWeight(edge, peso);
					} else {
						throw new EquipoExistenteException("La conexion entre los dos equipos ya existe");
					}
				} else {
					throw new EquipoExistenteException("equipo no puede estar conectado a si mismo");
				}
			} else {
				throw new EquipoInexistenteException("Uno o ambos equipos no existen");
			}
		}
	}
	
	

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
}
