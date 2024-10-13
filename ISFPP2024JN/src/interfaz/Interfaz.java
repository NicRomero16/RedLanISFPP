package interfaz;

import java.util.List;
import java.util.TreeMap;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;

public class Interfaz {
	private Coordinador coordinador;

	public Interfaz() {

	}

	public Equipo ingresarEquipoOrigen(TreeMap<String, Equipo> listarEquipos) {
		// TODO Auto-generated method stub
		return null;
	}

	public Equipo ingresarEquipoDestino(TreeMap<String, Equipo> listarEquipos) {
		// TODO Auto-generated method stub
		return null;
	}

	// imprime en pantalla la conexion entre dos equipos
	// junto con la velocidad que tiene de una a la otra(que seria el resultado)
	public void resultado1(List<Conexion> recorrido) {
	    if (recorrido == null || recorrido.isEmpty()) {
	        System.out.println("No se encontró un camino.");
	    } else {
	        System.out.println("Recorrido encontrado:");
	        for (Conexion conexion : recorrido) {
	            System.out.println("Conexión entre: " + conexion.getEquipo1().getCodigo() + " y " + conexion.getEquipo2().getCodigo());
	        }
	        // Imprimir la velocidad máxima (ya calculada en el método velocidadMaxima)
	    }
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
}
