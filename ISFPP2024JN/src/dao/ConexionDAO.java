package dao;

import java.util.List;

import modelo.Conexion;

public interface ConexionDAO {
	void insertar(Conexion conexion);

	void actualizar(Conexion conexion);

	void borrar(Conexion conexion);

	List<Conexion> buscarTodos();

}
