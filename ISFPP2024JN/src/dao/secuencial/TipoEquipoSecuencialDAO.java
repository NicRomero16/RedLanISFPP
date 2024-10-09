package dao.secuencial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import dao.TipoEquipoDAO;
import excepciones.ArchivoExistenteException;
import excepciones.ArchivoInexisteException;
import modelo.TipoEquipo;

public class TipoEquipoSecuencialDAO implements TipoEquipoDAO {

	private TreeMap<String, TipoEquipo> map;
	private String name;
	private boolean update;

	public TipoEquipoSecuencialDAO() {
		map = new TreeMap<String, TipoEquipo>();
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("tipoEquipo");
		update = true;
	}

	private TreeMap<String, TipoEquipo> readFromFile(String file) {

		TreeMap<String, TipoEquipo> tipoEquipo = new TreeMap<String, TipoEquipo>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
				tipoEquipo.put(atributos[0], new TipoEquipo(atributos[0], atributos[1]));

			}
			// System.out.println(tipoEquipo.toString());

		} catch (Exception ex) {
			System.out.println("Error al leer el archivo ");
		}
		return tipoEquipo;
	}

	private void writeToFile(TreeMap<String, TipoEquipo> map, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (TipoEquipo e : map.values()) {
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
	public void insertar(TipoEquipo tipoEquipo) throws ArchivoExistenteException {
		if (map.containsKey(tipoEquipo.getCodigo()))
			throw new ArchivoExistenteException("El tipo de equipo ya existe");
		map.put(tipoEquipo.getCodigo(), tipoEquipo);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void actualizar(TipoEquipo tipoEquipo) throws ArchivoInexisteException {
		if (!map.containsKey(tipoEquipo.getCodigo()))
			throw new ArchivoInexisteException("El tipo de equipo no existe");
		map.put(tipoEquipo.getCodigo(), tipoEquipo);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void borrar(TipoEquipo tipoEquipo) throws ArchivoInexisteException {
		if (!map.containsKey(tipoEquipo.getCodigo()))
			throw new ArchivoInexisteException("El tipo de equipo no existe");
		map.remove(tipoEquipo.getCodigo());
		writeToFile(map, name);
		update = true;
	}

	@Override
	public TreeMap<String, TipoEquipo> buscarTodos() {
		if (update) {
			map = readFromFile(name);
			update = false;
		}
		return map;
	}
}
