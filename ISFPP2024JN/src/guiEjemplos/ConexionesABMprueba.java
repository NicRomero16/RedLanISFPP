package guiEjemplos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ConexionesABMprueba extends JFrame {
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color NEON_GRAY = new Color(30, 30, 30);
    private JComboBox<Equipo> comboEquipo1;
    private JComboBox<Equipo> comboEquipo2;
    private JComboBox<TipoPuerto> comboPuerto1;
    private JComboBox<TipoPuerto> comboPuerto2;
    private JComboBox<TipoCable> comboCable;
    private JButton btnAgregar;
    private JTable tablaConexiones;
    private DefaultTableModel modeloTabla;

    public ConexionesABMprueba(List<Equipo> equipos, List<TipoPuerto> puertos, List<TipoCable> cables) {
        setTitle("ConexionesABM");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(NEON_GRAY); // Color de fondo de la ventana

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new GridLayout(3, 2, 10, 10));
        panelSuperior.setBackground(NEON_GRAY); // Color de fondo del panel superior

        comboEquipo1 = new JComboBox<>(equipos.toArray(new Equipo[0]));
        comboEquipo2 = new JComboBox<>(equipos.toArray(new Equipo[0]));
        comboPuerto1 = new JComboBox<>(puertos.toArray(new TipoPuerto[0]));
        comboPuerto2 = new JComboBox<>(puertos.toArray(new TipoPuerto[0]));
        comboCable = new JComboBox<>(cables.toArray(new TipoCable[0]));

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(NEON_GREEN);
        btnAgregar.setForeground(Color.BLACK);

        panelSuperior.add(new JLabel("Equipo1", SwingConstants.CENTER));
        panelSuperior.add(comboEquipo1);
        panelSuperior.add(new JLabel("TipoPuerto1", SwingConstants.CENTER));
        panelSuperior.add(comboPuerto1);
        panelSuperior.add(new JLabel("Equipo2", SwingConstants.CENTER));
        panelSuperior.add(comboEquipo2);
        panelSuperior.add(new JLabel("TipoPuerto2", SwingConstants.CENTER));
        panelSuperior.add(comboPuerto2);
        panelSuperior.add(new JLabel("TipoCable", SwingConstants.CENTER));
        panelSuperior.add(comboCable);
        panelSuperior.add(btnAgregar);

        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"Equipo1", "TipoPuerto1", "Equipo2", "TipoPuerto2", "TipoCable", "Modificar", "Borrar"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaConexiones = new JTable(modeloTabla) {
            @Override
            public Component prepareEditor(TableCellEditor editor, int row, int column) {
                Component c = super.prepareEditor(editor, row, column);
                if (c instanceof JButton) {
                    ((JButton) c).doClick();
                }
                return c;
            }
        };

        tablaConexiones.setBackground(NEON_GRAY);
        tablaConexiones.setForeground(Color.WHITE);
        tablaConexiones.setGridColor(NEON_GREEN);
        
        // Renderers and editors for buttons
        tablaConexiones.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tablaConexiones.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JButton("Modificar")));

        tablaConexiones.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tablaConexiones.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JButton("Borrar")));

        JScrollPane scrollPane = new JScrollPane(tablaConexiones);
        add(scrollPane, BorderLayout.CENTER);

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Equipo equipo1 = (Equipo) comboEquipo1.getSelectedItem();
                Equipo equipo2 = (Equipo) comboEquipo2.getSelectedItem();
                TipoPuerto puerto1 = (TipoPuerto) comboPuerto1.getSelectedItem();
                TipoPuerto puerto2 = (TipoPuerto) comboPuerto2.getSelectedItem();
                TipoCable cable = (TipoCable) comboCable.getSelectedItem();

                modeloTabla.addRow(new Object[]{equipo1, puerto1, equipo2, puerto2, cable, "Modificar", "Borrar"});
            }
        });
    }

    public static void main(String[] args) {
        List<Equipo> equipos = cargarEquipos();
        List<TipoPuerto> puertos = cargarPuertos();
        List<TipoCable> cables = cargarCables();

        ConexionesABMprueba abm = new ConexionesABMprueba(equipos, puertos, cables);
        abm.setVisible(true);
    }

    private static List<Equipo> cargarEquipos() {
        List<Equipo> equipos = new ArrayList<>();
        equipos.add(new Equipo("SWAM"));
        equipos.add(new Equipo("PC1"));
        return equipos;
    }

    private static List<TipoPuerto> cargarPuertos() {
        List<TipoPuerto> puertos = new ArrayList<>();
        puertos.add(new TipoPuerto("1G"));
        puertos.add(new TipoPuerto("8G"));
        return puertos;
    }

    private static List<TipoCable> cargarCables() {
        List<TipoCable> cables = new ArrayList<>();
        cables.add(new TipoCable("C5E"));
        return cables;
    }

    static class Equipo {
        private String nombre;

        public Equipo(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    static class TipoPuerto {
        private String tipo;

        public TipoPuerto(String tipo) {
            this.tipo = tipo;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        @Override
        public String toString() {
            return tipo;
        }
    }

    static class TipoCable {
        private String tipo;

        public TipoCable(String tipo) {
            this.tipo = tipo;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        @Override
        public String toString() {
            return tipo;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(NEON_GREEN);
            setForeground(Color.BLACK);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            if (isSelected) {
                setBackground(Color.YELLOW);
            } else {
                setBackground(NEON_GREEN);
            }
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public ButtonEditor(JButton button) {
            this.button = button;
            this.button.addActionListener(this);
            button.setBackground(NEON_GREEN);
            button.setForeground(Color.BLACK);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isPushed) {
                if (label.equals("Modificar")) {
                    new EditarConexion(modeloTabla, row).setVisible(true);
                } else if (label.equals("Borrar")) {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Desea borrar esta conexión?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        modeloTabla.removeRow(row);
                    }
                }
            }
            isPushed = false;
            fireEditingStopped();
        }
    }

    // Clase interna EditarConexion
    class EditarConexion extends JFrame {
        private JComboBox<Equipo> comboEquipo1;
        private JComboBox<Equipo> comboEquipo2;
        private JComboBox<TipoPuerto> comboPuerto1;
        private JComboBox<TipoPuerto> comboPuerto2;
        private JComboBox<TipoCable> comboCable;
        private JButton btnGuardar;
        private DefaultTableModel modeloTabla;
        private int fila;

        public EditarConexion(DefaultTableModel modeloTabla, int fila) {
            this.modeloTabla = modeloTabla;
            this.fila = fila;

            setTitle("Editar Conexión");
            setSize(400, 300);
            setLayout(new GridLayout(6, 2));

            comboEquipo1 = new JComboBox<>(new Equipo[]{(Equipo) modeloTabla.getValueAt(fila, 0)});
            comboEquipo2 = new JComboBox<>(new Equipo[]{(Equipo) modeloTabla.getValueAt(fila, 2)});
            comboPuerto1 = new JComboBox<>(new TipoPuerto[]{(TipoPuerto) modeloTabla.getValueAt(fila, 1)});
            comboPuerto2 = new JComboBox<>(new TipoPuerto[]{(TipoPuerto) modeloTabla.getValueAt(fila, 3)});
            comboCable = new JComboBox<>(new TipoCable[]{(TipoCable) modeloTabla.getValueAt(fila, 4)});

            btnGuardar = new JButton("Guardar");
            btnGuardar.addActionListener(e -> guardarCambios());

            add(new JLabel("Equipo1:"));
            add(comboEquipo1);
            add(new JLabel("TipoPuerto1:"));
            add(comboPuerto1);
            add(new JLabel("Equipo2:"));
            add(comboEquipo2);
            add(new JLabel("TipoPuerto2:"));
            add(comboPuerto2);
            add(new JLabel("TipoCable:"));
            add(comboCable);
            add(btnGuardar);
        }

        private void guardarCambios() {
            modeloTabla.setValueAt(comboEquipo1.getSelectedItem(), fila, 0);
            modeloTabla.setValueAt(comboPuerto1.getSelectedItem(), fila, 1);
            modeloTabla.setValueAt(comboEquipo2.getSelectedItem(), fila, 2);
            modeloTabla.setValueAt(comboPuerto2.getSelectedItem(), fila, 3);
            modeloTabla.setValueAt(comboCable.getSelectedItem(), fila, 4);
            dispose();
        }
    }
}
