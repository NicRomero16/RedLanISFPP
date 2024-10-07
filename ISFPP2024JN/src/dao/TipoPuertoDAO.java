package dao;

import java.util.TreeMap;

import modelo.TipoPuerto;


public interface TipoPuertoDAO {
	
	void insertar(TipoPuerto tipoPuerto);

	void actualizar(TipoPuerto tipoPuerto);

	void borrar(TipoPuerto tipoPuerto);

	TreeMap<String,TipoPuerto> buscarTodos();

}
