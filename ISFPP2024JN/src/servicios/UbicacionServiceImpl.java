package servicios;

import java.sql.SQLException;
import java.util.TreeMap;

import dao.FactoryDAO;
import dao.UbicacionDAO;
import dao.secuencial.UbicacionSecuencialDAO;
import modelo.Ubicacion;

public class UbicacionServiceImpl implements UbicacionService {
	private UbicacionDAO ubicacionDAO;

	public UbicacionServiceImpl() {
		//ubicacionDAO = new UbicacionSecuencialDAO();
		ubicacionDAO = FactoryDAO.getUbicacionDAO();

	}

	@Override
	public void insertar(Ubicacion ubicacion) {
		try {
			ubicacionDAO.insertar(ubicacion);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actualizar(Ubicacion ubicacion) {
		try {
			ubicacionDAO.actualizar(ubicacion);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void borrar(Ubicacion ubicacion) {
		try {
			ubicacionDAO.borrar(ubicacion);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public TreeMap<String, Ubicacion> buscarTodos() {
		return ubicacionDAO.buscarTodos();
	}

}
