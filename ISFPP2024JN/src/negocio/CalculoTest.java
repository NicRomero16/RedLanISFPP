package negocio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import controlador.Coordinador;
import excepciones.ConexionInexistenteException;
import excepciones.EquipoExistenteException;
import excepciones.EquipoInexistenteException;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoCable;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;

class CalculoTest {

	private Calculo calculo;
	private Coordinador coordinador;

	@BeforeEach
	void setUp() {
		calculo = new Calculo();
		calculo.setCoordinador(coordinador);
	}

	@Test
	void testCargarDatos() throws EquipoExistenteException {
		List<Conexion> conexiones = new ArrayList<>();

		Equipo equipo1 = new Equipo("RP01", "RPAulas", null, null, new TipoEquipo("RP", "Repetidor"),
				new Ubicacion("A08", "Aula 8"), true);
		Equipo equipo2 = new Equipo("CR01", "CRAulas", null, null, new TipoEquipo("CR", "Controlador de Red"),
				new Ubicacion("A10", "Aula 10"), true);
		Conexion conexion = new Conexion(equipo1, new TipoPuerto("1000M", "100 Mbps", 100), equipo2,
				new TipoPuerto("1000M", "100 Mbps", 100), new TipoCable("", "", 0));

		conexiones.add(conexion);

		Graph<Equipo, Conexion> red = calculo.cargarDatos(conexiones);

		assertNotNull(red);
		assertEquals(2, red.vertexSet().size());
		assertEquals(1, red.edgeSet().size());
	}

	@Test
	void testMostrarEquiposIntermedios() throws Exception {
		Equipo origen = new Equipo("AP01", "APAulas", null, null, new TipoEquipo("AP", "Access Point"),
				new Ubicacion("A01", "Aula 1"), true);

		Equipo intermedio = new Equipo("RT01", "RTAulas", null, null, new TipoEquipo("RT", "Router"),
				new Ubicacion("A02", "Aula 2"), true);

		Equipo destino = new Equipo("SR01", "SRAulas", null, null, new TipoEquipo("SR", "Servidor"),
				new Ubicacion("A04", "Aula 4"), true);

		// Crear conexiones
		List<Conexion> conexiones = new ArrayList<>();
		conexiones.add(new Conexion(origen, new TipoPuerto("1000M", "100 Mbps", 100), intermedio,
				new TipoPuerto("1000M", "100 Mbps", 100), new TipoCable("C6", "UTP Cat. 6", 1000)));
		conexiones.add(new Conexion(intermedio, new TipoPuerto("1000M", "100 Mbps", 100), destino,
				new TipoPuerto("1000M", "100 Mbps", 100), new TipoCable("C6", "UTP Cat. 6", 1000)));

		calculo.cargarDatos(conexiones);

		List<Equipo> intermedios = calculo.mostrarEquiposIntermedios(origen, destino);

		assertNotNull(intermedios);
		assertEquals(1, intermedios.size());
		assertEquals(intermedio, intermedios.get(0));
	}

	@Test
	void testMostrarEquiposIntermediosSinIntermedios() {
		Equipo origen = new Equipo("RP01", "RPAulas", null, null, new TipoEquipo("RP", "Repetidor"),
				new Ubicacion("A08", "Aula 8"), true);
		Equipo destino = new Equipo("SR01", "SRAulas", null, null, new TipoEquipo("SR", "Servidor"),
				new Ubicacion("A04", "Aula 4"), true);

		// Cargar conexiones sin equipos intermedios
		List<Conexion> conexiones = new ArrayList<>();
		conexiones.add(new Conexion(origen, new TipoPuerto("1G", "1Gbps", 1000), destino,
				new TipoPuerto("1G", "1Gbps", 1000), new TipoCable("C6", "UTP Cat. 6", 1000)));

		try {
			calculo.cargarDatos(conexiones);
		} catch (EquipoExistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Exception exception = assertThrows(EquipoInexistenteException.class, () -> {
			calculo.mostrarEquiposIntermedios(origen, destino);
		});

		assertEquals("Los equipos no tienen equipos intermedios", exception.getMessage());
	}

	@Test
	void testVelocidadMaxima() throws Exception {
		// Configurar equipos y conexiones
		Equipo origen = new Equipo("RP01", "RPAulas", null, null, new TipoEquipo("RP", "Repetidor"),
				new Ubicacion("A08", "Aula 8"), true);
		Equipo destino = new Equipo("SR01", "SRAulas", null, null, new TipoEquipo("SR", "Servidor"),
				new Ubicacion("A04", "Aula 4"), true);

		List<Conexion> conexiones = new ArrayList<>();
		conexiones.add(new Conexion(origen, new TipoPuerto("1G", "1Gbps", 1000), null,
				new TipoPuerto("1G", "1Gbps", 1000), new TipoCable("C6", "UTP Cat. 6", 1000)));

		calculo.cargarDatos(conexiones);

		double velocidadMaxima = calculo.velocidadMaxima(origen, destino);

		double velocidadEsperada = 1000; 
		assertEquals(velocidadEsperada, velocidadMaxima);
	}

	@Test
	void testVelocidadMaximaSinConexion() {
		Equipo origen = new Equipo("RP01", "RPAulas", null, null, new TipoEquipo("RP", "Repetidor"),
				new Ubicacion("A08", "Aula 8"), true);
		Equipo destino = new Equipo("SR01", "SRAulas", null, null, new TipoEquipo("SR", "Servidor"),
				new Ubicacion("A04", "Aula 4"), true);

		Exception exception = assertThrows(ConexionInexistenteException.class, () -> {
			calculo.velocidadMaxima(origen, destino);
		});

		assertEquals("No existe una conexion entre los equipos " + origen + " y " + destino, exception.getMessage());
	}
}
