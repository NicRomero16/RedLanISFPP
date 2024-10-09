package servicios;

import java.util.TreeMap;

import dao.TipoPuertoDAO;
import dao.secuencial.TipoPuertoSecuencialDAO;
import modelo.TipoPuerto;

public class TipoPuertoServiceImpl implements TipoPuertoService {

	private TipoPuertoDAO tipoPuertoDAO;

	public TipoPuertoServiceImpl() {
		tipoPuertoDAO = new TipoPuertoSecuencialDAO();
	}

	@Override
	public void insertar(TipoPuerto tipoPuerto) {
		tipoPuertoDAO.insertar(tipoPuerto);
	}

	@Override
	public void actualizar(TipoPuerto tipoPuerto) {
		tipoPuertoDAO.actualizar(tipoPuerto);
	}

	@Override
	public void borrar(TipoPuerto tipoPuerto) {
		tipoPuertoDAO.borrar(tipoPuerto);
	}

	@Override
	public TreeMap<String, TipoPuerto> buscarTodos() {
		return tipoPuertoDAO.buscarTodos();
	}

}
