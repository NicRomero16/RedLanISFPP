package dao;

import java.sql.SQLException;
import java.util.TreeMap;

import modelo.TipoPuerto;


public interface TipoPuertoDAO {
	
	void insertar(TipoPuerto tipoPuerto) throws SQLException;

	void actualizar(TipoPuerto tipoPuerto) throws SQLException;

	void borrar(TipoPuerto tipoPuerto) throws SQLException;

	TreeMap<String,TipoPuerto> buscarTodos();

}