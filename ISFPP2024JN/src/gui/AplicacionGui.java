package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import controlador.Coordinador;

public class AplicacionGui extends JFrame {
    private Coordinador coordinador;
    private JTextArea textAreaGrande;
    private JPanel panelCentral;
    private CardLayout cardLayout;
    private JPanel paneles; // Contenedor para intercambiar paneles

    public AplicacionGui(Coordinador coordinador) {
        this.coordinador = coordinador;
        // Configuración de la ventana principal
        setResizable(false);
        setTitle("Redes - Neon Style");
        setSize(800, 600); // Resolución: 800x600
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

        // Menú principal "Archivo"
        JMenu menuArchivo = new JMenu("Grafo");
        menuArchivo.setForeground(neonGreen);

        // Elementos dentro del menú "Archivo"
        JMenuItem itemAbrir = new JMenuItem("Grafico");
        
        itemAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(paneles, "nuevaPantalla");
            }
        });
        
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        JMenuItem itemSalir = new JMenuItem("Salir");
        
        // Añadir los elementos al menú "Archivo"
        menuArchivo.add(itemAbrir);
        menuArchivo.add(itemGuardar);
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
        JMenuItem itemConexion = new JMenuItem("Conexion");
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

        // Crear nueva pantalla que se mostrará cuando se presione el botón 2
        JPanel nuevaPantalla = crearNuevaPantalla(neonGreen, neonGray, neonBlack, neonWhite);
        paneles.add(nuevaPantalla, "nuevaPantalla");

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

        // Acción del botón 1
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
        labelNuevaPantalla.setBounds(300, 50, 200, 40);
        labelNuevaPantalla.setFont(labelNuevaPantalla.getFont().deriveFont(30f)); // Tamaño de fuente
        nuevaPantalla.add(labelNuevaPantalla);

        JButton botonRegresar = new JButton("Regresar");
        botonRegresar.setBackground(neonBlack);
        botonRegresar.setForeground(neonGreen);
        botonRegresar.setBorder(new LineBorder(neonGreen, 2));
        botonRegresar.setBounds(300, 150, 200, 40);
        nuevaPantalla.add(botonRegresar);

        // Acción del botón de regresar a la pantalla principal
        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(paneles, "panelPrincipal");
            }
        });

        return nuevaPantalla;
    }

  
}
