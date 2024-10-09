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

    public Calculo() {
  
        red = new SimpleGraph<>(Conexion.class);

        equiposMap = new TreeMap<>();
    }


    public void cargarDatos(List<Conexion> conexiones, List<Equipo> equipos) {

        for (Equipo equipo : equipos) {
            equiposMap.put(equipo.getCodigo(), equipo);  
            red.addVertex(equipo);  
        }

        for (Conexion conexion : conexiones) {
            Equipo equipo1 = equiposMap.get(conexion.getEquipo1().getCodigo());  
            Equipo equipo2 = equiposMap.get(conexion.getEquipo2().getCodigo()); 
            red.addEdge(equipo1, equipo2, conexion);  
        }
    }
}
