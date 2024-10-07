package dao.secuencial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;

import dao.ConexionDAO;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;
import modelo.TipoEquipo;

public class ConexionSecuencialDAO implements ConexionDAO {

	private TreeMap<String, Conexion> map;
	private String name;
	private TreeMap<String, Equipo> equipos;
	private TreeMap<String, TipoCable> tipoCable;
	private boolean update;

	private TreeMap<String, Conexion> readFromFile(String file) {
		TreeMap<String, Conexion> conexion = new TreeMap<String, Conexion>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
		
				//conexion.put(atributos[2], new Conexion(atributos[0],atributos[1],atributos[2]));

			}
			// System.out.println(tipoEquipo.toString());

		} catch (Exception ex) {
			System.out.println("Error al leer el archivo ");
		}
		return conexion;
	}
		

	private void writeToFile(TreeMap<String, Conexion> map, String file) {

	}

	@Override
	public void insertar(Conexion conexion) {

	}

	@Override
	public void actualizar(Conexion conexion) {
	}

	@Override
	public void borrar(Conexion conexion) {
	}

	@Override
	public TreeMap<String, Conexion> buscarTodos() {
		if (update)
			map = readFromFile(name);
		update = false;
		return map;
	}

}
