package servicios;

import java.sql.SQLException;
import java.util.List;

import dao.ConexionDAO;
import dao.FactoryDAO;
import modelo.Conexion;

public class ConexionServiceImpl implements ConexionService {

    private ConexionDAO conexionDAO;

    public ConexionServiceImpl() {
        conexionDAO = FactoryDAO.getConexionDAO();
    }

    @Override
    public void insertar(Conexion conexion) {
        try {
			conexionDAO.insertar(conexion);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void actualizar(Conexion conexion) {
        try {
			conexionDAO.actualizar(conexion);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void borrar(Conexion conexion) {
        try {
			conexionDAO.borrar(conexion);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public List<Conexion> buscarTodos() {
        return conexionDAO.buscarTodos();
    }
}
