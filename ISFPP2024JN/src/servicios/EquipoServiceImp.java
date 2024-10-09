package servicios;

import java.util.TreeMap;

import modelo.Equipo;
import dao.EquipoDAO;
import dao.secuencial.EquipoSecuencialDAO;

public class EquipoServiceImp implements EquipoService{
	
	private EquipoDAO equipoDAO;
	
	public EquipoServiceImp(){
		equipoDAO = new EquipoSecuencialDAO();
	}
	@Override
	public void insertar(Equipo equipo) {
		equipoDAO.insertar(equipo);				
	}

	@Override
	public void actualizar(Equipo equipo) {
		equipoDAO.actualizar(equipo);						
	}

	@Override
	public void borrar(Equipo equipo) {
		equipoDAO.borrar(equipo);
		
	}

	@Override
	public TreeMap<String,Equipo> buscarTodos() {
		return equipoDAO.buscarTodos();
		
	}

}
