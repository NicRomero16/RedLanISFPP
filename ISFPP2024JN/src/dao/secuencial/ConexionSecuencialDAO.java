package dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;

import dao.ConexionDAO;
import dao.EquipoDAO;
import dao.TipoCableDAO;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;

public class ConexionSecuencialDAO implements ConexionDAO {

	private List<Conexion> list;
	private String name;
	private TreeMap<String, Equipo> equipos;
	private TreeMap<String, TipoCable> tipoCables;
	private boolean update;

	public ConexionSecuencialDAO() {
		equipos = cargarEquipos();
		tipoCables = cargarTipoCables();
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("conexion");
		update = true;
	}

	private List<Conexion> readFromFile(String file) {
		Scanner read;
		List<Conexion> conexiones = new ArrayList<Conexion>();

		try {
			read = new Scanner(new File(file));
			read.useDelimiter("\\s*;\\s*");
			Equipo e1, e2;
			TipoCable tc;

			while (read.hasNext()) {
				e1 = equipos.get(read.next());
				e2 = equipos.get(read.next());
				tc = tipoCables.get(read.next());
				conexiones.add(new Conexion(e1, e2, tc));
			}
			read.close();

		} catch (Exception ex) {
			System.out.println("Error al leer el archivo ");
		}
		return conexiones;

	}

	private void writeToFile(List<Conexion> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Conexion c : list)
				outFile.format("%s;%s;%s\n", c.getEquipo1(), c.getEquipo2(), c.getTipoCable());

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
	public void insertar(Conexion conexion) {
		list.add(conexion);
		writeToFile(list, name);
		update = true;
	}

	@Override
	public void actualizar(Conexion conexion) {
		int pos = list.indexOf(conexion);
		list.set(pos, conexion);
		writeToFile(list, name);
		update = true;
	}

	@Override
	public void borrar(Conexion conexion) {
		list.remove(conexion);
		writeToFile(list, name);
		update = true;
	}

	@Override
	public List<Conexion> buscarTodos() {
		if (update)
			list = readFromFile(name);
		update = false;
		return list;
	}

	private TreeMap<String, Equipo> cargarEquipos() {
		TreeMap<String, Equipo> equipo = new TreeMap<String, Equipo>();
		EquipoDAO equipoDAO = new EquipoSecuencialDAO();
		TreeMap<String, Equipo> ds = equipoDAO.buscarTodos();

		for (Equipo d : ds.values())
			equipo.put(d.getCodigo(), d);

		return equipo;
	}

	private TreeMap<String, TipoCable> cargarTipoCables() {
		TreeMap<String, TipoCable> tipoCable = new TreeMap<String, TipoCable>();
		TipoCableDAO tipoCableDAO = new TipoCableSecuencialDAO();
		TreeMap<String, TipoCable> ds = tipoCableDAO.buscarTodos();

		for (TipoCable d : ds.values())
			tipoCable.put(d.getCodigo(), d);

		return tipoCable;
	}

}
