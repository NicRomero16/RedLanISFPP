package dao;

import java.sql.SQLException;
import java.util.TreeMap;

import modelo.Ubicacion;

public interface UbicacionDAO {
	void insertar(Ubicacion ubicacion) throws SQLException;

	void actualizar(Ubicacion ubicacion) throws SQLException;

	void borrar(Ubicacion ubicacion) throws SQLException;

	TreeMap<String, Ubicacion> buscarTodos();

}
