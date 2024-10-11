package controlador;

import negocio.Calculo;
import negocio.Empresa;
import negocio.EquipoExistenteException;

import java.util.List;
import java.util.TreeMap;

import interfaz.Interfaz;
import modelo.Conexion;
import modelo.Equipo;
import modelo.Ubicacion;

public class Coordinador {
	private Empresa empresa;
	private Calculo calculo;
	private Interfaz interfaz;

	public Empresa getEmpresa() {
		return empresa;
	}
	
	public void iniciar() {
		try {
			calculo.iniciar(empresa.getConexiones());
		} catch (EquipoExistenteException e) {
			System.out.println("pinchó");
			e.printStackTrace();
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

	public TreeMap<String, Ubicacion> listarUbicaciones() {
		return empresa.getUbicaciones();
	}

}
