package aplicacion;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import datos.*;
import modelo.*;

public class TestCompilacion {

	public static void main(String[] args) throws IOException {
		CargarParametros.parametros();

		TreeMap<String, Ubicacion> ubicacion = CargaDatos.cargarUbicacion(CargarParametros.getArchivoUbicacion());
		for (Ubicacion u : ubicacion.values()) {
			System.out.println(u);
		}
		TreeMap<String, TipoEquipo> tipoEquipo = CargaDatos.cargarTipoEquipo(CargarParametros.getArchivoTipoEquipo());
		for (TipoEquipo te : tipoEquipo.values()) {
			System.out.println(te);
		}
		TreeMap<String, Equipo> equipo = CargaDatos.cargarEquipo(CargarParametros.getArchivoEquipo(), ubicacion,
				tipoEquipo);
		for (Equipo e : equipo.values()) {
			System.out.println(e);
		}

		TreeMap<String, TipoCable> tipoCable = CargaDatos.cargarTipoCable(CargarParametros.getArchivoTipoCable());
		for (TipoCable tc : tipoCable.values()) {
			System.out.println(tc);
		}

		List<Conexion> conexiones = CargaDatos.cargarConexiones(CargarParametros.getArchivoConexion(), equipo,
				tipoCable);
		for (Conexion c: conexiones) {
			System.out.println(c);
			System.out.println();
		}
	}
}
