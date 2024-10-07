package dao.secuencial;

import java.util.List;

import modelo.TipoPuerto;


public interface TipoPuertoDAO {
	
	void insertar(TipoPuerto tipoPuerto);

	void actualizar(TipoPuerto tipoPuerto);

	void borrar(TipoPuerto tipoPuerto);

	List<TipoPuerto> buscarTodos();

}
