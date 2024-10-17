package controlador;

import negocio.Calculo;
import negocio.Empresa;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import excepciones.ConexionInexistenteException;
import interfaz.Interfaz;
import modelo.Conexion;
import modelo.Equipo;

public class Coordinador {
	private Empresa empresa;
	private Calculo calculo;
	private Interfaz interfaz;

	public Coordinador() {
		calculo = new Calculo();
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void imprimirGrafo() {
		Set<Conexion> edges = calculo.getRed().edgeSet();
		interfaz.imprimirGrafo(edges);
	}

	public void mostrarEquiposIntermedios(Equipo origen, Equipo destino) throws Exception {
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
		TreeMap<String, Equipo> equipos = empresa.getEquipos();
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

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
		return empresa.buscarEquipo(equipo.getCodigo());
	}

	public TreeMap<String, Equipo> listarEquipos() {
		return empresa.getEquipos();
	}

	public List<Conexion> listarConexiones() {
		return empresa.getConexiones();
	}
}
