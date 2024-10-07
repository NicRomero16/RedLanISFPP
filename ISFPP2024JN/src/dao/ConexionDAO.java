package dao;

import java.util.TreeMap;

import modelo.Conexion;

public interface ConexionDAO {
	void insertar(Conexion conexion);

	void actualizar(Conexion conexion);

	void borrar(Conexion conexion);

	TreeMap<String, Conexion> buscarTodos();

}
