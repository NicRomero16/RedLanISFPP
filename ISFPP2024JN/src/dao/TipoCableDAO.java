package dao;

import java.util.Map;

import modelo.TipoCable;

public interface TipoCableDAO {
	void insertar(TipoCable tipoCable);

	void actualizar(TipoCable tipoCable);

	void borrar(TipoCable tipoCable);

	Map<String,TipoCable> buscarTodos();

}
