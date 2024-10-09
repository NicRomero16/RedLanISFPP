package dao.secuencial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import dao.TipoPuertoDAO;
import modelo.TipoPuerto;

public class TipoPuertoSecuencialDAO implements TipoPuertoDAO {

	private TreeMap<String, TipoPuerto> map;
	private String name;
	private boolean update;// (actualizar)

	public TipoPuertoSecuencialDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		this.name = rb.getString("tipoPuerto");
		this.update = true;
	}

	private TreeMap<String, TipoPuerto> readFromFile(String file) {

		TreeMap<String, TipoPuerto> tipoPuerto = new TreeMap<String, TipoPuerto>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
				tipoPuerto.put(atributos[0],
						new TipoPuerto(atributos[0], atributos[1], Integer.parseInt(atributos[2])));

			}
			// System.out.println(tipoPuerto.toString());

		} catch (Exception ex) {
			System.out.println("Error al leer el archivo ");
		}
		return tipoPuerto;
	}

	private void writeToFile(TreeMap<String, TipoPuerto> map, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (TipoPuerto e : map.values()) {
				outFile.format("%s;%s;%d;\n", e.getCodigo(), e.getDescripcion(), e.getVelocidad());
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
	public void insertar(TipoPuerto tipoPuerto) {
		if (!map.containsKey(tipoPuerto.getCodigo()))
			map.put(tipoPuerto.getCodigo(), tipoPuerto);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void actualizar(TipoPuerto tipoPuerto) {
		if (map.containsKey(tipoPuerto.getCodigo()))
			map.put(tipoPuerto.getCodigo(), tipoPuerto);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void borrar(TipoPuerto tipoPuerto) {
		if (map.containsKey(tipoPuerto.getCodigo()))
			map.remove(tipoPuerto.getCodigo());
		writeToFile(map, name);
		update = true;
	}

	@Override
	public TreeMap<String, TipoPuerto> buscarTodos() {
		if (update) {
			map = readFromFile(name);
			update = false;
		}
		return map;
	}
}
