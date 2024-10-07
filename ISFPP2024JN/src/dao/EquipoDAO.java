package dao;

import java.util.Map;

import modelo.Equipo;

public interface EquipoDAO {
	void insertar(Equipo equipo);

	void actualizar(Equipo equipo);

	void borrar(Equipo equipo);

	Map<String, Equipo> buscarTodos();

}
