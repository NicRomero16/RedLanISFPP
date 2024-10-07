package dao.secuencial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import dao.TipoCableDAO;
import modelo.TipoCable;

public class TipoCableSecuencialDAO implements TipoCableDAO{

	private Map<String, TipoCable> map;
	private String name;
	private boolean update;// (actualizar)

	public TipoCableSecuencialDAO() {
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		this.name = rb.getString("tipoCable");
		this.update = true;
	}

	private Map<String, TipoCable> readFromFile(String file) {

		TreeMap<String, TipoCable> tipoCable = new TreeMap<String, TipoCable>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
				tipoCable.put(atributos[0],
						new TipoCable(atributos[0], atributos[1], Integer.parseInt(atributos[2])));

			}
			// System.out.println(tipoCable.toString());

		} catch (Exception ex) {
			System.out.println("Error al leer el archivo ");
		}
		return tipoCable;
	}

	private void writeToFile(Map<String, TipoCable> map, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (TipoCable e : map.values()) {
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
	public void insertar(TipoCable tipoCable) {
		map.put(tipoCable.getCodigo(), tipoCable);
		writeToFile(map, name);
		update = true;
	}
	
	@Override
	public void actualizar(TipoCable tipoCable) {
		map.put(tipoCable.getCodigo(), tipoCable);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void borrar(TipoCable tipoCable) {
		map.remove(tipoCable.getCodigo());
		writeToFile(map, name);
		update = true;

	}

	@Override
	public Map<String, TipoCable> buscarTodos() {
		if (update) {
			map = readFromFile(name);
			update = false;
		}
		return map;
	}
}
