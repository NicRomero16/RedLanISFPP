package dao;

import java.sql.SQLException;
import java.util.TreeMap;

import modelo.TipoEquipo;

public interface TipoEquipoDAO {
	void insertar(TipoEquipo tipoEquipo) throws SQLException;

	void actualizar(TipoEquipo tipoEquipo) throws SQLException;

	void borrar(TipoEquipo tipoEquipo) throws SQLException;

	TreeMap<String, TipoEquipo> buscarTodos();

}
