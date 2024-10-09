package servicios;

import java.util.List;

import dao.ConexionDAO;
import dao.secuencial.ConexionSecuencialDAO;
import modelo.Conexion;

public class ConexionServiceImpl implements ConexionService {

	private ConexionDAO conexionDAO;

	public ConexionServiceImpl() {
		conexionDAO = new ConexionSecuencialDAO();
	}

	@Override
	public void insertar(Conexion conexion) {
		conexionDAO.insertar(conexion);
	}

	@Override
	public void actualizar(Conexion conexion) {
		conexionDAO.actualizar(conexion);
	}

	@Override
	public void borrar(Conexion conexion) {
		conexionDAO.borrar(conexion);
	}

	@Override
	public List<Conexion> buscarTodos() {
		return conexionDAO.buscarTodos();
	}

}
