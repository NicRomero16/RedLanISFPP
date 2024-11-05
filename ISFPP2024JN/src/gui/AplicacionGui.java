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
	private JPanel panel;
	private JPanel nuevaPantalla;

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

		JLabel labelTitulo = new JLabel("Redes");
		labelTitulo.setForeground(neonGreen);
		labelTitulo.setBounds(20, 20, 500, 40);
		labelTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JLabel labelSubTitulo = new JLabel("Consultas");
		labelSubTitulo.setForeground(neonGreen);
		labelSubTitulo.setBounds(570, 30, 200, 30);
		labelSubTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JButton botonMostrarEquipos = new JButton("Ver Equipos");
		crearBoton(panelCentral, botonMostrarEquipos);
		botonMostrarEquipos.setBounds(540, 76, 200, 40);

		JButton botonMotrarConexionesGrafo = new JButton("Ver Conexiones");
		crearBoton(panelCentral, botonMotrarConexionesGrafo);
		botonMotrarConexionesGrafo.setBounds(540, 125, 200, 40);

		JButton botonRealizarPingEquipo = new JButton("Realizar Ping a un equipo");
		crearBoton(panelCentral, botonRealizarPingEquipo);
		botonRealizarPingEquipo.setBounds(540, 176, 200, 40);

		JLabel labelSeleccionarUnEquipo = new JLabel("Selecione un equipo: ");
		labelSeleccionarUnEquipo.setForeground(neonGreen);
		labelSeleccionarUnEquipo.setBounds(540, 206, 200, 40);

		JComboBox<String> comboBoxEquipoPing = new JComboBox<String>(listaDeEquipos);
		crearComboBox(panelCentral, comboBoxEquipoPing);
		comboBoxEquipoPing.setBounds(540, 236, 200, 40);

		JButton botonVelocidadMaxEntreEquipos = new JButton("Velocidad maxima entre equipos");
		crearBoton(panelCentral, botonVelocidadMaxEntreEquipos);
		botonVelocidadMaxEntreEquipos.setBounds(540, 286, 200, 40);

		JLabel labelSeleccionarEquipos = new JLabel("Selecione los equipos: ");
		labelSeleccionarEquipos.setForeground(neonGreen);
		labelSeleccionarEquipos.setBounds(540, 318, 200, 40);

		JComboBox<String> comboBoxVelMaxEquip1 = new JComboBox<>(listaDeEquipos);
		crearComboBox(panelCentral, comboBoxVelMaxEquip1);
		comboBoxVelMaxEquip1.setBounds(540, 350, 200, 40);

		JComboBox<String> comboBoxVelMaxEquip2 = new JComboBox<>(listaDeEquipos);
		crearComboBox(panelCentral, comboBoxVelMaxEquip2);
		comboBoxVelMaxEquip2.setBounds(540, 400, 200, 40);

		JButton botonMotrarMasConsultas = new JButton("Mas consultas");
		crearBoton(panelCentral, botonMotrarMasConsultas);
		botonMotrarMasConsultas.setBounds(540, 450, 200, 40);

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

		JLabel labelTitulo = new JLabel("Redes");
		labelTitulo.setForeground(neonGreen);
		labelTitulo.setBounds(20, 20, 500, 40);
		labelTitulo.setFont(labelTitulo.getFont().deriveFont(30f));

		JButton botonRangoIP = new JButton("Ver rango direccion IP");
		crearBoton(nuevaPantalla, botonRangoIP);
		botonRangoIP.setBounds(540, 62, 200, 40);

		JLabel labelDireccionIP = new JLabel("Ingrese una direccion IP:");
		labelDireccionIP.setForeground(neonGreen);
		labelDireccionIP.setBounds(540, 92, 200, 40);

		JTextField txtDireccionIP = new JTextField();
		crearJTextField(nuevaPantalla, txtDireccionIP);
		txtDireccionIP.setBounds(540, 125, 200, 40);

		JButton botonPingEntreIPS = new JButton("Ver equipos entre las ip");
		crearBoton(nuevaPantalla, botonPingEntreIPS);
		botonPingEntreIPS.setBounds(540, 175, 200, 40);

		JLabel labelDireccionesIP = new JLabel("Ingrese las direcciones IP:");
		labelDireccionesIP.setForeground(neonGreen);
		labelDireccionesIP.setBounds(540, 205, 200, 40);

		JTextField textIpOrigen = new JTextField();
		crearJTextField(nuevaPantalla, textIpOrigen);
		textIpOrigen.setBounds(540, 238, 200, 40);

		JTextField textIpDestino = new JTextField();
		crearJTextField(nuevaPantalla, textIpDestino);
		textIpDestino.setBounds(540, 288, 200, 40);

		JButton botonVolver = new JButton("Volver");
		crearBoton(nuevaPantalla, botonVolver);

		JButton botonSalir = new JButton("Salir");
		crearBoton(nuevaPantalla, botonSalir);
		botonSalir.setBounds(50, 490, 100, 30);

		botonVolver.setBounds(540, 450, 200, 40);
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
