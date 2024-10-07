package dao.secuencial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import dao.UbicacionDAO;
import modelo.Ubicacion;

public class UbicacionSecuencialDAO implements UbicacionDAO {

	private Map<String, Ubicacion> map;
	private String name;
	private boolean update;

	public UbicacionSecuencialDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("Ubicacion");
		update = true;
	}

	private Map<String, Ubicacion> readFromFile(String file) {

		TreeMap<String, Ubicacion> ubicacion = new TreeMap<String, Ubicacion>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
				ubicacion.put(atributos[0], new Ubicacion(atributos[0], atributos[1]));

			}
			// System.out.println(ubicacion.toString());

		} catch (Exception ex) {
			System.out.println("Error al leer el archivo ");
		}
		return ubicacion;
	}

	private void writeToFile(Map<String, Ubicacion> map, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Ubicacion e : map.values()) {
				outFile.format("%s;%s;\n", e.getCodigo(), e.getDescripcion());
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.err.println("Error creating file.");
		} catch (FormatterClosedException formatterClosedException) {
			System.err.println("Error writing to file.");
		} finally {
			if (outFile != null)
				outFile.close();
		}
	}

	@Override
	public void insertar(Ubicacion ubicacion) {
		map.put(ubicacion.getCodigo(), ubicacion);
		writeToFile(map, name);
		update = true;}

	@Override
	public void actualizar(Ubicacion ubicacion) {
		map.put(ubicacion.getCodigo(), ubicacion);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void borrar(Ubicacion ubicacion) {
		map.remove(ubicacion.getCodigo(), ubicacion);
		writeToFile(map, name);
		update = true;

	}

	@Override
	public Map<String, Ubicacion> buscarTodos() {
		if (update) {
			map = readFromFile(name);
			update = false;
		}
		return map;
	}
}
