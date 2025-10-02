package dao;

import java.sql.SQLException;
import java.util.TreeMap;

import modelo.Equipo;

public interface EquipoDAO {
	void insertar(Equipo equipo) throws SQLException ;

	void actualizar(Equipo equipo, Equipo equipoModificado)throws SQLException;

	void borrar(Equipo equipo)throws SQLException;

	TreeMap<String,Equipo> buscarTodos();

}
