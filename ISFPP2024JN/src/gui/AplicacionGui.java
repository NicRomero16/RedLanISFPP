package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.jgrapht.Graph;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoEquipo;
import modelo.Ubicacion;

public class AplicacionGui extends JFrame {
	private JTextField textField;
    private Coordinador coordinador;
    private JTextArea textAreaGrande;
    private JPanel panelCentral;
    private CardLayout cardLayout;
    private JPanel paneles; // Contenedor para intercambiar paneles
    private JTextArea textAreaGrafo; // Nueva área de texto para mostrar el grafo en la nueva pantalla
    private JPanel panelGrafico;
    private static final int ANCHO_VENTANA_PRINCIPAL = 800;
    private static final int LARGO_VENTANA_PRINCIPAL = 600;
    
    public AplicacionGui(Coordinador coordinador) {
        this.coordinador = coordinador;
        // Configuración de la ventana principal
        setResizable(false);
        setTitle("Redes - Neon Style");
        setSize(ANCHO_VENTANA_PRINCIPAL, LARGO_VENTANA_PRINCIPAL); // Resolución: 800x600
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Colores neón
        Color neonGreen = new Color(57, 255, 20);
        Color neonGray = new Color(30, 30, 30);
        Color neonBlack = Color.BLACK;
        Color neonWhite = Color.WHITE;

        // Crear la barra de menú
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(neonBlack);
        menuBar.setForeground(neonGreen);

        // Menú principal "Grafo"
        JMenu menuArchivo = new JMenu("Grafo");
        menuArchivo.setForeground(neonGreen);

        // Elementos dentro del menú "grafo"
        JMenuItem itemAbrir = new JMenuItem("Grafico");
        itemAbrir.addActionListener(new ActionListener() {
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
                panelGrafico.removeAll();
                panelGrafico.add(graphPanel);
                panelGrafico.revalidate();
                panelGrafico.repaint();

                // Mostrar la pantalla del grafo
                cardLayout.show(paneles, "pantallaGrafico");
            }
        });
        
        JMenuItem itemConsultas= new JMenuItem("Consultas");
        itemConsultas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(paneles, "panelPrincipal");
            }
        });
        
        JMenuItem itemSalir = new JMenuItem("Salir");

        // Añadir los elementos al menú "Archivo"
        menuArchivo.add(itemAbrir);
        menuArchivo.add(itemConsultas);
        menuArchivo.addSeparator(); // Separador
        menuArchivo.add(itemSalir);

        // Menú principal "Editar"
        JMenu menuEditar = new JMenu("Editar");
        menuEditar.setForeground(neonGreen);

        // Elementos dentro del menú "Editar"
        JMenuItem itemCopiar = new JMenuItem("Copiar");
        JMenuItem itemPegar = new JMenuItem("Pegar");

        // Añadir los elementos al menú "Editar"
        menuEditar.add(itemCopiar);
        menuEditar.add(itemPegar);

        // Añadir los menús a la barra de menú
        menuBar.add(menuArchivo);
        menuBar.add(menuEditar);

        // Establecer la barra de menú en el frame
        setJMenuBar(menuBar);

        JMenu menuABM = new JMenu("ABM");
        menuABM.setForeground(neonGreen);

        JMenuItem itemEquipo = new JMenuItem("Equipo");
		itemEquipo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(paneles, "panelEquipo");
			}
		});
        JMenuItem itemConexion = new JMenuItem("Conexion");
        itemConexion.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		cardLayout.show(paneles,"panelConexion");
        	}
        });
        JMenuItem itemUbicacion = new JMenuItem("Ubicacion");

        menuABM.add(itemEquipo);
        menuABM.add(itemConexion);
        menuABM.add(itemUbicacion);

        menuBar.add(menuABM);

        // Configuración de CardLayout
        cardLayout = new CardLayout();
        paneles = new JPanel(cardLayout);
        add(paneles);

        // Crear panel original (pantalla principal)
        panelCentral = crearPanelPrincipal(neonGreen, neonGray, neonBlack, neonWhite);
        paneles.add(panelCentral, "panelPrincipal");

        // Pantalla del grafo
        panelGrafico = crearPantallaGrafico(neonGreen, neonGray, neonBlack, neonWhite);
        paneles.add(panelGrafico, "pantallaGrafico");
        
        // Crear nueva pantalla que se mostrará cuando se presione el botón 2
        JPanel nuevaPantalla = crearNuevaPantalla(neonGreen, neonGray, neonBlack, neonWhite);
        paneles.add(nuevaPantalla, "nuevaPantalla");
        
        JPanel panelConexion = crearPanelConexion(neonGreen, neonGray, neonBlack, neonWhite);
		paneles.add(panelConexion, "panelConexion");

		JPanel panelEquipo = crearPanelEquipo(neonGreen, neonGray, neonBlack, neonWhite);
		paneles.add(panelEquipo, "panelEquipo");

        setJMenuBar(menuBar);
        setVisible(true);
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

        JScrollPane scrollGrande = new JScrollPane(textAreaGrande);
        scrollGrande.setBounds(20, 70, 500, 400); // Posición y tamaño
        scrollGrande.setBorder(new LineBorder(neonGreen, 2));
        panelCentral.add(scrollGrande);
        
        
        
        // Area de texto pequeña (con scroll y borde negro)
        JTextArea textAreaPequena = new JTextArea();
        textAreaPequena.setBackground(neonGray);
        textAreaPequena.setForeground(neonGreen);
        textAreaPequena.setBorder(new LineBorder(neonGreen, 2));
        textAreaPequena.setCaretColor(neonWhite); // Cursor blanco

        JScrollPane scrollPequeno = new JScrollPane(textAreaPequena);
        scrollPequeno.setBounds(540, 70, 200, 150); // Posición y tamaño
        scrollPequeno.setBorder(new LineBorder(neonGreen, 2));
        panelCentral.add(scrollPequeno);

        // Botones
        JButton botonMotrarGrafo = new JButton("Visualizar Conexiones");
        botonMotrarGrafo.setBackground(neonBlack);
        botonMotrarGrafo.setForeground(neonGreen);
        botonMotrarGrafo.setBorder(new LineBorder(neonGreen, 2));
        botonMotrarGrafo.setBounds(540, 240, 200, 40);
        panelCentral.add(botonMotrarGrafo);

        JButton boton2 = new JButton("Botón 2");
        boton2.setBackground(neonBlack);
        boton2.setForeground(neonGreen);
        boton2.setBorder(new LineBorder(neonGreen, 2));
        boton2.setBounds(540, 290, 200, 40);
        panelCentral.add(boton2);

        JButton boton3 = new JButton("Botón 3");
        boton3.setBackground(neonBlack);
        boton3.setForeground(neonGreen);
        boton3.setBorder(new LineBorder(neonGreen, 2));
        boton3.setBounds(540, 340, 200, 40);
        panelCentral.add(boton3);

        JButton botonSalir = new JButton("Salir");
        botonSalir.setBackground(neonBlack);
        botonSalir.setForeground(neonGreen);
        botonSalir.setBorder(new LineBorder(neonGreen, 2));
        botonSalir.setBounds(540, 390, 200, 40);
        panelCentral.add(botonSalir);

        JLabel labelTitulo = new JLabel("Redes");
        labelTitulo.setForeground(neonGreen);
        labelTitulo.setBounds(20, 20, 500, 40); // Ajuste de posición y tamaño
        labelTitulo.setFont(labelTitulo.getFont().deriveFont(30f)); // Tamaño de fuente
        panelCentral.add(labelTitulo);

        // Acción del visualizar conexiones
        botonMotrarGrafo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaGrande.setText(coordinador.imprimirGrafo2());
            }
        });

        // Acción del botón 2
        boton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambiar a la nueva pantalla
                cardLayout.show(paneles, "nuevaPantalla");
            }
        });

        // Acción del botón 3
        boton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                // Implementa tu lógica aquí
            }
        });

        // Acción del botón de salir
        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        return panelCentral;
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
        panel.setBackground(neonBlack);

        JLabel label = new JLabel("Visualización del Grafo");
        label.setForeground(neonGreen);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        // Volver al panel principal
        JButton botonVolver = new JButton("Volver");
        botonVolver.setBackground(neonBlack);
        botonVolver.setForeground(neonGreen);
        botonVolver.setBorder(new LineBorder(neonGreen, 2));
        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(paneles, "panelPrincipal");
            }
        });
        panel.add(botonVolver, BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel crearPanelEquipo(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {
		JPanel panelEquipo = new JPanel();
		panelEquipo.setBackground(neonBlack);
		panelEquipo.setLayout(null); // Layout nulo para colocación absoluta

		// Crear botones con opciones adicionales
		JButton botonAgregarEquipo = new JButton("Agregar Equipo");
		botonAgregarEquipo.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, ANCHO_VENTANA_PRINCIPAL / 8, 200, 40);
		botonAgregarEquipo.setBackground(neonBlack);
		botonAgregarEquipo.setForeground(neonGreen);
		botonAgregarEquipo.setBorder(new LineBorder(neonGreen, 2));
		panelEquipo.add(botonAgregarEquipo);

		JButton botonEliminarEquipo = new JButton("Eliminar Equipo");
		botonEliminarEquipo.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, (ANCHO_VENTANA_PRINCIPAL / 5), 200, 40);
		botonEliminarEquipo.setBackground(neonBlack);
		botonEliminarEquipo.setForeground(neonGreen);
		botonEliminarEquipo.setBorder(new LineBorder(neonGreen, 2));
		panelEquipo.add(botonEliminarEquipo);

		JButton botonModificarEquipo = new JButton("Modificar Equipo");
		botonModificarEquipo.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, (int) (ANCHO_VENTANA_PRINCIPAL / 3.6), 200, 40);
		botonModificarEquipo.setBackground(neonBlack);
		botonModificarEquipo.setForeground(neonGreen);
		botonModificarEquipo.setBorder(new LineBorder(neonGreen, 2));
		panelEquipo.add(botonModificarEquipo);

		JButton botonRegresar = new JButton("Regresar");
		botonRegresar.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, ANCHO_VENTANA_PRINCIPAL / 2, 200, 40);
		botonRegresar.setBackground(neonBlack);
		botonRegresar.setForeground(neonGreen);
		botonRegresar.setBorder(new LineBorder(neonGreen, 2));
		panelEquipo.add(botonRegresar);

		// Acción del botón regresar para volver al panel principal
		botonRegresar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(paneles, "panelPrincipal"); // Regresar al panel principal
			}
		});

		// Acción del botón 1
		botonAgregarEquipo.addActionListener(new ActionListener() {
			String codigo, descripcion, marca, modelo, codigoTE, descripcionTE, codigoU, descripcionU;
			TipoEquipo tipoEquipo = new TipoEquipo(codigoTE, descripcionTE);
			Ubicacion ubicacion = new Ubicacion(codigoU, descripcionU);
			Equipo eq = new Equipo(null, null, null, null, null, null, true);

			@Override
			   public void actionPerformed(ActionEvent e) {
		        // Crear un nuevo JTextField si no existe
		        if (textField == null) {
		            textField = new JTextField();
		            
		            // Establecer tamaño y posición del campo de texto
		            textField.setBounds(100, 100, 200, 30); // Ajusta la posición y el tamaño
		            
		            // Añadir el JTextField al panel principal
		            panelEquipo.add(textField);
		            
		            // Refrescar el panel para que el nuevo JTextField aparezca
		            panelEquipo.revalidate();  // Recalcula el layout
		            panelEquipo.repaint();     // Redibuja el panel
		        }
		        
		        // Si deseas mover el foco al JTextField automáticamente
		        textField.requestFocus();
		    }

		});

		return panelEquipo;
	}

	private JPanel crearPanelConexion(Color neonGreen, Color neonGray, Color neonBlack, Color neonWhite) {
		JPanel panelConexion = new JPanel();
		panelConexion.setBackground(neonBlack);
		panelConexion.setLayout(null); // Layout nulo para colocación absoluta

		// Crear botones con opciones adicionales
		JButton botonAgregarConexion = new JButton("Agregar Conexion");
		botonAgregarConexion.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, ANCHO_VENTANA_PRINCIPAL / 8, 200, 40);
		botonAgregarConexion.setBackground(neonBlack);
		botonAgregarConexion.setForeground(neonGreen);
		botonAgregarConexion.setBorder(new LineBorder(neonGreen, 2));
		panelConexion.add(botonAgregarConexion);

		JButton botonEliminarConexion = new JButton("Eliminar Conexion");
		botonEliminarConexion.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, ANCHO_VENTANA_PRINCIPAL / 5, 200, 40);
		botonEliminarConexion.setBackground(neonBlack);
		botonEliminarConexion.setForeground(neonGreen);
		botonEliminarConexion.setBorder(new LineBorder(neonGreen, 2));
		panelConexion.add(botonEliminarConexion);

		JButton botonModificarConexion = new JButton("Modificar Conexion");
		botonModificarConexion.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, (int) (ANCHO_VENTANA_PRINCIPAL / 3.6), 200, 40);
		botonModificarConexion.setBackground(neonBlack);
		botonModificarConexion.setForeground(neonGreen);
		botonModificarConexion.setBorder(new LineBorder(neonGreen, 2));
		panelConexion.add(botonModificarConexion);

		JButton botonRegresar = new JButton("Regresar");
		botonRegresar.setBounds(ANCHO_VENTANA_PRINCIPAL / 3, ANCHO_VENTANA_PRINCIPAL / 2, 200, 40);
		botonRegresar.setBackground(neonBlack);
		botonRegresar.setForeground(neonGreen);
		botonRegresar.setBorder(new LineBorder(neonGreen, 2));
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
