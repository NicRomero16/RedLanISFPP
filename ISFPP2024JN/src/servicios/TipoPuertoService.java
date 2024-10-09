package servicios;

import java.util.TreeMap;

import modelo.TipoPuerto;

public interface TipoPuertoService {
	void insertar(TipoPuerto tipoPuerto);

	void actualizar(TipoPuerto tipoPuerto);

	void borrar(TipoPuerto tipoPuerto);

	TreeMap<String, TipoPuerto> buscarTodos();

}
