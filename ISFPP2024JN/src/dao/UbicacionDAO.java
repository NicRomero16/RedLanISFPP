package dao;

import java.util.Map;

import modelo.Ubicacion;

public interface UbicacionDAO {
	void insertar(Ubicacion ubicacion);

	void actualizar(Ubicacion ubicacion);

	void borrar(Ubicacion ubicacion);

	Map<String, Ubicacion> buscarTodos();

}
