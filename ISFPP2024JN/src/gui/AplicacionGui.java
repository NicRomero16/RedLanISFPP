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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jgrapht.Graph;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;

@SuppressWarnings("serial")
public class AplicacionGui extends JFrame {
	private JScrollPane scrollGrande;
	private Coordinador coordinador;
	private JPanel panelCentral;
	private CardLayout cardLayout;
	private JPanel paneles;
	private JTextArea textAreaGrafo;
	private JPanel panel;
	private JPanel nuevaPantalla;
	private JPanel nuevaPantalla2;

	private static final Color NEON_GREEN = new Color(57, 255, 20);
	private static final Color NEON_GRAY = new Color(30, 30, 30);

	public AplicacionGui(Coordinador coordinador) {
		this.coordinador = coordinador;
		cardLayout = new CardLayout();
		paneles = new JPanel(cardLayout);
		setResizable(false);
		setTitle("Redes - Neon Style");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		definirColoresItems(menuBar);

		JMenu menuOpciones = new JMenu("Opciones");
		definirColoresItems(menuOpciones);

		JMenuItem itemOpcionesConsultas = new JMenuItem("Consultas");
		definirColoresItems(itemOpcionesConsultas);

		JMenuItem itemOpcionesGrafico = new JMenuItem("Grafico");
		definirColoresItems(itemOpcionesGrafico);

		JMenu menuABM = new JMenu("ABM");
		definirColoresItems(menuABM);

		JMenuItem itemABMEquipo = new JMenuItem("Equipos");
		definirColoresItems(itemABMEquipo);

		JMenuItem itemABMConexion = new JMenuItem("Conexiones");
		definirColoresItems(itemABMConexion);

		itemOpcionesGrafico.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Graph<Equipo, Conexion> grafo = coordinador.cargarDatos();

				List<Equipo> equipos = new ArrayList<>(grafo.vertexSet());
				List<Conexion> conexiones = new ArrayList<>(grafo.edgeSet());

				GraphPanel graphPanel = new GraphPanel(equipos, conexiones);

				panel.removeAll();
				panel.add(graphPanel);
				panel.revalidate();
				panel.repaint();

				cardLayout.show(paneles, "pantallaGrafico");
			}
		});

		itemOpcionesConsultas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(paneles, "panelPrincipal");
			}
		});

		itemABMEquipo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				TreeMap<String, Equipo> equipos = coordinador.listarEquipos();

				ABMEquipos EquipoABM = new ABMEquipos(equipos, coordinador);

				panel.removeAll();
				panel.add(EquipoABM);
				panel.revalidate();
				panel.repaint();

				cardLayout.show(paneles, "pantallaGrafico");
			}
		});

		itemABMConexion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ConexionesABM ConexionABM = new ConexionesABM(coordinador);

				panel.removeAll();
				panel.add(ConexionABM);
				panel.revalidate();
				panel.repaint();
				cardLayout.show(paneles, "pantallaGrafico");
			}
		});

		menuBar.add(menuOpciones);
		menuOpciones.add(itemOpcionesConsultas);
		menuOpciones.add(itemOpcionesGrafico);
		menuABM.add(itemABMEquipo);
		menuABM.add(itemABMConexion);
		menuBar.add(menuABM);
		setJMenuBar(menuBar);

		panelCentral = crearPanelPrincipal(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE);
		panel = crearPantallaGrafico(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE);
		nuevaPantalla = crearNuevaPantalla(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE);
		add(paneles);

		paneles.add(panelCentral, "panelPrincipal");
		paneles.add(panel, "pantallaGrafico");
		paneles.add(nuevaPantalla, "nuevaPantalla");

		setVisible(true);
	}

	private JPanel crearPanelPrincipal(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {
		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(neonBlack);
		panelCentral.setLayout(null);

		JTextArea textAreaGrande = new JTextArea();
		textAreaGrande.setBackground(neonGray);
		textAreaGrande.setForeground(neonGreen);
		textAreaGrande.setBorder(new LineBorder(neonGreen, 2));
		textAreaGrande.setCaretColor(neonWhite);
		textAreaGrande.setEditable(false);

		scrollGrande = new JScrollPane(textAreaGrande);
		scrollGrande.setBounds(20, 70, 500, 400);
		scrollGrande.setBorder(new LineBorder(neonGreen, 2));
		panelCentral.add(scrollGrande);
		String[] listaDeEquipos = coordinador.devolverEquipoCodigos();

		JButton botonMostrarEquipos = new JButton("Ver Equipos");
		crearBoton(panelCentral, botonMostrarEquipos);
		botonMostrarEquipos.setBounds(540, 76, 200, 40);

		JButton botonRealizarPingEquipo = new JButton("Realizar Ping a un equipo");
		crearBoton(panelCentral, botonRealizarPingEquipo);
		botonRealizarPingEquipo.setBounds(540, 176, 200, 40);

		JComboBox<String> comboBoxEquipoPing = new JComboBox<String>(listaDeEquipos);
		crearComboBox(panelCentral, comboBoxEquipoPing);
		comboBoxEquipoPing.setBounds(540, 236, 200, 40);

		JButton botonVelocidadMaxEntreEquipos = new JButton("Velocidad maxima entre equipos");
		crearBoton(panelCentral, botonVelocidadMaxEntreEquipos);
		botonVelocidadMaxEntreEquipos.setBounds(540, 286, 200, 40);

		JComboBox<String> comboBoxVelMaxEquip1 = new JComboBox<>(listaDeEquipos);
		crearComboBox(panelCentral, comboBoxVelMaxEquip1);
		comboBoxVelMaxEquip1.setBounds(540, 350, 200, 40);

		JComboBox<String> comboBoxVelMaxEquip2 = new JComboBox<>(listaDeEquipos);
		crearComboBox(panelCentral, comboBoxVelMaxEquip2);
		comboBoxVelMaxEquip2.setBounds(540, 400, 200, 40);

		JButton botonMotrarConexionesGrafo = new JButton("Ver Conexiones");
		crearBoton(panelCentral, botonMotrarConexionesGrafo);
		botonMotrarConexionesGrafo.setBounds(540, 125, 200, 40);

		JButton botonMotrarMasConsultas = new JButton("Mas consultas");
		crearBoton(panelCentral, botonMotrarMasConsultas);
		botonMotrarMasConsultas.setBounds(540, 450, 200, 40);

		JLabel labelTitulo = new JLabel("Redes");
		labelTitulo.setForeground(neonGreen);
		labelTitulo.setBounds(20, 20, 500, 40);
		labelTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JLabel labelSubTitulo = new JLabel("Consultas");
		labelSubTitulo.setForeground(neonGreen);
		labelSubTitulo.setBounds(570, 30, 200, 30);
		labelSubTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JLabel labelSeleccionarUnEquipo = new JLabel("Selecione un equipo: ");
		labelSeleccionarUnEquipo.setForeground(neonGreen);
		labelSeleccionarUnEquipo.setBounds(540, 206, 200, 40);

		JLabel labelSeleccionarEquipos = new JLabel("Selecione los equipos: ");
		labelSeleccionarEquipos.setForeground(neonGreen);
		labelSeleccionarEquipos.setBounds(540, 320, 200, 40);

		JButton botonSalir = new JButton("Salir");
		crearBoton(panelCentral, botonSalir);
		botonSalir.setBounds(50, 490, 100, 30);

		botonSalir.addActionListener(ev -> {
			int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas salir?", "Confirmar salida",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (confirm == JOptionPane.YES_OPTION)
				System.exit(0); // Termina la aplicación

		});

		botonMotrarConexionesGrafo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaGrande.setText(coordinador.imprimirConexionesGrafo());
				textAreaGrande.setFont(new Font("Arial", Font.BOLD, 12));
			}
		});

		botonMostrarEquipos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaGrande.setText(coordinador.imprimirEquipos());
				textAreaGrande.setFont(new Font("Arial", Font.BOLD, 12));

			}
		});

		botonRealizarPingEquipo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String equipoSelected = (String) comboBoxEquipoPing.getSelectedItem();

				boolean ping = coordinador.realizarPingEquipo(equipoSelected);

				if (ping) {
					textAreaGrande.setText("Estado del " + equipoSelected + ": Conectado");

				} else {
					textAreaGrande.setText("Estado del " + equipoSelected + ": Desconectado");
				}

				textAreaGrande.setFont(new Font("Arial", Font.BOLD, 20));
			}
		});

		botonVelocidadMaxEntreEquipos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String equipo1 = (String) comboBoxVelMaxEquip1.getSelectedItem();
				String equipo2 = (String) comboBoxVelMaxEquip2.getSelectedItem();

				double velocidad = coordinador.velocidadMaximaEntreEquipos(equipo1, equipo2);

				if (velocidad == -1) {
					mostrarMensaje("Por favor, seleccione equipos diferentes.");
					return;
				}
				if (velocidad == -2) {
					mostrarMensaje("Uno o ambos equipos no tienen conexiones activas en la red.");
					return;
				}
				if (velocidad == -3) {
					mostrarMensaje("No hay un camino entre los equipos.");
					return;
				}
				if (velocidad == -4) {
					mostrarMensaje("Error: uno o más puertos o cables tienen velocidad nula.");
					return;
				}
				if (velocidad == -5) {
					mostrarMensaje("No hay conexiones activas entre los equipos seleccionados.");
					return;
				}
				if (velocidad <= 0) {
					mostrarMensaje("Error inesperado al calcular la velocidad máxima.");
					return;
				}

				mostrarMensaje("Velocidad máxima entre equipos: " + velocidad + " Mbps");
			}

			/**
			 * Muestra un mensaje en el área de texto grande. Este método se utiliza para
			 * mostrar errores o la velocidad máxima calculada entre los equipos
			 * seleccionados.
			 *
			 * @param mensaje El mensaje a mostrar al usuario, que puede ser un error o la
			 *                velocidad máxima en Mbps.
			 */
			private void mostrarMensaje(String mensaje) {
				textAreaGrande.setText(mensaje);
				textAreaGrande.setFont(new Font("Arial", Font.BOLD, 16));
			}
		});

		botonMotrarMasConsultas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cardLayout = (CardLayout) paneles.getLayout();
				cardLayout.show(paneles, "nuevaPantalla");
			}
		});

		panelCentral.add(labelTitulo);
		panelCentral.add(labelSubTitulo);
		panelCentral.add(labelSeleccionarEquipos);
		panelCentral.add(labelSeleccionarUnEquipo);

		return panelCentral;
	}

	private JPanel crearNuevaPantalla(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {
		JPanel nuevaPantalla = new JPanel();
		nuevaPantalla.setBackground(neonBlack);
		nuevaPantalla.setLayout(null);

		textAreaGrafo = new JTextArea();
		textAreaGrafo.setBackground(neonGray);
		textAreaGrafo.setForeground(neonGreen);
		textAreaGrafo.setBorder(new LineBorder(neonGreen, 2));
		textAreaGrafo.setCaretColor(neonWhite);
		textAreaGrafo.setEditable(false);

		JScrollPane scrollGrafo = new JScrollPane(textAreaGrafo);
		scrollGrafo.setBounds(20, 70, 500, 400);
		scrollGrafo.setBorder(new LineBorder(neonGreen, 2));

		String[] listaDeEquipos = coordinador.devolverEquipoCodigos();

		JButton botonMostrarEquipos = new JButton("ACA pone los metodos");
		crearBoton(nuevaPantalla, botonMostrarEquipos);
		botonMostrarEquipos.setBounds(540, 76, 200, 40);

		JButton botonRealizarPingEquipo = new JButton("no es necesario crear ");
		crearBoton(nuevaPantalla, botonRealizarPingEquipo);
		botonRealizarPingEquipo.setBounds(540, 176, 200, 40);

		JComboBox<String> comboBoxEquipoPing = new JComboBox<String>(listaDeEquipos);
		crearComboBox(nuevaPantalla, comboBoxEquipoPing);
		comboBoxEquipoPing.setBounds(540, 236, 200, 40);

		JButton botonVelocidadMaxEntreEquipos = new JButton("otra nueva pantalla");
		crearBoton(nuevaPantalla, botonVelocidadMaxEntreEquipos);
		botonVelocidadMaxEntreEquipos.setBounds(540, 286, 200, 40);

		JComboBox<String> comboBoxVelMaxEquip1 = new JComboBox<>(listaDeEquipos);
		crearComboBox(nuevaPantalla, comboBoxVelMaxEquip1);
		comboBoxVelMaxEquip1.setBounds(540, 350, 200, 40);

		JComboBox<String> comboBoxVelMaxEquip2 = new JComboBox<>(listaDeEquipos);
		crearComboBox(nuevaPantalla, comboBoxVelMaxEquip2);
		comboBoxVelMaxEquip2.setBounds(540, 400, 200, 40);

		JButton botonMotrarConexionesGrafo = new JButton("ya tenemos este");
		crearBoton(nuevaPantalla, botonMotrarConexionesGrafo);
		botonMotrarConexionesGrafo.setBounds(540, 125, 200, 40);

		JButton botonVolver = new JButton("Volver");
		crearBoton(nuevaPantalla, botonVolver);
		botonVolver.setBounds(540, 450, 200, 40);

		JLabel labelTitulo = new JLabel("Redes");
		labelTitulo.setForeground(neonGreen);
		labelTitulo.setBounds(20, 20, 500, 40);
		labelTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JLabel labelSubTitulo = new JLabel("Consultas");
		labelSubTitulo.setForeground(neonGreen);
		labelSubTitulo.setBounds(570, 30, 200, 30);
		labelSubTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JLabel labelSeleccionarUnEquipo = new JLabel("Selecione un equipo: ");
		labelSeleccionarUnEquipo.setForeground(neonGreen);
		labelSeleccionarUnEquipo.setBounds(540, 206, 200, 40);

		JLabel labelSeleccionarEquipos = new JLabel("Selecione los equipos: ");
		labelSeleccionarEquipos.setForeground(neonGreen);
		labelSeleccionarEquipos.setBounds(540, 320, 200, 40);

		botonVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				cardLayout.show(paneles, "panelPrincipal");
			}
		});
		nuevaPantalla.add(labelTitulo);
		nuevaPantalla.add(labelSubTitulo);
		nuevaPantalla.add(scrollGrafo);

		nuevaPantalla.add(botonVolver);
		return nuevaPantalla;
	}

	private JPanel crearNuevaPantalla2(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {
		JPanel nuevaPantalla = new JPanel();
		nuevaPantalla.setBackground(neonBlack);
		nuevaPantalla.setLayout(null);

		JTextArea textAreaGrande = new JTextArea();
		textAreaGrande.setBackground(neonGray);
		textAreaGrande.setForeground(neonGreen);
		textAreaGrande.setBorder(new LineBorder(neonGreen, 2));
		textAreaGrande.setCaretColor(neonWhite);
		textAreaGrande.setEditable(false);

		scrollGrande = new JScrollPane(textAreaGrande);
		scrollGrande.setBounds(20, 70, 500, 400);
		scrollGrande.setBorder(new LineBorder(neonGreen, 2));
		panelCentral.add(scrollGrande);

		JLabel labelDireccionIP = new JLabel("Ingrese una direccion IP:");
		labelDireccionIP.setForeground(neonGreen);
		labelDireccionIP.setBounds(540, 43, 200, 40);

		JTextField txtDireccionIP = new JTextField();
		txtDireccionIP.setForeground(neonGreen);
		txtDireccionIP.setBackground(neonBlack);
		txtDireccionIP.setCaretColor(neonGreen);
		txtDireccionIP.setBorder(BorderFactory.createLineBorder(neonGreen, 2));
		txtDireccionIP.setBounds(540, 76, 200, 40);

		JButton botonRangoIP = new JButton("Ver rango direccion IP");
		crearBoton(nuevaPantalla, botonRangoIP);
		botonRangoIP.setBounds(540, 125, 200, 40);

		JLabel labelTitulo = new JLabel("Redes");
		labelTitulo.setForeground(neonGreen);
		labelTitulo.setBounds(20, 20, 500, 40);
		labelTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JButton botonVolver = new JButton("Volver");
		crearBoton(nuevaPantalla, botonVolver);
		botonVolver.setBounds(540, 450, 200, 40);
		botonVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(paneles, "panelPrincipal");
			}
		});

		JButton botonSalir = new JButton("Salir");
		crearBoton(nuevaPantalla, botonSalir);
		botonSalir.setBounds(50, 490, 100, 30);
		botonSalir.addActionListener(ev -> {
			int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas salir?", "Confirmar salida",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (confirm == JOptionPane.YES_OPTION)
				System.exit(0);
		});

		botonRangoIP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaGrande.setText(coordinador.rangoIP(txtDireccionIP.getText()));
			}
		});

		nuevaPantalla.add(txtDireccionIP);
		nuevaPantalla.add(labelTitulo);
		nuevaPantalla.add(labelDireccionIP);
		nuevaPantalla.add(scrollGrande);
		return nuevaPantalla;
	}

	private JPanel crearPantallaGrafico(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {

		JPanel panel = new JPanel(new BorderLayout());

		return panel;
	}

	public void definirColoresItems(JMenuBar menuBar) {
		menuBar.setBackground(Color.BLACK);
		menuBar.setForeground(NEON_GREEN);
	}

	public void definirColoresItems(JMenuItem item) {
		item.setBackground(Color.BLACK);
		item.setForeground(NEON_GREEN);

	}

	private JComboBox<String> crearComboBox(JPanel panel, JComboBox<String> comboBox) {
		comboBox.setBackground(NEON_GRAY); // Fondo gris
		comboBox.setForeground(NEON_GREEN); // Texto verde
		comboBox.setBorder(new LineBorder(NEON_GREEN, 2)); // Borde verde
		panel.add(comboBox);
		return comboBox;
	}

	private JButton crearBoton(JPanel panel, JButton boton) {
		boton.setBackground(Color.BLACK);
		boton.setForeground(NEON_GREEN);
		boton.setBorder(new LineBorder(NEON_GREEN, 2));
		panel.add(boton);
		return boton;
	}
}
