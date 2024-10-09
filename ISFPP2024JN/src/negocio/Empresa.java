package negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;
import servicios.*;

public class Empresa {

	private static Empresa empresa = null;

	private String nombre;
	private List<Conexion> conexiones;
	private ConexionService conexionService;
	private TreeMap<String, Ubicacion> ubicaciones;
	private UbicacionService ubicacionService;
	private TreeMap<String, Equipo> equipos;
	private EquipoService equipoService;
	private TreeMap<String, TipoEquipo> tiposEquipos;
	private TipoEquipoService tipoEquipoService;
	private TreeMap<String, TipoCable> tiposCables;
	private TipoCableService tipoCableService;
	private TreeMap<String, TipoPuerto> tiposPuertos;
	private TipoPuertoService tipoPuertoService;

	public static Empresa getEmpresa() {
		if (empresa == null) {
			empresa = new Empresa();
		}
		return empresa;
	}

	private Empresa() {
		super();
		conexiones = new ArrayList<Conexion>();
		conexionService = new ConexionServiceImpl();
		conexiones.addAll(conexionService.buscarTodos());
		ubicaciones = new TreeMap<String, Ubicacion>();
		ubicacionService = new UbicacionServiceImpl();
		ubicaciones.putAll(ubicacionService.buscarTodos());
		equipos = new TreeMap<String, Equipo>();
		equipoService = new EquipoServiceImpl();
		equipos.putAll(equipoService.buscarTodos());
		tiposEquipos = new TreeMap<String, TipoEquipo>();
		tipoEquipoService = new TipoEquipoServiceImpl();
		tiposEquipos.putAll(tipoEquipoService.buscarTodos());
		tiposCables = new TreeMap<String, TipoCable>();
		tipoCableService = new TipoCableServiceImpl();
		tiposCables.putAll(tipoCableService.buscarTodos());
		tiposPuertos = new TreeMap<String, TipoPuerto>();
		tipoPuertoService = new TipoPuertoServiceImpl();
		tiposPuertos.putAll(tipoPuertoService.buscarTodos());
	}

}
