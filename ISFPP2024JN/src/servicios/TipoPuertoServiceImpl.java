package servicios;

import java.sql.SQLException;
import java.util.TreeMap;

import dao.FactoryDAO;
import dao.TipoPuertoDAO;
import dao.secuencial.TipoPuertoSecuencialDAO;
import modelo.TipoPuerto;

public class TipoPuertoServiceImpl implements TipoPuertoService {

	private TipoPuertoDAO tipoPuertoDAO;

	public TipoPuertoServiceImpl() {
//		tipoPuertoDAO = new TipoPuertoSecuencialDAO();
		tipoPuertoDAO = FactoryDAO.getTipoPuertoDAO(); 

	}

	@Override
	public void insertar(TipoPuerto tipoPuerto) {
		try {
			tipoPuertoDAO.insertar(tipoPuerto);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actualizar(TipoPuerto tipoPuerto) {
		try {
			tipoPuertoDAO.actualizar(tipoPuerto);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void borrar(TipoPuerto tipoPuerto) {
		try {
			tipoPuertoDAO.borrar(tipoPuerto);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public TreeMap<String, TipoPuerto> buscarTodos() {
		return tipoPuertoDAO.buscarTodos();
	}

}
