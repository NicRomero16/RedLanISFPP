package dao.secuencial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.TreeMap;
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

	private TreeMap<String, Equipo> readFromFile(String archivoEquipo) {

		Scanner read;
		TreeMap<String, Equipo> equipo = new TreeMap<String, Equipo>();

		read = new Scanner(new File(archivoEquipo));
		read.useDelimiter("\\s*;\\s*");

		String codigo, descripcion, marca, modelo;
		String[] puertos;
		String[] direccionesIP;
		TipoEquipo tipoEquipo;
		Ubicacion ubicacion;
		boolean estado;
		while (read.hasNext()) {
			codigo = read.next();
			descripcion = read.next();
			marca = read.next();
			modelo = read.next();
			tipoEquipo = tiposEquipos.get(read.next());
			ubicacion = ubicaciones.get(read.next());
			puertos = read.next().split(",");
			direccionesIP = read.next().split(",");
			estado = read.nextBoolean();
			equipo.put(codigo, new Equipo(codigo, descripcion, marca, modelo, tipoEquipo, ubicacion, estado));
			
			for (int i = 0; i < puertos.length; i+=2) {
				
			}
		}
		read.close();	// System.out.println(tipoEquipo.toString());

	return equipo;
	}

	private void writeToFile(TreeMap<String, Equipo> map, String file) {
	}

	@Override
	public void insertar(Equipo equipo) {
		map.put(equipo.getCodigo(), equipo);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void actualizar(Equipo equipo) {
		map.put(equipo.getCodigo(), equipo);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void borrar(Equipo equipo) {
		map.remove(equipo.getCodigo());
		writeToFile(map, name);
		update = true;
	}

	@Override
	public TreeMap<String, Equipo> buscarTodos() {
		if (update)
			map = readFromFile(name);
		update = false;
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
