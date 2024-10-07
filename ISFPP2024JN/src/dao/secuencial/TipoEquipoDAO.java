package dao.secuencial;

import java.util.List;

import modelo.TipoEquipo;

public interface TipoEquipoDAO {
	void insertar(TipoEquipo tipoEquipo);

	void actualizar(TipoEquipo tipoEquipo);

	void borrar(TipoEquipo tipoEquipo);

	List<TipoEquipo> buscarTodos();

}
