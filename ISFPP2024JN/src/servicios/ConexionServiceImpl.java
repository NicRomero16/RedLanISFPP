package servicios;

import java.util.List;

import dao.ConexionDAO;
import dao.secuencial.ConexionSecuencialDAO;
import excepciones.ArchivoExistenteException;
import excepciones.ArchivoInexisteException;
import modelo.Conexion;

public class ConexionServiceImpl implements ConexionService {

	private ConexionDAO conexionDAO;

	public ConexionServiceImpl() {
		conexionDAO = new ConexionSecuencialDAO();
	}

	@Override
	public void insertar(Conexion conexion) {
		try {
			conexionDAO.insertar(conexion);
			System.out.println("Conexión insertada exitosamente.");
		} catch (ArchivoExistenteException e) {
			System.err.println("Error: La conexión ya existe. " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error inesperado al insertar conexión: " + e.getMessage());
		}
	}

	@Override
	public void actualizar(Conexion conexion) {
		try {
			conexionDAO.actualizar(conexion);
			System.out.println("Conexión actualizada exitosamente.");
		} catch (ArchivoInexisteException e) {
			System.err.println("Error: La conexión a actualizar no existe. " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error inesperado al actualizar conexión: " + e.getMessage());
		}
	}

	@Override
	public void borrar(Conexion conexion) {
		try {
			conexionDAO.borrar(conexion);
			System.out.println("Conexión borrada exitosamente.");
		} catch (ArchivoInexisteException e) {
			System.err.println("Error: La conexión a borrar no existe. " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error inesperado al borrar conexión: " + e.getMessage());
		}
	}

	@Override
	public List<Conexion> buscarTodos() {
		try {
			return conexionDAO.buscarTodos();
		} catch (Exception e) {
			System.err.println("Error al buscar todas las conexiones: " + e.getMessage());
			return null;
		}
	}

}
