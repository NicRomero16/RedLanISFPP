package negocio;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

import controlador.Coordinador;
import modelo.Equipo;
import modelo.Conexion;

import java.util.List;
import java.util.TreeMap;

public class Calculo {

	private Coordinador coordinador;
	private Graph<Equipo, Conexion> red;
	private TreeMap<String, Equipo> equiposMap;

	public Calculo(List<Equipo> equipos, List<Conexion> conexiones) {

		red = new SimpleGraph<>(Conexion.class);
		equiposMap = new TreeMap<String, Equipo>();

		for (Equipo e : equipos) {
			equiposMap.put(e.getCodigo(), e);
			red.addVertex(e);
		}
		for (Conexion c : conexiones) {
			Equipo equipo1 = equiposMap.get(c.getEquipo1().getCodigo());
			Equipo equipo2 = equiposMap.get(c.getEquipo2().getCodigo());
			red.addEdge(equipo1, equipo2, c);
		}
	}
}
