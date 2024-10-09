package negocio;


import org.jgrapht.*;
import org.jgrapht.graph.SimpleGraph;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;

public class Calculo {

	private Coordinador coordinador;
	
	private Graph<Equipo,Conexion> red;
	
	   public Calculo() {
	        red = new SimpleGraph<>(Conexion.class);
	    }

	    public void agregarEquipo(Equipo equipo) {
	        red.addVertex(equipo);
	    }
}
