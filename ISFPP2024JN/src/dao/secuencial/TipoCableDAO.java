package dao.secuencial;

import java.util.List;

import modelo.TipoCable;

public interface TipoCableDAO {
	void insertar(TipoCable tipoCable);

	void actualizar(TipoCable tipoCable);

	void borrar(TipoCable tipoCable);

	List<TipoCable> buscarTodos();

}
