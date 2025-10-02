package dao;

import java.sql.SQLException;
import java.util.TreeMap;

import modelo.TipoCable;

public interface TipoCableDAO {
	void insertar(TipoCable tipoCable) throws SQLException;

	void actualizar(TipoCable tipoCable) throws SQLException;

	void borrar(TipoCable tipoCable) throws SQLException;

	TreeMap<String,TipoCable> buscarTodos();

}
