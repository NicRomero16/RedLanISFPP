package logica;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import modelo.Conexiones;
import modelo.Maquina;
import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Graph;
import net.datastructures.Vertex;

public class Logica {

	private Graph<Maquina, Conexiones> red;
	private TreeMap<String, Vertex<Maquina>> vertices;

	public Logica(TreeMap<String, Maquina> maq, List<Conexiones> conexiones) {

		red = new AdjacencyMapGraph<>(false);

		// Carga de maquinas
		vertices = new TreeMap<>();
		
		for (Entry<String, Maquina> entry : maq.entrySet()) {
			
			String llave = entry.getKey();
			Maquina maquina = entry.getValue();
			Vertex<Maquina> vertex = red.insertVertex(maquina);
			vertices.put(llave, vertex);

			System.out.println("Insertando vertice: " + maquina.getIpAddress() + " | con la llave " + llave);
			
		}
		// Carga de conexiones
		for (Conexiones conexion : conexiones) { //se utiliza para recorrer las las vertices compu y router para acceder a las IPs
			String ipCompu = conexion.getSourceNode().getIpAddress();
			String ipRouter = conexion.getTargetNode().getIpAddress();

			Vertex<Maquina> compuVertice = vertices.get(conexion.getSourceNode().getId());
			Vertex<Maquina> routerVertice = vertices.get(conexion.getTargetNode().getId());
			
			if (compuVertice != null && routerVertice != null) {
				System.out.println("Insertando enlace entre " + ipCompu + " y " + ipRouter); //inserta un arco no dirigido entre la ipCompu y ipRouter
				red.insertEdge(compuVertice, routerVertice, conexion); 
				
			} else {	
				System.err.println("ERROR al no encontrar vertice " + ipCompu + " o " + ipRouter);
				
			}
			
		}
	}
}
