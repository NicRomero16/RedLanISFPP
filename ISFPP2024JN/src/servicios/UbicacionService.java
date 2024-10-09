package servicios;

import java.util.TreeMap;

import modelo.Ubicacion;

public interface UbicacionService {
	void insertar(Ubicacion ubicacion);

	void actualizar(Ubicacion ubicacion);

	void borrar(Ubicacion ubicacion);

	TreeMap<String, Ubicacion> buscarTodos();

}
