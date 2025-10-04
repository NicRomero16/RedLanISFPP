package negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import excepciones.ConexionExistenteException;
import excepciones.ConexionInexistenteException;
import excepciones.EquipoExistenteException;
import excepciones.EquipoInexistenteException;
import excepciones.UbicacionInexistenteException;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;
import servicios.ConexionService;
import servicios.ConexionServiceImpl;
import servicios.EquipoService;
import servicios.EquipoServiceImpl;
import servicios.TipoCableService;
import servicios.TipoCableServiceImpl;
import servicios.TipoEquipoService;
import servicios.TipoEquipoServiceImpl;
import servicios.TipoPuertoService;
import servicios.TipoPuertoServiceImpl;
import servicios.UbicacionService;
import servicios.UbicacionServiceImpl;

public class Red {

	private static Red red = null;

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

	private Red() {
		super();
		tiposEquipos = new TreeMap<String, TipoEquipo>();
		tipoEquipoService = new TipoEquipoServiceImpl();
		tiposEquipos.putAll(tipoEquipoService.buscarTodos());

		tiposCables = new TreeMap<String, TipoCable>();
		tipoCableService = new TipoCableServiceImpl();
		tiposCables.putAll(tipoCableService.buscarTodos());

		tiposPuertos = new TreeMap<String, TipoPuerto>();
		tipoPuertoService = new TipoPuertoServiceImpl();
		tiposPuertos.putAll(tipoPuertoService.buscarTodos());

		ubicaciones = new TreeMap<String, Ubicacion>();
		ubicacionService = new UbicacionServiceImpl();
		ubicaciones.putAll(ubicacionService.buscarTodos());

		equipos = new TreeMap<String, Equipo>();
		equipoService = new EquipoServiceImpl();
		equipos.putAll(equipoService.buscarTodos());

		conexiones = new ArrayList<Conexion>();
		conexionService = new ConexionServiceImpl();
		conexiones.addAll(conexionService.buscarTodos());

		// Si cambiamos a Postgres y la BD está vacía, mantenemos estructuras vacías.
		// Refrescar una vez para asegurar coherencia inicial.
		refrescarDesdeBD();

	}

	public void agregarEquipo(Equipo equipo) throws EquipoExistenteException {
		if (equipos.containsKey(equipo.getCodigo()))
			throw new EquipoExistenteException("El equipo ya existe");
		equipos.put(equipo.getCodigo(), equipo);
		equipoService.insertar(equipo);
	}

	public void modificarEquipo(Equipo equipo, Equipo equipoModificado) {
        // Actualiza en BD
        equipoService.actualizar(equipo, equipoModificado);

        // Mantén la misma instancia para preservar referencias en conexiones
		Equipo actual = equipos.get(equipo.getCodigo());
		if (actual != null) {
			boolean pkCambia = !equipo.getCodigo().equals(equipoModificado.getCodigo());
			if (pkCambia) {
				// Mueve la entrada del mapa a la nueva clave pero conserva la MISMA instancia
				equipos.remove(equipo.getCodigo());
				actual.setCodigo(equipoModificado.getCodigo());
				equipos.put(actual.getCodigo(), actual);
			}

			actual.setDescripcion(equipoModificado.getDescripcion());
			actual.setMarca(equipoModificado.getMarca());
			actual.setModelo(equipoModificado.getModelo());
			actual.setTipoEquipo(equipoModificado.getTipoEquipo());
			actual.setUbicacion(equipoModificado.getUbicacion());
			actual.setEstado(equipoModificado.getEstado());

			// Reflejar puertos/IPs modificados:
			actual.getPuertos().clear();
			actual.getPuertos().addAll(equipoModificado.getPuertos());
			actual.getDireccionesIP().clear();
			actual.getDireccionesIP().addAll(equipoModificado.getDireccionesIP());
		}
    }

	public void eliminarEquipo(Equipo equipo) {
		buscarEquipo(equipo.getCodigo());
		equipos.remove(equipo.getCodigo());
		equipoService.borrar(equipo);
	}

	public Equipo buscarEquipo(String codigo) throws EquipoInexistenteException {
		if (!equipos.containsKey(codigo))
			throw new EquipoInexistenteException("El equipo no existe");
		return equipos.get(codigo);
	}

	public void agregarConexion(Conexion conexion) throws ConexionExistenteException {
		if (conexiones.contains(conexion))
			throw new ConexionExistenteException("La conexion ya existe");
		conexionService.insertar(conexion);
		conexiones.add(conexion);
	}

	public void modificarConexion(Conexion conexion) throws ConexionInexistenteException {
		int pos = conexiones.indexOf(conexion);
		if (pos == -1)
			throw new ConexionInexistenteException("La conexion no existe");
		conexiones.set(pos, conexion);
		conexionService.actualizar(conexion);
	}

	public void borrarConexion(Conexion conexion) throws ConexionInexistenteException {
		Conexion emp = buscarConexion(conexion);
		conexiones.remove(emp);
		conexionService.borrar(conexion);
	}

	public Conexion buscarConexion(Conexion conexion) throws ConexionInexistenteException {
		int pos = conexiones.indexOf(conexion);
		if (pos == -1)
			throw new ConexionInexistenteException("La conexion no existe");
		return conexiones.get(pos);
	}

	public TipoPuerto buscarTipoPuerto(String codigo) {
		if (!tiposPuertos.containsKey(codigo))
			throw new EquipoInexistenteException("El tipo puerto no existe");
		return tiposPuertos.get(codigo);
	}

	public TipoCable buscarTipoCable(String codigo) {
		if (!tiposCables.containsKey(codigo))
			throw new EquipoInexistenteException("El tipo cable no existe");
		return tiposCables.get(codigo);
	}

	public TipoEquipo buscarTipoEquipo(String codigo) {
		return tiposEquipos.get(codigo);
	}

	public void agregarUbicacion(Ubicacion ubicacion) throws EquipoExistenteException {
		if (ubicaciones.containsKey(ubicacion.getCodigo()))
			throw new EquipoExistenteException("La ubicacion ya existe");
		ubicaciones.put(ubicacion.getCodigo(), ubicacion);
		ubicacionService.insertar(ubicacion);
	}
	
	public void eliminarUbicacion(Ubicacion ubicacion) {
		buscarUbicacion(ubicacion.getCodigo());
		ubicaciones.remove(ubicacion.getCodigo());
		ubicacionService.borrar(ubicacion);
	}
	
	public Ubicacion buscarUbicacion(String codigo) {
		if (!ubicaciones.containsKey(codigo))
			throw new UbicacionInexistenteException("La ubicacion no existe");
		return ubicaciones.get(codigo);
	}

	public static Red getRed() {
		if (red == null) {
			red = new Red();
		}
		return red;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Conexion> getConexiones() {
		return conexiones;
	}

	public TreeMap<String, Ubicacion> getUbicaciones() {
		return ubicaciones;
	}

	public TreeMap<String, Equipo> getEquipos() {
		return equipos;
	}

	public TreeMap<String, TipoEquipo> getTiposEquipos() {
		return tiposEquipos;
	}

	public TreeMap<String, TipoCable> getTiposCables() {
		return tiposCables;
	}

	public TreeMap<String, TipoPuerto> getTiposPuertos() {
		return tiposPuertos;
	}

	public synchronized void refrescarDesdeBD() {
        tiposEquipos.clear(); tiposEquipos.putAll(tipoEquipoService.buscarTodos());
        tiposCables.clear();  tiposCables.putAll(tipoCableService.buscarTodos());
        tiposPuertos.clear(); tiposPuertos.putAll(tipoPuertoService.buscarTodos());
        ubicaciones.clear();  ubicaciones.putAll(ubicacionService.buscarTodos());
        equipos.clear();      equipos.putAll(equipoService.buscarTodos());
        conexiones.clear();   conexiones.addAll(conexionService.buscarTodos());
    }
}
