package dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.TreeMap;

import Excepciones.ArchivoExistenteException;
import Excepciones.ArchivoInexisteException;

import java.util.ResourceBundle;
import java.util.Scanner;
import modelo.*;
import dao.*;

public class EquipoSecuencialDAO implements EquipoDAO {

	private TreeMap<String, Equipo> map;
	private String name;
	private TreeMap<String, TipoEquipo> tipoEquipos;
	private TreeMap<String, Ubicacion> ubicaciones;
	private TreeMap<String, TipoPuerto> tipoPuertos;
	private boolean update;

	public EquipoSecuencialDAO() {
		tipoEquipos = cargarTipoEquipos();
		ubicaciones = cargarUbicaciones();
		tipoPuertos = cargarTipoPuerto();
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("equipo");
		update = true;

	}

	private TreeMap<String, Equipo> readFromFile(String file) {

		Scanner read;
		TreeMap<String, Equipo> map = new TreeMap<String, Equipo>();

		try {
			read = new Scanner(new File(file));
			read.useDelimiter("\\s*;\\s*");

			while (read.hasNext()) {
				String codigo = read.next();
				String descripcion = read.next();
				String marca = read.next();
				String modelo = read.next();
				TipoEquipo tipoEquipo = tipoEquipos.get(read.next());
				Ubicacion ubicacion = ubicaciones.get(read.next());
				String[] puertos = read.next().split(",");
				String[] direccionesIP = read.next().split(",");
				Boolean estado = read.nextBoolean();
				Equipo equipo = new Equipo(codigo, descripcion, marca, modelo, tipoEquipo, ubicacion, estado);

				for (int i = 0; i < puertos.length; i += 2) {
					TipoPuerto tipoPuerto = tipoPuertos.get(puertos[i]);
					int cantidad = Integer.parseInt(puertos[i + 1]);
					equipo.agregarPuerto(cantidad, tipoPuerto);
				}
				Collections.addAll(equipo.getDireccionesIP(), direccionesIP);
				map.put(codigo, equipo);
			}
		} catch (Exception ex) {
			System.out.println("Error al leer el archivo");
		}
		return map;
	}

	private void writeToFile(TreeMap<String, Equipo> map, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Equipo e : map.values()) {
				outFile.format("%s;%s;%s;%s;%s;%s;%s;%s;\n", e.getCodigo(), e.getDescripcion(), e.getMarca(),
						e.getModelo(), e.getTipoEquipo().toString(), e.getUbicacion().toString(),
						e.getPuertos().toString(), e.getDireccionesIP().toString());
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.err.println("Error creating file.");
			return;
		} catch (FormatterClosedException formatterClosedException) {
			System.err.println("Error writing to file.");
			return;
		} finally {
			if (outFile != null)
				outFile.close();
		}
		update = true;
	}

	@Override
	public void insertar(Equipo equipo) throws ArchivoExistenteException {
		if (map.containsKey(equipo.getCodigo()))
			throw new ArchivoExistenteException("El equipo ya existe");
		map.put(equipo.getCodigo(), equipo);
		writeToFile(map, name);
	}

	@Override
	public void actualizar(Equipo equipo) throws ArchivoInexisteException {
		if (!map.containsKey(equipo.getCodigo()))
			throw new ArchivoInexisteException("El equipo no existe");
		map.put(equipo.getCodigo(), equipo);
		writeToFile(map, name);
	}

	@Override
	public void borrar(Equipo equipo) throws ArchivoInexisteException {
		if (!map.containsKey(equipo.getCodigo()))
			throw new ArchivoInexisteException("El equipo no existe");
		map.remove(equipo.getCodigo());
		writeToFile(map, name);
	}

	@Override
	public TreeMap<String, Equipo> buscarTodos() {
		if (update) {
			map = readFromFile(name);
			update = false;
		}
		return map;
	}

	private TreeMap<String, TipoEquipo> cargarTipoEquipos() {
		TreeMap<String, TipoEquipo> tipoEquipo = new TreeMap<String, TipoEquipo>();
		TipoEquipoDAO tipoEquipoDAO = new TipoEquipoSecuencialDAO();
		TreeMap<String, TipoEquipo> ds = tipoEquipoDAO.buscarTodos();
		for (TipoEquipo d : ds.values())
			tipoEquipo.put(d.getCodigo(), d);
		return tipoEquipo;
	}

	private TreeMap<String, Ubicacion> cargarUbicaciones() {
		TreeMap<String, Ubicacion> ubicacion = new TreeMap<String, Ubicacion>();
		UbicacionDAO ubicacionDAO = new UbicacionSecuencialDAO();
		TreeMap<String, Ubicacion> ds = ubicacionDAO.buscarTodos();

		for (Ubicacion d : ds.values())
			ubicacion.put(d.getCodigo(), d);

		return ubicacion;
	}

	private TreeMap<String, TipoPuerto> cargarTipoPuerto() {
		TreeMap<String, TipoPuerto> tipoPuerto = new TreeMap<String, TipoPuerto>();
		TipoPuertoDAO tipoPuertoDAO = new TipoPuertoSecuencialDAO();
		TreeMap<String, TipoPuerto> ds = tipoPuertoDAO.buscarTodos();

		for (TipoPuerto tp : ds.values())
			tipoPuerto.put(tp.getCodigo(), tp);

		return tipoPuerto;
	}
}
