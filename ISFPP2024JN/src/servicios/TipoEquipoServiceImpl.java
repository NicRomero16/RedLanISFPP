package servicios;

import java.sql.SQLException;
import java.util.TreeMap;

import dao.FactoryDAO;
import dao.TipoEquipoDAO;
import modelo.TipoEquipo;

public class TipoEquipoServiceImpl implements TipoEquipoService {

	private TipoEquipoDAO tipoEquipoDAO;

	public TipoEquipoServiceImpl() {
		//tipoEquipoDAO = new TipoEquipoSecuencialDAO();
		tipoEquipoDAO = FactoryDAO.getTipoEquipoDAO();
	}

	@Override
	public void insertar(TipoEquipo tipoEquipo) {
		try {
			tipoEquipoDAO.insertar(tipoEquipo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actualizar(TipoEquipo tipoEquipo) {
		try {
			tipoEquipoDAO.actualizar(tipoEquipo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void borrar(TipoEquipo tipoEquipo) {
		try {
			tipoEquipoDAO.borrar(tipoEquipo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public TreeMap<String, TipoEquipo> buscarTodos() {
		return tipoEquipoDAO.buscarTodos();

	}

}
