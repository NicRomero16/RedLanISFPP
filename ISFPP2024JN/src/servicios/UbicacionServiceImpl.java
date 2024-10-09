package servicios;

import java.util.TreeMap;

import dao.UbicacionDAO;
import dao.secuencial.UbicacionSecuencialDAO;
import modelo.Ubicacion;

public class UbicacionServiceImpl implements UbicacionService {
	private UbicacionDAO ubicacionDAO;

	public UbicacionServiceImpl() {
		ubicacionDAO = new UbicacionSecuencialDAO();
	}

	@Override
	public void insertar(Ubicacion ubicacion) {
		ubicacionDAO.insertar(ubicacion);
	}

	@Override
	public void actualizar(Ubicacion ubicacion) {
		ubicacionDAO.actualizar(ubicacion);
	}

	@Override
	public void borrar(Ubicacion ubicacion) {
		ubicacionDAO.borrar(ubicacion);
	}

	@Override
	public TreeMap<String, Ubicacion> buscarTodos() {
		return ubicacionDAO.buscarTodos();
	}

}
