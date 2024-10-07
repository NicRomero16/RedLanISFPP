package dao.secuencial;

import java.util.TreeMap;

import dao.ConexionDAO;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;

public class ConexionSecuencialDAO implements ConexionDAO {

	private TreeMap<String, Conexion> map;
	private String name;
	private TreeMap<String, Equipo> equipos;
	private TreeMap<String, TipoCable> tipoCable;
	private boolean update;

	private TreeMap<String, Conexion> readFromFile(String file) {
		return map;
	}

	private void writeToFile(TreeMap<String, Conexion> map, String file) {

	}

	@Override
	public void insertar(Conexion conexion) {

	}

	@Override
	public void actualizar(Conexion conexion) {
	}

	@Override
	public void borrar(Conexion conexion) {
	}

	@Override
	public TreeMap<String, Conexion> buscarTodos() {
		if (update)
			map = readFromFile(name);
		update = false;
		return map;
	}

}
