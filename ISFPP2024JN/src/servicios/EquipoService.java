package servicios;

import java.util.TreeMap;
import modelo.Equipo;

public interface EquipoService {
	void insertar(Equipo equipo);

	void actualizar(Equipo equipo);

	void borrar(Equipo equipo);

	TreeMap<String,Equipo> buscarTodos();
}
