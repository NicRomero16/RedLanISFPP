package dao;

import java.util.Map;

import modelo.TipoPuerto;


public interface TipoPuertoDAO {
	
	void insertar(TipoPuerto tipoPuerto);

	void actualizar(TipoPuerto tipoPuerto);

	void borrar(TipoPuerto tipoPuerto);

	Map<String,TipoPuerto> buscarTodos();

}
