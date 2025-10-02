package dao;

import java.sql.SQLException;
import java.util.List;

import modelo.Conexion;

public interface ConexionDAO {
	void insertar(Conexion conexion) throws SQLException;

	void actualizar(Conexion conexion)throws SQLException;

	void borrar(Conexion conexion)throws SQLException;

	List<Conexion> buscarTodos();

}
