package negocio;

import org.jgrapht.Graph;
import org.jgrapht.graph.*;

import controlador.Coordinador;
import modelo.Equipo;
import modelo.Conexion;

import java.util.List;
import java.util.TreeMap;

public class Calculo {

	private Coordinador coordinador;

	private Graph<Equipo, Conexion> red;
	private TreeMap<String, Equipo> vertices;

	public Calculo() {
	}

	public void cargarEquipos(TreeMap<String, Equipo> eq, List<Conexion> conexiones) {

		red = new SimpleGraph<>(Conexion.class);

		// Cargar equipos
		for (Equipo equipo : eq.values()) {
			red.addVertex(equipo);
			vertices.put(equipo.getCodigo(), equipo);
		}
		// Cargar conexiones
		for (Conexion conexion : conexiones) {
			Equipo equipo1 = conexion.getEquipo1();
			Equipo equipo2 = conexion.getEquipo2();

			if (red.containsVertex(equipo1) && red.containsVertex(equipo2))
				// Agregar la arista entre equipo1 y equipo2
				red.addEdge(equipo1, equipo2, conexion);
			else
				System.out.println("Error: Uno o ambos equipos no están en el grafo.");

		}
	}

	public TreeMap<String, Equipo> getVertices() {
		return vertices;
	}

	public Graph<Equipo, Conexion> getRed() {
		return red ;
	}

}