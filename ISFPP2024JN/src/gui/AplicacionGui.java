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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.jgrapht.Graph;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;

public class AplicacionGui extends JFrame {
	private JScrollPane scrollGrande;
	private Coordinador coordinador;
	private JPanel pantallaCarga;
	private JPanel panelCentral;
	private CardLayout cardLayout;
	private JPanel paneles;
	private JPanel panel;
	private JPanel nuevaPantalla;

	private static final Color NEON_GREEN = new Color(57, 255, 20);
	private static final Color NEON_GRAY = new Color(30, 30, 30);
	private static final int ANCHO = 1000;
	private static final int ALTO = 600;

	public AplicacionGui(Coordinador coordinador) {
		this.coordinador = coordinador;
		cardLayout = new CardLayout();
		paneles = new JPanel(cardLayout);
		paneles.setBackground(Color.BLACK);
		setResizable(false);
		setTitle("Redes - Neon Style");
		setSize(ANCHO, ALTO);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();

		JMenu menuOpciones = new JMenu("Opciones");
		JMenuItem itemOpcionesConsultas = new JMenuItem("Consultas");
		definirColoresMenu(menuBar, menuOpciones, itemOpcionesConsultas);
		JMenuItem itemOpcionesGrafico = new JMenuItem("Grafico");
		definirColoresMenu(menuBar, menuOpciones, itemOpcionesGrafico);

		JMenu menuABM = new JMenu("Editar");
		JMenuItem itemABMEquipo = new JMenuItem("Equipos");
		definirColoresMenu(menuBar, menuABM, itemABMEquipo);
		JMenuItem itemABMConexion = new JMenuItem("Conexiones");
		definirColoresMenu(menuBar, menuABM, itemABMConexion);

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

				ABMEquipos equipoABM = new ABMEquipos(equipos, coordinador);

				panel.removeAll();
				panel.add(equipoABM);
				panel.revalidate();
				panel.repaint();

				cardLayout.show(paneles, "pantallaGrafico");
			}
		});

		itemABMConexion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ConexionesABM conexionABM = new ConexionesABM(coordinador, ANCHO, ALTO);

				panel.removeAll();
				panel.add(conexionABM);
				panel.revalidate();
				panel.repaint();
				cardLayout.show(paneles, "pantallaGrafico");
			}
		});

		setJMenuBar(menuBar);

		pantallaCarga = crearPantallaCarga(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE, menuBar);
		paneles.add(pantallaCarga, "pantallaCarga");

		panelCentral = crearPanelPrincipal(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE, menuBar);
		paneles.add(panelCentral, "panelPrincipal");

		panel = crearPantallaGrafico(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE);
		paneles.add(panel, "pantallaGrafico");

		nuevaPantalla = crearNuevaPantalla(NEON_GREEN, NEON_GRAY, Color.BLACK, Color.WHITE);
		paneles.add(nuevaPantalla, "nuevaPantalla");

		add(paneles);
		setVisible(true);
	}

	private JPanel crearPantallaCarga(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite,
			JMenuBar menuBar) {

		JPanel pantallaCarga = new JPanel();
		pantallaCarga.setBackground(neonBlack);
		pantallaCarga.setLayout(null);

		JLabel labelCargando = new JLabel("Cargando, por favor espere...");
		labelCargando.setForeground(neonGreen);
		labelCargando.setFont(new Font("Arial", Font.BOLD, 20));
		labelCargando.setBounds((int)(ANCHO * 0.25), (int)(ALTO * 0.333), (int)(ANCHO * 0.5), (int)(ALTO * 0.083));
		pantallaCarga.add(labelCargando);

		JProgressBar barraProgreso = new JProgressBar();
		barraProgreso.setBounds((int)(ANCHO * 0.1875), (int)(ALTO * 0.5), (int)(ANCHO * 0.625), (int)(ALTO * 0.05));
		barraProgreso.setForeground(neonGreen);
		barraProgreso.setBackground(neonGray);
		barraProgreso.setBorder(new LineBorder(neonGreen, 2));
		pantallaCarga.add(barraProgreso);

		SwingUtilities.invokeLater(() -> {
			setJMenuBar(null);
		});

		new Thread(() -> {
			for (int i = 0; i <= 100; i++) {
				try {
					Thread.sleep(50);
					barraProgreso.setValue(i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			SwingUtilities.invokeLater(() -> {
				setJMenuBar(menuBar);
				cardLayout.show(paneles, "panelPrincipal");
			});
		}).start();

		return pantallaCarga;
	}

	private JPanel crearPanelPrincipal(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite,
			JMenuBar menuBar) {

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
		scrollGrande.setBounds((int)(ANCHO * 0.025), (int)(ALTO * 0.117), (int)(ANCHO * 0.625), (int)(ALTO * 0.667));
		scrollGrande.setBorder(new LineBorder(neonGreen, 2));
		panelCentral.add(scrollGrande);
		String[] listaDeEquipos = coordinador.devolverEquipoCodigos();

		JLabel labelTitulo = new JLabel("Redes");
		labelTitulo.setForeground(neonGreen);
		labelTitulo.setBounds((int)(ANCHO * 0.025), (int)(ALTO * 0.033), (int)(ANCHO * 0.625), (int)(ALTO * 0.067));
		labelTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JLabel labelSubTitulo = new JLabel("Consultas");
		labelSubTitulo.setForeground(neonGreen);
		labelSubTitulo.setBounds((int)(ANCHO * 0.713), (int)(ALTO * 0.05), (int)(ANCHO * 0.25), (int)(ALTO * 0.05));
		labelSubTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JButton botonMostrarEquipos = new JButton("Ver Equipos");
		crearBoton(panelCentral, botonMostrarEquipos);
		botonMostrarEquipos.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.127), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JButton botonMotrarConexionesGrafo = new JButton("Ver Conexiones");
		crearBoton(panelCentral, botonMotrarConexionesGrafo);
		botonMotrarConexionesGrafo.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.208), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JButton botonRealizarPingEquipo = new JButton("Realizar Ping a un equipo");
		crearBoton(panelCentral, botonRealizarPingEquipo);
		botonRealizarPingEquipo.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.293), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JLabel labelSeleccionarUnEquipo = new JLabel("Selecione un equipo: ");
		labelSeleccionarUnEquipo.setForeground(neonGreen);
		labelSeleccionarUnEquipo.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.343), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JComboBox<String> comboBoxEquipoPing = new JComboBox<String>(listaDeEquipos);
		crearComboBox(panelCentral, comboBoxEquipoPing);
		comboBoxEquipoPing.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.393), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JButton botonVelocidadMaxEntreEquipos = new JButton("Velocidad maxima entre equipos");
		crearBoton(panelCentral, botonVelocidadMaxEntreEquipos);
		botonVelocidadMaxEntreEquipos.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.477), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JLabel labelSeleccionarEquipos = new JLabel("Selecione los equipos: ");
		labelSeleccionarEquipos.setForeground(neonGreen);
		labelSeleccionarEquipos.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.53), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JComboBox<String> comboBoxVelMaxEquip1 = new JComboBox<>(listaDeEquipos);
		crearComboBox(panelCentral, comboBoxVelMaxEquip1);
		comboBoxVelMaxEquip1.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.583), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JComboBox<String> comboBoxVelMaxEquip2 = new JComboBox<>(listaDeEquipos);
		crearComboBox(panelCentral, comboBoxVelMaxEquip2);
		comboBoxVelMaxEquip2.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.667), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JButton botonMotrarMasConsultas = new JButton("Mas consultas");
		crearBoton(panelCentral, botonMotrarMasConsultas);
		botonMotrarMasConsultas.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.75), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JButton botonSalir = new JButton("Salir");
		crearBoton(panelCentral, botonSalir);
		botonSalir.setBounds((int)(ANCHO * 0.0625), (int)(ALTO * 0.817), (int)(ANCHO * 0.125), (int)(ALTO * 0.05));

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

		JTextArea textAreaGrande = new JTextArea();
		textAreaGrande.setBackground(neonGray);
		textAreaGrande.setForeground(neonGreen);
		textAreaGrande.setBorder(new LineBorder(neonGreen, 2));
		textAreaGrande.setCaretColor(neonWhite);
		textAreaGrande.setEditable(false);

		scrollGrande = new JScrollPane(textAreaGrande);
		scrollGrande.setBounds((int)(ANCHO * 0.025), (int)(ALTO * 0.117), (int)(ANCHO * 0.625), (int)(ALTO * 0.667));
		scrollGrande.setBorder(new LineBorder(neonGreen, 2));
		nuevaPantalla.add(scrollGrande);

		JLabel labelTitulo = new JLabel("Redes");
		labelTitulo.setForeground(neonGreen);
		labelTitulo.setBounds((int)(ANCHO * 0.025), (int)(ALTO * 0.033), (int)(ANCHO * 0.625), (int)(ALTO * 0.067));
		labelTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JButton botonRangoIP = new JButton("Ver rango direccion IP");
		crearBoton(nuevaPantalla, botonRangoIP);
		botonRangoIP.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.103), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JLabel labelDireccionIP = new JLabel("Ingrese una direccion IP:");
		labelDireccionIP.setForeground(neonGreen);
		labelDireccionIP.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.153), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JTextField txtDireccionIP = new JTextField();
		crearJTextField(nuevaPantalla, txtDireccionIP);
		txtDireccionIP.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.208), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JButton botonPingEntreIPS = new JButton("Ver equipos entre las ip");
		crearBoton(nuevaPantalla, botonPingEntreIPS);
		botonPingEntreIPS.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.292), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JLabel labelDireccionesIP = new JLabel("Ingrese las direcciones IP:");
		labelDireccionesIP.setForeground(neonGreen);
		labelDireccionesIP.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.343), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JTextField textIpOrigen = new JTextField();
		crearJTextField(nuevaPantalla, textIpOrigen);
		textIpOrigen.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.393), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JTextField textIpDestino = new JTextField();
		crearJTextField(nuevaPantalla, textIpDestino);
		textIpDestino.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.48), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JButton botonVolver = new JButton("Volver");
		crearBoton(nuevaPantalla, botonVolver);
		botonVolver.setBounds((int)(ANCHO * 0.675), (int)(ALTO * 0.75), (int)(ANCHO * 0.25), (int)(ALTO * 0.067));

		JButton botonSalir = new JButton("Salir");
		crearBoton(nuevaPantalla, botonSalir);
		botonSalir.setBounds((int)(ANCHO * 0.0625), (int)(ALTO * 0.817), (int)(ANCHO * 0.125), (int)(ALTO * 0.05));
		botonVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(paneles, "panelPrincipal");
			}
		});

		botonSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas salir?",
						"Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (confirm == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});

		botonRangoIP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaGrande.setText(coordinador.verRangoIP(txtDireccionIP.getText()));
			}
		});

		botonPingEntreIPS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textIpOrigen.getText().equals(textIpDestino.getText())) {
					JOptionPane.showMessageDialog(null, "Ingrese direcciones IP diferentes", "Error",
							JOptionPane.WARNING_MESSAGE);
					textAreaGrande.setText("");
					return;
				}
				textAreaGrande
						.setText(coordinador.mostrarEquiposEntrePings(textIpOrigen.getText(), textIpDestino.getText()));
			}
		});

		nuevaPantalla.add(labelTitulo);
		nuevaPantalla.add(labelDireccionIP);
		nuevaPantalla.add(labelDireccionesIP);
		nuevaPantalla.add(scrollGrande);
		return nuevaPantalla;
	}

	private JPanel crearPantallaGrafico(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {

		JPanel panel = new JPanel(new BorderLayout());

		return panel;
	}

	private void crearJTextField(JPanel panel, JTextField text) {
		text.setForeground(NEON_GREEN);
		text.setBackground(Color.BLACK);
		text.setCaretColor(NEON_GREEN);
		text.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 2));
		panel.add(text);
	}

	public void definirColoresMenu(JMenuBar menuBar, JMenu menu, JMenuItem item) {
		menuBar.setBackground(Color.BLACK);
		menuBar.setForeground(NEON_GREEN);
		menu.setBackground(Color.BLACK);
		menu.setForeground(NEON_GREEN);
		item.setBackground(Color.BLACK);
		item.setForeground(NEON_GREEN);
		menu.add(item);
		menuBar.add(menu);
	}

	private JComboBox<String> crearComboBox(JPanel panel, JComboBox<String> comboBox) {
		comboBox.setBackground(NEON_GRAY); 
		comboBox.setForeground(NEON_GREEN);
		comboBox.setBorder(new LineBorder(NEON_GREEN, 2));
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
