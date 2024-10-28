package dao;

import java.util.TreeMap;

import modelo.Equipo;

public interface EquipoDAO {
	void insertar(Equipo equipo) ;

	void actualizar(Equipo equipo, Equipo equipoModificado);

	void borrar(Equipo equipo);

	TreeMap<String,Equipo> buscarTodos();

}
