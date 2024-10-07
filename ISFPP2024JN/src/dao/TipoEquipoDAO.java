package dao;

import java.util.Map;

import modelo.TipoEquipo;

public interface TipoEquipoDAO {
	void insertar(TipoEquipo tipoEquipo);

	void actualizar(TipoEquipo tipoEquipo);

	void borrar(TipoEquipo tipoEquipo);

	Map<String, TipoEquipo> buscarTodos();

}
