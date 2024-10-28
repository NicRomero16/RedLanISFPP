package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import org.jgrapht.Graph;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;

public class AplicacionGui extends JFrame {
	private JScrollPane scrollGrande;
	private Coordinador coordinador;
	private JTextArea textAreaGrande;
	private JPanel panelCentral;
	private CardLayout cardLayout;
	private JPanel paneles; // Contenedor para intercambiar paneles
	private JTextArea textAreaGrafo; // Nueva área de texto para mostrar el grafo en la nueva pantalla
	private JPanel panel;
	private static final int ANCHO_VENTANA_PRINCIPAL = 800;
	private static final int LARGO_VENTANA_PRINCIPAL = 600;

	private static final Color NEON_GREEN = new Color(57, 255, 20);
	private static final Color NEON_GRAY = new Color(30, 30, 30);

	public AplicacionGui(Coordinador coordinador) {
		this.coordinador = coordinador;
		// Configuración de la ventana principal
		setResizable(false);
		setTitle("Redes - Neon Style");
		setSize(ANCHO_VENTANA_PRINCIPAL, LARGO_VENTANA_PRINCIPAL); // Resolución: 800x600
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Crear la barra de menú
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.BLACK);
		menuBar.setForeground(NEON_GREEN);

		// Menú principal "Grafo"
		JMenu menuOpciones = new JMenu("Opciones");
		menuOpciones.setForeground(NEON_GREEN);

		// Elementos dentro del menú "grafo"
		JMenuItem itemOpcionesGrafico = new JMenuItem("Grafico");
		definirColoresItems(itemOpcionesGrafico);

		itemOpcionesGrafico.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Cargar el grafo desde el coordinador
				Graph<Equipo, Conexion> grafo = coordinador.cargarDatos();

				// Crear listas de equipos y conexiones
				List<Equipo> equipos = new ArrayList<>(grafo.vertexSet());
				List<Conexion> conexiones = new ArrayList<>(grafo.edgeSet());

				// Crear el panel gráfico con los equipos y conexiones
				GraphPanel graphPanel = new GraphPanel(equipos, conexiones);

				// Reemplazar el contenido del panel con el nuevo panel gráfico
				panel.removeAll();
				panel.add(graphPanel);
				panel.revalidate();
				panel.repaint();

				// Mostrar la pantalla del grafo
				cardLayout.show(paneles, "pantallaGrafico");
			}
		});

		JMenuItem itemOpcionesConsultas = new JMenuItem("Consultas");
		definirColoresItems(itemOpcionesConsultas);
		itemOpcionesConsultas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(paneles, "panelPrincipal");
			}
		});

		// Añadir los elementos al menú "oPCIONES"
		menuOpciones.add(itemOpcionesGrafico);
		menuOpciones.add(itemOpcionesConsultas);

		// Añadir los menús a la barra de menú
		menuBar.add(menuOpciones);

		// Establecer la barra de menú en el frame
		setJMenuBar(menuBar);

		JMenu menuABM = new JMenu("ABM");
		menuABM.setForeground(NEON_GREEN);

		JMenuItem itemABMEquipo = new JMenuItem("Equipos");
		definirColoresItems(itemABMEquipo);
		itemABMEquipo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Crear listas de equipos y conexiones
				TreeMap<String, Equipo> equipos = coordinador.listarEquipos();

				// Crear el panel gráfico con los equipos y conexiones
				ABMEquipos EquipoABM = new ABMEquipos(equipos, coordinador);

				// Reemplazar el contenido del panel con el nuevo panel gráfico
				panel.removeAll();
				panel.add(EquipoABM);
				panel.revalidate();
				panel.repaint();

				// Mostrar la pantalla del grafo
				cardLayout.show(paneles, "pantallaGrafico");
			}
		});

		JMenuItem itemABMConexion = new JMenuItem("Conexiones");
		definirColoresItems(itemABMConexion);
		itemABMConexion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// List<Conexion> conexiones = coordinador.listarConexiones();

				ConexionesABM ConexionABM = new ConexionesABM(coordinador);

				panel.removeAll();
				panel.add(ConexionABM);
				panel.revalidate();
				panel.repaint();
				cardLayout.show(paneles, "pantallaGrafico");
			}
		});

		menuABM.add(itemABMEquipo);
		menuABM.add(itemABMConexion);
		menuBar.add(menuABM);

		// Configuración de CardLayout
		cardLayout = new CardLayout();
		paneles = new JPanel(cardLayout);

		add(paneles);

		// Crear panel original (pantalla principal)
		panelCentral = crearPanelPrincipal(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE);
		paneles.add(panelCentral, "panelPrincipal");

		// Pantalla del grafo
		panel = crearPantallaGrafico(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE);
		paneles.add(panel, "pantallaGrafico");

		// Crear nueva pantalla que se mostrará cuando se presione el botón 2
		JPanel nuevaPantalla = crearNuevaPantalla(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE);
		paneles.add(nuevaPantalla, "nuevaPantalla");

		JPanel panelConexion = crearPanelConexion(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE);
		paneles.add(panelConexion, "panelConexion");

		setJMenuBar(menuBar);
		setVisible(true);
	}

	public void definirColoresItems(JMenuItem item) {
		item.setBackground(Color.BLACK);
		item.setForeground(NEON_GREEN);
	}

	private JPanel crearPanelPrincipal(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {
		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(neonBlack);
		panelCentral.setLayout(null); // Layout nulo para colocar elementos de manera absoluta

		// Area de texto grande (con scroll y borde negro)
		this.textAreaGrande = new JTextArea();
		textAreaGrande.setBackground(neonGray);
		textAreaGrande.setForeground(neonGreen);
		textAreaGrande.setBorder(new LineBorder(neonGreen, 2));
		textAreaGrande.setCaretColor(neonWhite); // Cursor blanco
		textAreaGrande.setEditable(false);

		scrollGrande = new JScrollPane(textAreaGrande);
		scrollGrande.setBounds(20, 70, 500, 400); // Posición y tamaño
		scrollGrande.setBorder(new LineBorder(neonGreen, 2));
		panelCentral.add(scrollGrande);

		// Botones
		JButton botonMostrarEquipos = new JButton("Ver Equipos");
		crearBoton(botonMostrarEquipos);
		botonMostrarEquipos.setBounds((int) (ANCHO_VENTANA_PRINCIPAL / 1.48), (int) (LARGO_VENTANA_PRINCIPAL / 7.9),
				200, 40);
		panelCentral.add(botonMostrarEquipos);

		JButton botonRealizarPingEquipo = new JButton("Realizar Ping a un equipo");
		crearBoton(botonRealizarPingEquipo);
		botonRealizarPingEquipo.setBounds((int) (ANCHO_VENTANA_PRINCIPAL / 1.48), (int) (LARGO_VENTANA_PRINCIPAL / 3.4),
				200, 40);
		panelCentral.add(botonRealizarPingEquipo);

		// Lista de equipos para el JComboBox (se llena con los equipos que tenemos)
		String[] listaDeEquipos = coordinador.devolverEquipoCodigos();

		// Crear el JComboBox con la lista de equipos
		JComboBox<String> comboBoxEquipoPing = new JComboBox<String>(listaDeEquipos);

		crearComboBox(comboBoxEquipoPing);
		comboBoxEquipoPing.setBounds((int) (ANCHO_VENTANA_PRINCIPAL / 1.48), (int) (LARGO_VENTANA_PRINCIPAL / 2.53),
				200, 40); // Posición y tamaño
		panelCentral.add(comboBoxEquipoPing);

		JButton botonVelocidadMaxEntreEquipos = new JButton("Velocidad maxima entre equipos");
		crearBoton(botonVelocidadMaxEntreEquipos);
		botonVelocidadMaxEntreEquipos.setBounds((int) (ANCHO_VENTANA_PRINCIPAL / 1.48),
				(int) (LARGO_VENTANA_PRINCIPAL / 2.1), 200, 40);
		panelCentral.add(botonVelocidadMaxEntreEquipos);

		JComboBox<String> comboBoxVelMaxEquip1 = new JComboBox<>(listaDeEquipos);
		crearComboBox(comboBoxVelMaxEquip1);
		comboBoxVelMaxEquip1.setBounds((int) (ANCHO_VENTANA_PRINCIPAL / 1.48), (int) (LARGO_VENTANA_PRINCIPAL / 1.7),
				200, 40); // Posición y tamaño
		panelCentral.add(comboBoxVelMaxEquip1);

		JComboBox<String> comboBoxVelMaxEquip2 = new JComboBox<>(listaDeEquipos);
		crearComboBox(comboBoxVelMaxEquip2);
		comboBoxVelMaxEquip2.setBounds((int) (ANCHO_VENTANA_PRINCIPAL / 1.48), (int) (LARGO_VENTANA_PRINCIPAL / 1.5),
				200, 40); // Posición y tamaño
		panelCentral.add(comboBoxVelMaxEquip2);

		JButton botonMotrarConexionesGrafo = new JButton("Ver Conexiones");
		crearBoton(botonMotrarConexionesGrafo);
		botonMotrarConexionesGrafo.setBounds((int) (ANCHO_VENTANA_PRINCIPAL / 1.48),
				(int) (LARGO_VENTANA_PRINCIPAL / 4.8), 200, 40);
		panelCentral.add(botonMotrarConexionesGrafo);

		JLabel labelSeleccionarEquipo1 = new JLabel("Selecione los equipos: ");
		labelSeleccionarEquipo1.setForeground(neonGreen);
		labelSeleccionarEquipo1.setBounds((int) (ANCHO_VENTANA_PRINCIPAL / 1.48),
				(int) (LARGO_VENTANA_PRINCIPAL / 1.88), 200, 40); // Posición y tamaño
		panelCentral.add(labelSeleccionarEquipo1);

		JLabel labelSeleccionarEquipo2 = new JLabel("Selecione un equipo: ");
		labelSeleccionarEquipo2.setForeground(neonGreen);
		labelSeleccionarEquipo2.setBounds((int) (ANCHO_VENTANA_PRINCIPAL / 1.48), (int) (LARGO_VENTANA_PRINCIPAL / 2.9),
				200, 40); // Posición y tamaño
		panelCentral.add(labelSeleccionarEquipo2);

		JLabel labelTitulo = new JLabel("Redes");
		labelTitulo.setForeground(neonGreen);
		labelTitulo.setBounds(20, 20, 500, 40); // Ajuste de posición y tamaño
		labelTitulo.setFont(labelTitulo.getFont().deriveFont(30f)); // Tamaño de fuente
		panelCentral.add(labelTitulo);

		JLabel labelSubTitulo = new JLabel("Consultas");
		labelSubTitulo.setForeground(neonGreen);
		labelSubTitulo.setBounds(570, 30, 200, 30); // Ajuste de posición y tamaño
		labelSubTitulo.setFont(labelTitulo.getFont().deriveFont(30f)); // Tamaño de fuente
		panelCentral.add(labelSubTitulo);

		// Acción del visualizar conexiones
		botonMotrarConexionesGrafo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaGrande.setText(coordinador.imprimirConexionesGrafo());
				textAreaGrande.setFont(new Font("Arial", Font.BOLD, 12));
			}
		});

		// Acción del visualizar equipos
		botonMostrarEquipos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaGrande.setText(coordinador.imprimirEquipos());
				textAreaGrande.setFont(new Font("Arial", Font.BOLD, 12));

			}
		});

		// Acción de realizar un ping a un equipo
		botonRealizarPingEquipo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String equipoSelected = (String) comboBoxEquipoPing.getSelectedItem();
				if (equipoSelected != null) {
					boolean ping = coordinador.realizarPingEquipo(equipoSelected);
					if ((ping == true) || (ping == false)) {
						
						textAreaGrande.setText("Estado del equipo: " + ping);
					}
					textAreaGrande.setFont(new Font("Arial", Font.BOLD, 20));
				}
			}
		});

		botonVelocidadMaxEntreEquipos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String equipo1 = (String) comboBoxVelMaxEquip1.getSelectedItem();
				String equipo2 = (String) comboBoxVelMaxEquip2.getSelectedItem();

				double velocidad = coordinador.VelocidadMaximaEntreEquipos(equipo1, equipo2);
				if (velocidad > 0) {
					textAreaGrande.setText("Velocidad: " + velocidad);
				}
				if (velocidad == 0) {
					textAreaGrande.setText("Seleccione equipos diferentes, Porfavor. ");
				}
				if (velocidad == -1) {
					textAreaGrande.setText("el equipo " + equipo1 + " no tiene conexiones en la red.");
				}
				if (velocidad == -2) {
					textAreaGrande.setText("el equipo " + equipo2 + " no tiene conexiones en la red.");
				}

				textAreaGrande.setFont(new Font("Arial", Font.BOLD, 16));
			}
		});
		return panelCentral;
	}

	private JButton crearBoton(JButton boton) {
		boton.setBackground(Color.BLACK);
		boton.setForeground(NEON_GREEN);
		boton.setBorder(new LineBorder(NEON_GREEN, 2));
		return boton;
	}

	private JComboBox<String> crearComboBox(JComboBox<String> comboBox) {
		comboBox.setBackground(NEON_GRAY); // Fondo gris
		comboBox.setForeground(NEON_GREEN); // Texto verde
		comboBox.setBorder(new LineBorder(NEON_GREEN, 2)); // Borde verde
		return comboBox;
	}

	private JPanel crearNuevaPantalla(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {
		JPanel nuevaPantalla = new JPanel();
		nuevaPantalla.setBackground(neonBlack);
		nuevaPantalla.setLayout(null); // Layout nulo para colocación absoluta

		JLabel labelNuevaPantalla = new JLabel("Nueva Pantalla");
		labelNuevaPantalla.setForeground(neonGreen);
		labelNuevaPantalla.setBounds(20, 20, 200, 40); // Posición y tamaño
		nuevaPantalla.add(labelNuevaPantalla);

		// Crear el área de texto para mostrar el grafo
		textAreaGrafo = new JTextArea();
		textAreaGrafo.setBackground(neonGray);
		textAreaGrafo.setForeground(neonGreen);
		textAreaGrafo.setBorder(new LineBorder(neonGreen, 2));
		textAreaGrafo.setCaretColor(neonWhite); // Cursor blanco
		textAreaGrafo.setEditable(false);

		JScrollPane scrollGrafo = new JScrollPane(textAreaGrafo);
		scrollGrafo.setBounds(20, 70, 500, 400); // Posición y tamaño
		scrollGrafo.setBorder(new LineBorder(neonGreen, 2));
		nuevaPantalla.add(scrollGrafo);

		JButton botonVolver = new JButton("Volver");
		botonVolver.setBackground(neonBlack);
		botonVolver.setForeground(neonGreen);
		botonVolver.setBorder(new LineBorder(neonGreen, 2));
		botonVolver.setBounds(540, 390, 200, 40);
		nuevaPantalla.add(botonVolver);

		// Acción del botón de volver
		botonVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Cambiar a la pantalla principal
				cardLayout.show(paneles, "panelPrincipal");
			}
		});

		return nuevaPantalla;
	}

	private JPanel crearPantallaGrafico(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {

		JPanel panel = new JPanel(new BorderLayout());

		return panel;
	}

	private JPanel crearPanelConexion(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {
		JPanel panelConexion = new JPanel();
		panelConexion.setBackground(neonBlack);
		panelConexion.setLayout(null); // Layout nulo para colocación absoluta

		// Crear botones con opciones adicionales
		JButton botonAgregarConexion = new JButton("Agregar Conexion");
		crearBoton(botonAgregarConexion);
		botonAgregarConexion.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, ANCHO_VENTANA_PRINCIPAL / 8, 200, 40);
		panelConexion.add(botonAgregarConexion);

		JButton botonEliminarConexion = new JButton("Eliminar Conexion");
		crearBoton(botonEliminarConexion);
		botonEliminarConexion.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, ANCHO_VENTANA_PRINCIPAL / 5, 200, 40);
		panelConexion.add(botonEliminarConexion);

		JButton botonModificarConexion = new JButton("Modificar Conexion");
		crearBoton(botonModificarConexion);
		botonModificarConexion.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, (int) (ANCHO_VENTANA_PRINCIPAL / 3.6), 200, 40);
		panelConexion.add(botonModificarConexion);

		JButton botonRegresar = new JButton("Regresar");
		crearBoton(botonRegresar);
		botonRegresar.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, ANCHO_VENTANA_PRINCIPAL / 2, 200, 40);
		panelConexion.add(botonRegresar);

		// Acción del botón regresar para volver al panel principal
		botonRegresar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(paneles, "panelPrincipal"); // Regresar al panel principal
			}
		});

		return panelConexion;
	}
}
