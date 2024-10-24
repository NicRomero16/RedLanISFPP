package controlador;

import negocio.Calculo;
import negocio.Red;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;

import excepciones.ConexionInexistenteException;
import excepciones.EquipoExistenteException;
import interfaz.Interfaz;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoEquipo;
import modelo.Ubicacion;

public class Coordinador {
	private Red empresa;
	private Calculo calculo;
	private Interfaz interfaz;

	public Coordinador() {
		calculo = new Calculo();
	}

	public Red getEmpresa() {
		return empresa;
	}

	public void imprimirGrafo() {
		Set<Conexion> edges = calculo.getRed().edgeSet();
		interfaz.imprimirGrafo(edges);
	}

	public String imprimirConexionesGrafo() {

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

	public String imprimirEquipos() {
		TreeMap<String, Equipo> edges = listarEquipos();

		StringBuilder resultado = new StringBuilder();

		for (Equipo edge : edges.values()) {

			resultado.append("Equipo: " + edge.getCodigo() + "\n");

			resultado.append("Decripcion: " + edge.getDescripcion() + "\n Marca: " + edge.getMarca() + "\n Modelo: "
					+ edge.getModelo() + "\n " + edge.getTipoEquipo() + "\n " + edge.getUbicacion() + "\n "
					+ edge.getPuertos() + "\n Direciones IP: " + edge.getDireccionesIP() + "\n Estado: "
					+ edge.getEstado() + "\n");
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

	public void setEmpresa(Red empresa) {
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

	public void agregarEquipoMock() {
		Equipo equipo = new Equipo("REQW", "eLjota", null, null, new TipoEquipo("RE", "jota"),
				new Ubicacion("A01", "Aula 1"), false);
		try {
			empresa.agregarEquipo(equipo);
		} catch (EquipoExistenteException e) {
			System.out.println("Error al agregar equipo");
			e.printStackTrace();
		}

	}

	public void agregarEquipo(Equipo equipo) {
		try {
			empresa.agregarEquipo(equipo);
		} catch (EquipoExistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calculo.update();
	}

//interfaz grafica
	public Graph<Equipo, Conexion> cargarDatos() {

		Graph<Equipo, Conexion> grafo = null;
		try {
			grafo = calculo.cargarDatos(listarConexiones());
		} catch (EquipoExistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grafo;
	}
}
