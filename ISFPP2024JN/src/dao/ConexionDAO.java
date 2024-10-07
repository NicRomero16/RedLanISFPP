package dao;

import java.util.Map;

import modelo.Conexion;

public interface ConexionDAO {
	void insertar(Conexion conexion);

	void actualizar(Conexion conexion);

	void borrar(Conexion conexion);

	Map<String, Conexion> buscarTodos();

}
