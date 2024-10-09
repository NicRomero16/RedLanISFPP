package servicios;

import java.util.TreeMap;

import dao.TipoEquipoDAO;
import dao.secuencial.TipoEquipoSecuencialDAO;
import modelo.TipoEquipo;

public class TipoEquipoServiceImpl implements TipoEquipoService {

	private TipoEquipoDAO tipoEquipoDAO;

	public TipoEquipoServiceImpl() {
		tipoEquipoDAO = new TipoEquipoSecuencialDAO();
	}

	@Override
	public void insertar(TipoEquipo tipoEquipo) {
		tipoEquipoDAO.insertar(tipoEquipo);
	}

	@Override
	public void actualizar(TipoEquipo tipoEquipo) {
		tipoEquipoDAO.actualizar(tipoEquipo);
	}

	@Override
	public void borrar(TipoEquipo tipoEquipo) {
		tipoEquipoDAO.borrar(tipoEquipo);

	}

	@Override
	public TreeMap<String, TipoEquipo> buscarTodos() {
		return tipoEquipoDAO.buscarTodos();

	}

}
