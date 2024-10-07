package aplicacion;

import java.io.IOException;
import java.util.TreeMap;

import datos.*;
import modelo.Equipo;
import modelo.TipoEquipo;
import modelo.Ubicacion;

public class TestCompilacion {

	public static void main(String[] args) throws IOException {
		CargarParametros.parametros();
	
		TreeMap<String, Ubicacion> ubicacion = CargaDatos.cargarUbicacion(CargarParametros.getArchivoUbicacion());
		for(Ubicacion u: ubicacion.values()) {
			System.out.println(u);
		}
		TreeMap<String, TipoEquipo> tipoEquipo = CargaDatos.cargarTipoEquipo(CargarParametros.getArchivoTipoEquipo());
		for(TipoEquipo te: tipoEquipo.values()) {
			System.out.println(te);
		}
		TreeMap<String, Equipo> equipo = CargaDatos.cargarEquipo(CargarParametros.getArchivoEquipo(),ubicacion,tipoEquipo);
		for(Equipo e: equipo.values()) {
			System.out.println(e);
		}
	}
}