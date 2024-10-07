package aplicacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import datos.CargarParametros;
import modelo.TipoPuerto;

public class TestCompilacion {

	public static void main(String[] args) throws IOException {
		CargarParametros.parametros();
		
		TreeMap<String, TipoPuerto> tipoPuerto = new TreeMap<String, TipoPuerto>();
		String archivo = CargarParametros.getArchivoTipoPuerto();
		System.out.println(archivo);
		
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] atributos = linea.split(";");
				tipoPuerto.put(atributos[0],
						new TipoPuerto(atributos[0], atributos[1], Integer.parseInt(atributos[2])));
				System.out.println(linea);
			}
			System.out.println(tipoPuerto.toString());

		} catch (Exception ex) {
			System.out.println("Error al leer el archivo ");
		}
	}
}