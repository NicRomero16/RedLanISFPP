package gui;

import java.awt.Color;
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

    // Mapa para almacenar las posiciones de los equipos usando Point
    private Map<Equipo, Point> posiciones;

    // Variables para manejar el arrastre de nodos
    private Equipo nodoSeleccionado;
    private Point posicionInicial;
    private Point posicionInicialNodo;

    // Variable para mostrar información del equipo
    private String infoEquipo;

    public GraphPanel(List<Equipo> equipos, List<Conexion> conexiones) {
        this.equipos = equipos;
        this.conexiones = conexiones;
        this.posiciones = new HashMap<>();
        setBackground(Color.BLACK); // Fondo negro
        
        // Añadir un listener para detectar cuando el panel cambia de tamaño
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                asignarPosicionesDinamicas(); // Recalcular posiciones cuando se redimensiona
                repaint(); // Redibujar el panel
            }
        });

        // Añadir listener de ratón para mover nodos
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Detectar si se hace clic en un nodo
                Point clickPoint = e.getPoint();
                for (Equipo equipo : equipos) {
                    Point pos = posiciones.get(equipo);
                    if (pos != null && pos.distance(clickPoint) <= 35) { // Aumentar el radio para incluir el nuevo tamaño del nodo
                        nodoSeleccionado = equipo;
                        posicionInicial = clickPoint;
                        posicionInicialNodo = pos;
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                nodoSeleccionado = null; // Desbloquear el nodo
                infoEquipo = null; // Ocultar información
                repaint(); // Redibujar para ocultar la información
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
                    repaint(); // Redibujar el panel
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // Detectar si el mouse está sobre un nodo
                Point mousePos = e.getPoint();
                infoEquipo = null; // Reiniciar información del equipo
                for (Equipo equipo : equipos) {
                    Point pos = posiciones.get(equipo);
                    if (pos != null && pos.distance(mousePos) <= 35) {
                        infoEquipo = equipo.getCodigo() + " \n " + equipo.getDescripcion() + " \n " + equipo.getMarca() + " \n " + equipo.getModelo() + " \n " + equipo.getEstado(); // Guardar la información del equipo
                        break;
                    }
                }
                repaint(); // Redibujar para mostrar información
            }
        });
    }

    // Método para asignar posiciones a los equipos en un círculo
    private void asignarPosicionesDinamicas() {
        int centerX = getWidth() / 2; // Centro del panel en X
        int centerY = getHeight() / 2; // Centro del panel en Y
        int radius = Math.min(centerX, centerY) - 50; // Radio para el círculo de nodos

        // Si no hay suficiente espacio para el radio, evitar errores
        if (radius < 0) radius = 10;

        // Distribuir los equipos en círculo
        int numEquipos = equipos.size();
        for (int i = 0; i < numEquipos; i++) {
            double angle = 2 * Math.PI * i / numEquipos; // Ángulo para cada equipo
            int x = (int) (centerX + radius * Math.cos(angle)); // Coordenada X
            int y = (int) (centerY + radius * Math.sin(angle)); // Coordenada Y
            posiciones.put(equipos.get(i), new Point(x, y)); // Guardar posición como Point
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int nodoRadius = 40; // Aumentar el tamaño del nodo

        // Dibujar conexiones
        g2d.setColor(Color.GREEN); // Color de las conexiones
        for (Conexion conexion : conexiones) {
            Equipo equipo1 = conexion.getEquipo1();
            Equipo equipo2 = conexion.getEquipo2();

            // Obtener las posiciones de los equipos para dibujar la línea
            Point pos1 = posiciones.get(equipo1);
            Point pos2 = posiciones.get(equipo2);

            if (pos1 != null && pos2 != null) {
                // Dibujar línea de conexión
                g2d.drawLine(pos1.x, pos1.y, pos2.x, pos2.y); // Dibuja la línea entre los nodos
            }
        }

        // Dibujar nodos (equipos)
        for (Equipo equipo : equipos) {
            Point pos = posiciones.get(equipo);
            if (pos != null) {
                g2d.setColor(Color.CYAN); // Color de los nodos
                // Dibuja el nodo como un círculo
                g2d.fillOval(pos.x - nodoRadius / 2, pos.y - nodoRadius / 2, nodoRadius, nodoRadius);

                // Dibujar nombre del equipo en el centro del nodo
                g2d.setColor(Color.BLACK);
                // Centrar el texto sobre el nodo
                g2d.drawString(equipo.getCodigo(), pos.x - g2d.getFontMetrics().stringWidth(equipo.getCodigo()) / 2, pos.y + 5);
            }
        }

        // Dibujar información del equipo en la esquina superior izquierda
        if (infoEquipo != null) {
            g2d.setColor(Color.GREEN); // Color de la información
            g2d.drawString(infoEquipo, 10, 20); // Mostrar la información
        }
    }
}
