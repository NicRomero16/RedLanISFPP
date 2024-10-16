package interfaz;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;

public class Interfaz {
	// main->controlador->calculo->controlador->interfaz->consola
	private Coordinador coordinador;

	public Interfaz() {

	}
	
	public Equipo ingresarIP(String ip) {
		coordinador.ping(ip);
		return null;
		
	}

	public Equipo ingresarEquipoOrigen(TreeMap<String, Equipo> listarEquipos) {
		return listarEquipos.get("SWAM");
	}

	public Equipo ingresarEquipoDestino(TreeMap<String, Equipo> listarEquipos) {
		return listarEquipos.get("AP09");
	}

	// imprime en pantalla la conexion entre dos equipos
	// junto con la velocidad que tiene de una a la otra(que seria el resultado)
	public void resultado1(String resultad) {
		System.out.println(resultad);
	}
	public void resultado2(String resultado) {
		System.out.println(resultado);
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public void imprimirEquipo(Equipo equipo) {
	System.out.println(equipo.toString());
		
	}

	public void imprimirPing(String resultado ) {
		
		System.out.println(resultado);
	}
	public void imprimirGrafo(Set<Conexion> edges) {
		StringBuilder resultado = new StringBuilder();
        
		// Iterar sobre todas las aristas
		for (Conexion edge : edges) {
			resultado.append("Conexion:  \n");
			
			resultado.append("Equipo origen: \n Codigo: " + edge.getEquipo1().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo1().getDescripcion() + "\n Marca: " + edge.getEquipo1().getMarca() + "\n Modelo: "
					+ edge.getEquipo1().getModelo() + "\n " + edge.getEquipo1().getTipoEquipo() + "\n "
					+ edge.getEquipo1().getUbicacion() + "\n " + edge.getEquipo1().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo1().getDireccionesIP() + "\n " + edge.getTipoPuerto1() + "\n");
			resultado.append("Equipo destino: \n Codigo: " + edge.getEquipo2().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo2().getDescripcion() + "\n Marca: " + edge.getEquipo2().getMarca() + "\n Modelo: "
					+ edge.getEquipo2().getModelo() + "\n " + edge.getEquipo2().getTipoEquipo() + "\n "
					+ edge.getEquipo2().getUbicacion() + "\n " + edge.getEquipo2().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo2().getDireccionesIP() + "\n " + edge.getTipoPuerto2() + "\n");
			resultado.append(" Tipo de cable de la conexion: \n " + edge.getTipoCable());
			resultado.append(
					"\n =========================================================================================================\n");

		}
		System.out.println(resultado.toString());
	}
}
