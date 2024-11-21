package dao.secuencial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;

import excepciones.ArchivoExistenteException;
import excepciones.ArchivoInexisteException;
import dao.UbicacionDAO;
import modelo.Equipo;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;

public class UbicacionSecuencialDAO implements UbicacionDAO {

	private TreeMap<String, Ubicacion> map;
	private String name;
	private boolean update;

	public UbicacionSecuencialDAO() {
		map = new TreeMap<String, Ubicacion>();
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("ubicacion");
		update = true;
	}

	private TreeMap<String, Ubicacion> readFromFile(String file) {

		Scanner read;
		TreeMap<String, Ubicacion> map = new TreeMap<String, Ubicacion>();

		try {
			read = new Scanner(new File(file));
			read.useDelimiter("\\s*;\\s*");

			while (read.hasNext()) {
				String codigo = read.next();
				if (codigo.isEmpty()) // para evitar problemas cuando lee un campo vacio, le asignamos el valor null
					codigo = null;
				String descripcion = read.next();
				if (descripcion.isEmpty())
					descripcion = null;
				Ubicacion ubicacion = new Ubicacion(codigo, descripcion);
				map.put(codigo, ubicacion);
			}
		} catch (Exception ex) {
			System.out.println("Error al leer el archivo");
		}
		return map;
	}

	private void writeToFile(TreeMap<String, Ubicacion> map, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Ubicacion e : map.values()) {
				String codigo;
				if (e.getCodigo() == null)
					codigo = "";
				else
					codigo = e.getCodigo();

				String descripcion;
				if (e.getDescripcion() == null)
					descripcion = "";
				else
					descripcion = e.getDescripcion();
				outFile.format("%s;%s;\n", codigo, descripcion);
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
	public void insertar(Ubicacion ubicacion) throws ArchivoInexisteException {
		if (map.containsKey(ubicacion.getCodigo()))
			throw new ArchivoExistenteException("El tipo de ubicacion existe");
		map.put(ubicacion.getCodigo(), ubicacion);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void actualizar(Ubicacion ubicacion) throws ArchivoInexisteException {
		if (!map.containsKey(ubicacion.getCodigo()))
			throw new ArchivoExistenteException("El tipo de ubicacion no existe");
		map.put(ubicacion.getCodigo(), ubicacion);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void borrar(Ubicacion ubicacion) throws ArchivoInexisteException {
		if (!map.containsKey(ubicacion.getCodigo()))
			throw new ArchivoExistenteException("El tipo de ubicacion ya existe");
		map.remove(ubicacion.getCodigo());
		writeToFile(map, name);
		update = true;
	}

	@Override
	public TreeMap<String, Ubicacion> buscarTodos() {
		if (update) {
			map = readFromFile(name);
			update = false;
		}
		return map;
	}
}
