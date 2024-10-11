package negocio;

import java.util.List;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.*;

public class Calculo {

	private Coordinador coordinador;

	private Graph<Equipo, Conexion> red;

	public Calculo() {
	}

	public void iniciar(List<Conexion> conexiones) throws EquipoExistenteException {

		red = new DefaultUndirectedWeightedGraph<>(Conexion.class);

		//
		for (Conexion conexion : conexiones) {
			Equipo equipo1 = conexion.getEquipo1();
			Equipo equipo2 = conexion.getEquipo2();
			double velocidadEntre = conexion.getTipoCable().getVelocidad();
			red.addVertex(equipo1);
			red.addVertex(equipo2);

			// Verificar que ambos equipos existan en el grafo
			if (!red.containsVertex(equipo1)) {
				throw new EquipoInexistenteException("El equipo " + equipo1 + " no existe");
			}
			if (!red.containsVertex(equipo2)) {
				throw new EquipoInexistenteException("El equipo " + equipo2 + " no existe");
			}
			// Verificar que los equipos no sean el mismo
			if (equipo1.equals(equipo2)) {
				throw new EquipoExistenteException("El equipo no puede estar conectado a sí mismo");
			}
			// Verificar si la arista ya existe
			if (red.getEdge(equipo1, equipo2) == null) {
				// Agregar la arista y establecer la velocidad
				Conexion edge = red.addEdge(equipo1, equipo2);
				red.setEdgeWeight(edge, velocidadEntre);
			} else {
				throw new EquipoExistenteException("La conexión entre los dos equipos ya existe");
			}
		}
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
}
