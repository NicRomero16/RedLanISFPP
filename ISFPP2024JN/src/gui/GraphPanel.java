package gui;

import java.awt.Color;
import java.awt.Font; // Importar la clase Font
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import modelo.Conexion;
import modelo.Equipo;

public class GraphPanel extends JPanel {

	private List<Equipo> equipos;
	private List<Conexion> conexiones;
	private Map<Equipo, Point> posiciones;
	private String infoTexto;
	private Equipo nodoSeleccionado;
	private Point posicionInicial;
	private Point posicionInicialNodo;

	public GraphPanel(List<Equipo> equipos, List<Conexion> conexiones) {
		this.equipos = equipos;
		this.conexiones = conexiones;
		this.posiciones = new HashMap<>();
		setBackground(Color.BLACK);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				asignarPosicionesDinamicas();
				repaint();
			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point clickPoint = e.getPoint();
				boolean clickedOnNode = false; // Variable para verificar si se hizo clic en un nodo

				for (Equipo equipo : equipos) {
					Point pos = posiciones.get(equipo);
					if (pos != null && pos.distance(clickPoint) <= 35) {
						// Si se hace clic en un nodo, lo seleccionamos
						nodoSeleccionado = equipo;
						posicionInicial = clickPoint;
						posicionInicialNodo = pos;

						// Crear el texto de información
						StringBuilder infoBuilder = new StringBuilder();
						infoBuilder.append(" Equipo: ").append(equipo.getCodigo()).append(" \tDescripción: ")
								.append(equipo.getDescripcion()).append(" \tUbicación: ");

						// Verificación de la ubicación
						if (equipo.getUbicacion() != null) {
							infoBuilder.append(equipo.getUbicacion().getDescripcion());
						} else {
							infoBuilder.append("Sin ubicación actualmente");
						}

						// Usar if-else para asignar el estado
						String status;
						if (equipo.getEstado()) {
							status = "Conectado";
						} else {
							status = "Desconectado";
						}
						infoBuilder.append(" \tEstado: ").append(status);

						// Agregar conexiones
						infoBuilder.append("\nConectado a: \n");
						for (Conexion conexion : conexiones) {
							if (conexion.getEquipo1().equals(equipo)) {
								infoBuilder.append(conexion.getEquipo2().getCodigo()).append("\n");
							} else if (conexion.getEquipo2().equals(equipo)) {
								infoBuilder.append(conexion.getEquipo1().getCodigo()).append("\n");
							}
						}

						infoTexto = infoBuilder.toString(); // Asignar texto de información
						clickedOnNode = true; // Se hizo clic en un nodo
						break;
					}
				}

				// Si no se hizo clic en un nodo, ocultar la información
				if (!clickedOnNode) {
					infoTexto = null; // Ocultar información
				}

				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				nodoSeleccionado = null; // Desbloquear el nodo
				repaint(); // Redibujar el panel
			}
		});

		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (nodoSeleccionado != null) {
					// Mover el nodo seleccionado
					Point nuevaPosicion = e.getPoint();
					int dx = (int) (nuevaPosicion.getX() - posicionInicial.getX());
					int dy = (int) (nuevaPosicion.getY() - posicionInicial.getY());
					Point nuevaPos = new Point(posicionInicialNodo.x + dx, posicionInicialNodo.y + dy);
					posiciones.put(nodoSeleccionado, nuevaPos); // Actualizar posición en el mapa
					posicionInicialNodo = nuevaPos; // Actualizar posición inicial del nodo
					posicionInicial = nuevaPos; // Actualizar la posición del mouse
					repaint(); // Redibujar el panel
				}
			}
		});
	}

	private void asignarPosicionesDinamicas() {
		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		int radius = Math.min(centerX, centerY) - 50;

		if (radius < 0)
			radius = 10;

		int numEquipos = equipos.size();
		for (int i = 0; i < numEquipos; i++) {
			double angle = 2 * Math.PI * i / numEquipos;
			int x = (int) (centerX + radius * Math.cos(angle));
			int y = (int) (centerY + radius * Math.sin(angle));
			posiciones.put(equipos.get(i), new Point(x, y));
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		int nodoRadius = 55;
		g2d.setColor(Color.GREEN);
		for (Conexion conexion : conexiones) {
			Equipo equipo1 = conexion.getEquipo1();
			Equipo equipo2 = conexion.getEquipo2();
			Point pos1 = posiciones.get(equipo1);
			Point pos2 = posiciones.get(equipo2);
			if (pos1 != null && pos2 != null) {
				g2d.drawLine(pos1.x, pos1.y, pos2.x, pos2.y);
			}
		}

		for (Equipo equipo : equipos) {
			Point pos = posiciones.get(equipo);
			if (pos != null) {
				g2d.setColor(Color.CYAN);
				g2d.fillOval(pos.x - nodoRadius / 2, pos.y - nodoRadius / 2, nodoRadius, nodoRadius);
				g2d.setColor(Color.BLACK);

				// Establecer una fuente más gruesa (negrita)
				g2d.setFont(new Font("Arial", Font.BOLD, 16));
				g2d.drawString(equipo.getCodigo(), pos.x - g2d.getFontMetrics().stringWidth(equipo.getCodigo()) / 2,
						pos.y + 5);
			}
		}

		if (infoTexto != null) {
			g2d.setColor(Color.CYAN);
			String[] lineas = infoTexto.split("\n"); // Usar \n para dividir las líneas
			int y = 20; // Posición vertical inicial para el texto
			for (String linea : lineas) {
				g2d.drawString(linea, 10, y);
				y += 15; // Aumentar la posición vertical para la siguiente línea
			}
		}
	}
}
