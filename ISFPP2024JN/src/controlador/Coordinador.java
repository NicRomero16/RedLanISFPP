package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;

import excepciones.ConexionExistenteException;
import excepciones.ConexionInexistenteException;
import excepciones.EquipoExistenteException;
import interfaz.Interfaz;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;
import negocio.Calculo;
import negocio.Red;

public class Coordinador {
	private Red red;
	private Calculo calculo;
	private Interfaz interfaz;

	public Coordinador(Red red) {
		calculo = new Calculo();
		this.red = red;
	}

	public Graph<Equipo, Conexion> cargarDatos() {

		return calculo.cargarDatos(listarConexiones());
	}

	public void imprimirGrafo() {
		Set<Conexion> edges = calculo.getRed().edgeSet();
		interfaz.imprimirGrafo(edges);
	}

	public String imprimirConexionesGrafo() {
		Set<Conexion> edges = calculo.getRed().edgeSet();
		StringBuilder resultado = new StringBuilder();

		for (Conexion edge : edges) {
			resultado.append("=== Conexion ===\n\n");

			resultado.append("Equipo Origen:\n").append(" - Codigo: ").append(edge.getEquipo1().getCodigo())
					.append("\n").append(" - Descripción: ").append(edge.getEquipo1().getDescripcion()).append("\n")
					.append(" - Marca: ").append(edge.getEquipo1().getMarca()).append("\n").append(" - Modelo: ")
					.append(edge.getEquipo1().getModelo()).append("\n").append(" - Tipo de Equipo: ")
					.append(edge.getEquipo1().getTipoEquipo()).append("\n").append(" - Ubicación: ")
					.append(edge.getEquipo1().getUbicacion()).append("\n").append(" - Puertos: ")
					.append(edge.getEquipo1().getPuertos()).append("\n").append(" - Direcciones IP: ")
					.append(edge.getEquipo1().getDireccionesIP()).append("\n").append(" - Estado: ")
					.append(edge.getEquipo1().getEstado()).append("\n").append(" - Tipo de Puerto: ")
					.append(edge.getTipoPuerto1()).append("\n\n");

			resultado.append("Equipo Destino:\n").append(" - Codigo: ").append(edge.getEquipo2().getCodigo())
					.append("\n").append(" - Descripción: ").append(edge.getEquipo2().getDescripcion()).append("\n")
					.append(" - Marca: ").append(edge.getEquipo2().getMarca()).append("\n").append(" - Modelo: ")
					.append(edge.getEquipo2().getModelo()).append("\n").append(" - Tipo de Equipo: ")
					.append(edge.getEquipo2().getTipoEquipo()).append("\n").append(" - Ubicación: ")
					.append(edge.getEquipo2().getUbicacion()).append("\n").append(" - Puertos: ")
					.append(edge.getEquipo2().getPuertos()).append("\n").append(" - Direcciones IP: ")
					.append(edge.getEquipo2().getDireccionesIP()).append("\n").append(" - Estado: ")
					.append(edge.getEquipo2().getEstado()).append("\n").append(" - Tipo de Puerto: ")
					.append(edge.getTipoPuerto2()).append("\n\n");

			resultado.append("Tipo de Cable:\n").append(" - ").append(edge.getTipoCable()).append("\n\n");

			resultado.append("========================================\n\n");
		}
		return resultado.toString();
	}

	public String imprimirEquipos() {
		TreeMap<String, Equipo> equipos = listarEquipos();
		StringBuilder resultado = new StringBuilder();

		for (Equipo equipo : equipos.values()) {
			resultado.append("=== Equipo: ").append(equipo.getCodigo()).append(" ===\n\n").append(" - Descripción: ")
					.append(equipo.getDescripcion()).append("\n").append(" - Marca: ").append(equipo.getMarca())
					.append("\n").append(" - Modelo: ").append(equipo.getModelo()).append("\n")
					.append(" - Tipo de Equipo: ").append(equipo.getTipoEquipo()).append("\n").append(" - Ubicación: ")
					.append(equipo.getUbicacion()).append("\n").append(" - Puertos: ").append(equipo.getPuertos())
					.append("\n").append(" - Direcciones IP: ").append(equipo.getDireccionesIP()).append("\n")
					.append(" - Estado: ").append(equipo.getEstado()).append("\n\n")
					.append("========================================\n\n");
		}

		return resultado.toString();
	}

	public void mostrarEquiposIntermedios(Equipo origen, Equipo destino) {
		String resultado = "Los equipos intermedios entre " + origen.getCodigo() + " y " + destino.getCodigo()
				+ " son: \n" + calculo.mostrarEquiposIntermedios(origen, destino);
		interfaz.mostrarEquiposIntermedios(resultado);
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

	public boolean realizarPingEquipo(String equipoSelected) {
		return calculo.realizarPingEquipo(red.buscarEquipo(equipoSelected));
	}

	public double velocidadMaximaEntreEquipos(String equipo1, String equipo2) {

		return calculo.velocidadMaximaEntreEquipos(red.buscarEquipo(equipo1), red.buscarEquipo(equipo2));

	}

	public void velocidadMaximaEntreEquipos(Equipo equipo, Equipo equipo2) {

		calculo.velocidadMaximaEntreEquipos(equipo, equipo2);
	}

	public String[] devolverEquipoCodigos() {
		TreeMap<String, Equipo> equipos = listarEquipos();
		List<String> equipoList = new ArrayList<>();
		for (Equipo equipo : equipos.values()) {
			equipoList.add(equipo.getCodigo());
		}
		return equipoList.toArray(new String[0]);
	}

	public String[] devolverTipoPuertoCodigo() {
		TreeMap<String, TipoPuerto> tipoPuertos = listarTipoPuertos();
		List<String> tipoPuertoList = new ArrayList<>();
		for (TipoPuerto tipoPuerto : tipoPuertos.values()) {
			tipoPuertoList.add(tipoPuerto.getCodigo());
		}
		return tipoPuertoList.toArray(new String[0]);
	}

	public String[] devolverTipoCableCodigo() {
		TreeMap<String, TipoCable> tipoCables = listarTipoCables();
		List<String> tipoCableList = new ArrayList<>();
		for (TipoCable tipoCable : tipoCables.values()) {
			tipoCableList.add(tipoCable.getCodigo());
		}
		return tipoCableList.toArray(new String[0]);
	}

	public int getPuertosDisponibles(Equipo equipo) {
		int totalPuertos = equipo.getCantidadPuertos();
		int puertosEnUso = 0;
		List<Conexion> conexionesActivas = listarConexiones();

		for (Conexion conexion : conexionesActivas) {
			if (conexion.getEquipo1().equals(equipo)) {
				puertosEnUso++; // Cuenta el puerto usado en equipo1
			}
			if (conexion.getEquipo2().equals(equipo)) {
				puertosEnUso++; // Cuenta el puerto usado en equipo2
			}
		}

		return totalPuertos - puertosEnUso;
	}

	public boolean existeConexion(Equipo e1, Equipo e2) {
		List<Conexion> conexiones = listarConexiones();

		for (Conexion conexion : conexiones) {
			if ((conexion.getEquipo1().equals(e1) && conexion.getEquipo2().equals(e2))
					|| (conexion.getEquipo1().equals(e2) && conexion.getEquipo2().equals(e1))) {
				return true;
			}
		}
		return false;
	}

	public boolean tieneConexiones(Equipo equipo) {
		List<Conexion> conexiones = listarConexiones();
		for (Conexion conexion : conexiones) {
			if (conexion.getEquipo1() != null && conexion.getEquipo2() != null)
				if ((conexion.getEquipo1().equals(equipo)) || (conexion.getEquipo2().equals(equipo)))
					return true;
		}
		return false;
	}

	public List<Conexion> buscarConexiones(Equipo equipo) {
		List<Conexion> conexiones = red.getConexiones();
		List<Conexion> conexionesEq = new ArrayList<Conexion>();

		for (Conexion conexion : conexiones)
			if (conexion.getEquipo1() != null && conexion.getEquipo2() != null)
				if (conexion.getEquipo1().equals(equipo) || conexion.getEquipo2().equals(equipo))
					conexionesEq.add(conexion);
		System.out.println(conexionesEq);
		return conexionesEq;
	}

	public void agregarConexion(Conexion conexion) throws Exception {

		if (getPuertosDisponibles(conexion.getEquipo1()) > 0 && getPuertosDisponibles(conexion.getEquipo2()) > 0) {
			try {
				red.agregarConexion(conexion);
				calculo.update();
				calculo.cargarDatos(listarConexiones());
			} catch (ConexionExistenteException e) {
				throw new ConexionExistenteException("Error: Ya existe una conexión entre estos equipos.");
			}
		} else {
			throw new Exception("Error: No hay puertos disponibles en uno o ambos equipos.");
		}
	}

	public void borrarConexion(Conexion conexion) {
		try {
			red.borrarConexion(conexion);
		} catch (ConexionInexistenteException e) {
			e.printStackTrace();
		}
		calculo.update();
		calculo.cargarDatos(listarConexiones());
	}

	public Conexion buscarConexion(Equipo equipo1, TipoPuerto tipoPuerto1, Equipo equipo2, TipoPuerto tipoPuerto2,
			TipoCable tipoCable) {

		List<Conexion> conexiones = listarConexiones();

		for (Conexion conexion : conexiones) {
			if (conexion.getEquipo1().equals(equipo1) && conexion.getTipoPuerto1().equals(tipoPuerto1)
					&& conexion.getEquipo2().equals(equipo2) && conexion.getTipoPuerto2().equals(tipoPuerto2)
					&& conexion.getTipoCable().equals(tipoCable)) {
				return conexion;
			}
		}
		return null;
	}

	public void agregarPuertos(Equipo equipo, int cantidad, TipoPuerto tipoPuerto) {
		equipo.agregarPuerto(cantidad, tipoPuerto);
	}

	public void agregarDireccionesIP(Equipo equipo, String ip) {
		equipo.agregarDireccionIP(ip);
	}

	public TipoEquipo buscarTipoEquipo(String codigo) {
		return red.buscarTipoEquipo(codigo);
	}

	public Ubicacion buscarUbicacion(String codigo) {
		return red.buscarUbicacion(codigo);
	}

	public Equipo buscarEquipo(String codigo) {
		return red.buscarEquipo(codigo);
	}

	public void agregarEquipo(Equipo equipo) {
		try {
			red.agregarEquipo(equipo);
		} catch (EquipoExistenteException e) {
			e.printStackTrace();
		}
		calculo.update();
		calculo.cargarDatos(listarConexiones());
	}

	public void modificarEquipo(String codigo, Equipo equipoModificado) {
		Equipo equipo = buscarEquipo(codigo);
		red.modificarEquipo(equipo, equipoModificado);
		calculo.update();
		calculo.cargarDatos(listarConexiones());
	}

	public void eliminarEquipo(String codigo) {
		Equipo equipo = buscarEquipo(codigo);
		red.eliminarEquipo(equipo);
		calculo.update();
		calculo.cargarDatos(listarConexiones());
	}

	public TipoPuerto buscarTipoPuerto(String codigo) {

		return red.buscarTipoPuerto(codigo);
	}

	public TipoCable buscarTipoCable(String codigo) {

		return red.buscarTipoCable(codigo);
	}

	public TreeMap<String, Equipo> listarEquipos() {
		return red.getEquipos();
	}

	public List<Conexion> listarConexiones() {
		return red.getConexiones();
	}

	public TreeMap<String, TipoPuerto> listarTipoPuertos() {
		return red.getTiposPuertos();
	}

	public TreeMap<String, TipoCable> listarTipoCables() {
		return red.getTiposCables();
	}

	public TreeMap<String, Ubicacion> listarUbicaciones() {
		return red.getUbicaciones();
	}

	public TreeMap<String, TipoEquipo> listarTipoEquipos() {
		return red.getTiposEquipos();
	}

	public void setEmpresa(Red empresa) {
		this.red = empresa;
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

}
