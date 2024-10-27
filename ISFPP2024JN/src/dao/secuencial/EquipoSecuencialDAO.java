package dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.TreeMap;
import java.util.ResourceBundle;
import java.util.Scanner;

import modelo.*;
import dao.*;
import excepciones.EquipoExistenteException;
import excepciones.EquipoInexistenteException;

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
		this.name = rb.getString("equipo");
		this.update = true;
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
				if (marca.isEmpty()) // para evitar problemas cuando lee un campo vacio, le asignamos el valor null
					marca = null;
				String modelo = read.next();
				if (modelo.isEmpty())
					modelo = null;
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

				String marca;
				if (e.getMarca() == null)
					marca = "";
				else
					marca = e.getMarca();

				String modelo;
				if (e.getModelo() == null)
					modelo = "";
				else
					modelo = e.getModelo();
				String ubicacion;
				if (e.getUbicacion() == null)
					ubicacion = "";
				else
					ubicacion = e.getUbicacion().getCodigo();
				String tipoEquipo;
				if (e.getTipoEquipo() == null)
					tipoEquipo = "";
				else 
					tipoEquipo = e.getTipoEquipo().getCodigo();
				

				outFile.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;\n", e.getCodigo(), e.getDescripcion(), marca, modelo,
						tipoEquipo, ubicacion, portTypeFormatter(e), ipAdressFormatter(e),
						String.valueOf(e.getEstado()));
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

	private String ipAdressFormatter(Equipo equipo) {
		StringBuilder sb = new StringBuilder();
		if (equipo.getDireccionesIP().isEmpty() || equipo.getDireccionesIP() == null)
			return "";
		for (String ip : equipo.getDireccionesIP()) {
			sb.append(ip);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private String portTypeFormatter(Equipo equipo) {
		StringBuilder sb = new StringBuilder();
		if (equipo.getPuertos().isEmpty() || equipo.getPuertos() == null)
			return ",0";
		 System.out.println(equipo.getCodigo()+" "+equipo.obtenerCodigoTipoPuerto(0));
		sb.append(equipo.obtenerCodigoTipoPuerto(0));
		sb.append(",");
		sb.append(equipo.getPuertos().size());
		return sb.toString();
	}

	@Override
	public void insertar(Equipo equipo) throws EquipoExistenteException {
		if (map.containsKey(equipo.getCodigo()))
			throw new EquipoExistenteException("El equipo ya existe");
		map.put(equipo.getCodigo(), equipo);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void actualizar(Equipo equipo) throws EquipoInexistenteException {
		if (!map.containsKey(equipo.getCodigo()))
			throw new EquipoInexistenteException("El equipo no existe");
		map.put(equipo.getCodigo(), equipo);
		writeToFile(map, name);
		update = true;
	}

	@Override
	public void borrar(Equipo equipo) throws EquipoInexistenteException {
		if (!map.containsKey(equipo.getCodigo()))
			throw new EquipoInexistenteException("El equipo no existe");
		map.remove(equipo.getCodigo());
		writeToFile(map, name);
		update = true;
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
