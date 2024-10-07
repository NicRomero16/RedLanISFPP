package dao.secuencial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.TreeMap;
import java.util.ResourceBundle;

import dao.EquipoDAO;
import modelo.Equipo;
import modelo.TipoEquipo;
import modelo.Ubicacion;
import dao.TipoEquipoDAO;
import dao.UbicacionDAO;

public class EquipoSecue-ncialDAO implements EquipoDAO {

	private TreeMap<String, Equipo> map;
	private String name;
	private TreeMap<String, TipoEquipo> tipoEquipos;
	private TreeMap<String, Ubicacion> ubicaciones;
	private boolean update;

	public EquipoSecuencialDAO() {
		tipoEquipos = cargarTipoEquipos();
		ubicaciones = cargarUbicaciones();
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("equipo");
		update = true;

	}

	private TreeMap<String, Equipo> readFromFile(String file) {

		TreeMap<String, Equipo> equipo = new TreeMap<String, Equipo>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
				// String[] atributos1 = linea.split(",");
				equipo.put(atributos[0], new Equipo(atributos[0],atributos[1],atributos[2],atributos[3],atributos[4],atributos[5]));

			}
			// System.out.println(tipoEquipo.toString());

		} catch (Exception ex) {
			System.out.println("Error al leer el archivo ");
		}
		return equipo;
	}

	private void writeToFile(TreeMap<String, Equipo> map, String file) {
	}

	@Override
	public void insertar(Equipo equipo) {
		map.put(equipo.getCodigo(), equipo);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void actualizar(Equipo equipo) {
		map.put(equipo.getCodigo(), equipo);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void borrar(Equipo equipo) {
		map.remove(equipo.getCodigo());
		writeToFile(map, name);
		update = true;
	}

	@Override
	public TreeMap<String, Equipo> buscarTodos() {
		if (update)
			map = readFromFile(name);
		update = false;
		return map;
	}

	private TreeMap<String, TipoEquipo> cargarTipoEquipos() {
		TreeMap<String, TipoEquipo> tipoEquipo = new TreeMap<String, TipoEquipo>();
		TipoEquipoDAO tipoEquipoDAO = new TipoEquipoSecuencialDAO();
		TreeMap<String, TipoEquipo> ds = tipoEquipoDAO.buscarTodos();
		for (TipoEquipo d : ds.values())
			tipoEquipo.put(d.getCodigo(), d);
		return tipoEquipo;
	}

	private TreeMap<String, Ubicacion> cargarUbicaciones() {
		TreeMap<String, Ubicacion> ubicacion = new TreeMap<String, Ubicacion>();
		UbicacionDAO ubicacionDAO = new UbicacionSecuencialDAO();
		TreeMap<String, Ubicacion> ds = ubicacionDAO.buscarTodos();

		for (Ubicacion d : ds.values())
			ubicacion.put(d.getCodigo(), d);

		return ubicacion;
	}
}
