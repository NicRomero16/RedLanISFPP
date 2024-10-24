package controlador;

import negocio.Calculo;
import negocio.Red;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import excepciones.ConexionExistenteException;
import excepciones.ConexionInexistenteException;
import excepciones.EquipoExistenteException;
import excepciones.EquipoInexistenteException;
import interfaz.Interfaz;
import modelo.Conexion;
import modelo.Equipo;

public class Coordinador {
	private Red red;
	private Calculo calculo;
	private Interfaz interfaz;

	public Coordinador() {
		calculo = new Calculo();
	}
	
	public void agregarEquipo(Equipo equipo) {
		try {
			red.agregarEquipo(equipo);
		} catch (EquipoExistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calculo.update();
	}

	public void eliminarEquipo(Equipo equipo) {
		try {
			red.eliminarEquipo(equipo);
		} catch (EquipoInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calculo.update();
	}

	public void modificarEquipo(Equipo equipo) {
		try {
			red.modificarEquipo(equipo);
		} catch (EquipoInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calculo.update();
	}

	public void agregarConexion(Conexion conexion) {
		try {
			red.agregarConexion(conexion);
		} catch (ConexionExistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calculo.update();
	}

	public void eliminarConexion(Conexion conexion) {
		try {
			red.borrarConexion(conexion);
		} catch (ConexionInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calculo.update();
	}

	public void modificarConexion(Conexion conexion) {
		try {
			red.modificarConexion(conexion);
		} catch (ConexionInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calculo.update();
	}

	public void imprimirGrafo() {
		Set<Conexion> edges = calculo.getRed().edgeSet();
		interfaz.imprimirGrafo(edges);
	}

	public String imprimirGrafo2() {

		Set<Conexion> edges = calculo.getRed().edgeSet();

		StringBuilder resultado = new StringBuilder();

		for (Conexion edge : edges) {
			resultado.append("Conexion:  \n");

			resultado.append("Equipo origen: \n Codigo: " + edge.getEquipo1().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo1().getDescripcion() + "\n Marca: " + edge.getEquipo1().getMarca() + "\n Modelo: "
					+ edge.getEquipo1().getModelo() + "\n " + edge.getEquipo1().getTipoEquipo() + "\n "
					+ edge.getEquipo1().getUbicacion() + "\n " + edge.getEquipo1().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo1().getDireccionesIP() + "\n Estado: " + edge.getEquipo1().getEstado() + "\n "
					+ edge.getTipoPuerto1() + "\n");
			resultado.append("\nEquipo destino: \n Codigo: " + edge.getEquipo2().getCodigo() + "\n Decripcion: "
					+ edge.getEquipo2().getDescripcion() + "\n Marca: " + edge.getEquipo2().getMarca() + "\n Modelo: "
					+ edge.getEquipo2().getModelo() + "\n " + edge.getEquipo2().getTipoEquipo() + "\n "
					+ edge.getEquipo2().getUbicacion() + "\n " + edge.getEquipo2().getPuertos() + "\n Direciones IP: "
					+ edge.getEquipo2().getDireccionesIP() + "\n Estado: " + edge.getEquipo2().getEstado() + "\n "
					+ edge.getTipoPuerto2() + "\n");
			resultado.append(" \nTipo de cable de la conexion: \n " + edge.getTipoCable() + "\n");
			resultado.append("================================================================\n");

		}

		return resultado.toString();
	}

	public void mostrarEquiposIntermedios(Equipo origen, Equipo destino) {
		String resultado = "Los equipos intermedios entre " + origen.getCodigo() + " y " + destino.getCodigo()
				+ " son: \n" + calculo.mostrarEquiposIntermedios(origen, destino);
		interfaz.mostrarEquiposIntermedios(resultado);
	}

	public void velocidadMaxima(Equipo origen, Equipo destino) {
		try {
			String resultado = "la velocidad maxima entre el " + origen.getCodigo() + " y " + destino.getCodigo()
					+ " es de " + calculo.velocidadMaxima(origen, destino) + " Mbps ";
			interfaz.mostrarVelocidadMaxima(resultado);
		} catch (ConexionInexistenteException e) {
			e.printStackTrace();
		}
	}

	public void ping(String ip) {
		TreeMap<String, Equipo> equipos = red.getEquipos();
		for (Equipo equipo : equipos.values()) {
			if (equipo.contieneIp(ip)) {
				interfaz.imprimirEquipo(equipo);
				if (equipo.getEstado()) {
					interfaz.imprimirPing("el equipo " + equipo.getCodigo() + " con la IP " + equipo.getDireccionesIP()
							+ " esta en estado activo (" + equipo.getEstado() + ")");
				} else {
					interfaz.imprimirPing("el equipo " + equipo.getCodigo() + " con la IP: " + equipo.getDireccionesIP()
							+ " esta en estado inactivo (" + equipo.getEstado() + ")");
				}
			}
		}
	}

	public void estadoEquipos(TreeMap<String, Equipo> eq) {
		TreeMap<String, Equipo> equipos = eq;
		for (Equipo equipo : equipos.values()) {
			if (equipo.getEstado()) {
				interfaz.imprimirPing("el equipo " + equipo.getCodigo() + " con la IP " + equipo.getDireccionesIP()
						+ " esta en estado activo (" + equipo.getEstado() + ")");
			} else {
				interfaz.imprimirPing("el equipo " + equipo.getCodigo() + " con la IP: " + equipo.getDireccionesIP()
						+ " esta en estado inactivo (" + equipo.getEstado() + ")");
			}
		}
	}

	public Red getRed() {
		return red;
	}
	
	public Calculo getCalculo() {
		return calculo;
	}

	public void setCalculo(Calculo calculo) {
		this.calculo = calculo;
	}

	public Interfaz getInterfaz() {
		return interfaz;
	}

	public void setInterfaz(Interfaz interfaz) {
		this.interfaz = interfaz;
	}

	public Equipo buscarEquipo(Equipo equipo) {
		return red.buscarEquipo(equipo.getCodigo());
	}

	public TreeMap<String, Equipo> listarEquipos() {
		return red.getEquipos();
	}

	public List<Conexion> listarConexiones() {
		return red.getConexiones();
	}
}
