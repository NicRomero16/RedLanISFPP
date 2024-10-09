package servicios;

import java.util.TreeMap;

import dao.TipoCableDAO;
import dao.secuencial.TipoCableSecuencialDAO;
import modelo.TipoCable;

public class TipoCableServiceImp implements TipoCableService{

	private TipoCableDAO tipoCableDAO;
	
	public TipoCableServiceImp(){
		tipoCableDAO = new TipoCableSecuencialDAO();
	}
	
	@Override
	public void insertar(TipoCable tipoCable) {
		tipoCableDAO.insertar(tipoCable);				
	}

	@Override
	public void actualizar(TipoCable tipoCable) {
		tipoCableDAO.actualizar(tipoCable);						
	}

	@Override
	public void borrar(TipoCable tipoCable) {
		tipoCableDAO.borrar(tipoCable);
		
	}

	@Override
	public TreeMap<String,TipoCable> buscarTodos() {
		return tipoCableDAO.buscarTodos();
		
	}
}
