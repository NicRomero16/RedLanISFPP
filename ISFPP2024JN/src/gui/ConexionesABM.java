package gui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoPuerto;
import modelo.TipoCable;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

public class ConexionesABM extends JPanel {
    private static final Color NEON_GRAY = new Color(60, 60, 60);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private int ANCHO;
    private int ALTO;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> comboBoxEquipo1;
    private JComboBox<String> comboBoxEquipo2;
    private JComboBox<String> comboBoxTipoPuerto1;
    private JComboBox<String> comboBoxTipoPuerto2;
    private JComboBox<String> comboBoxtipocable;
    private List<Conexion> conexiones;
    private JButton btnActualizar;
    private JButton btnModificar;
    private JButton btnAgregar;
    private JButton btnBorrar;
    private JButton btnCancelar;
    private Coordinador coordinador;

    public ConexionesABM(Coordinador coordinador, int ANCHO, int ALTO) {
        setBackground(Color.BLACK);
        setLayout(null);
        this.coordinador = coordinador;
        this.conexiones = coordinador.listarConexiones();
        this.ANCHO = ANCHO;
        this.ALTO = ALTO;
        inicializarComponentes();
        inicializarEventos();
        cargarTablaConexiones();
        agregarComponentes();
    }

    private void inicializarComponentes() {
        String[] listaDeEquipos = coordinador.devolverEquipoCodigos();
        String[] listaDeTipoCables = coordinador.devolverTipoCableCodigo();

        comboBoxEquipo1 = crearComboBox(new JComboBox<>(listaDeEquipos));
        comboBoxEquipo2 = crearComboBox(new JComboBox<>(listaDeEquipos));
        comboBoxTipoPuerto1 = crearComboBox(new JComboBox<>());
        comboBoxTipoPuerto2 = crearComboBox(new JComboBox<>());
        comboBoxtipocable = crearComboBox(new JComboBox<>(listaDeTipoCables));

        comboBoxEquipo1.setBounds((int)(ANCHO*0.025), (int)(ALTO*0.067), (int)(ANCHO*0.1875), (int)(ALTO*0.05));
        comboBoxEquipo2.setBounds((int)(ANCHO*0.25), (int)(ALTO*0.067), (int)(ANCHO*0.1875), (int)(ALTO*0.05));
        comboBoxTipoPuerto1.setBounds((int)(ANCHO*0.025), (int)(ALTO*0.2), (int)(ANCHO*0.1875), (int)(ALTO*0.05));
        comboBoxTipoPuerto2.setBounds((int)(ANCHO*0.25), (int)(ALTO*0.2), (int)(ANCHO*0.1875), (int)(ALTO*0.05));
        comboBoxtipocable.setBounds((int)(ANCHO*0.475), (int)(ALTO*0.133), (int)(ANCHO*0.1875), (int)(ALTO*0.05));

        btnAgregar = crearBoton("Agregar", (int)(ANCHO*0.6875), (int)(ALTO*0.067));
        btnBorrar = crearBoton("Borrar", (int)(ANCHO*0.85), (int)(ALTO*0.067));
        btnModificar = crearBoton("Modificar", (int)(ANCHO*0.6875), (int)(ALTO*0.167));
        btnActualizar = crearBoton("Actualizar", (int)(ANCHO*0.85), (int)(ALTO*0.167));
        btnActualizar.setVisible(false);
        btnCancelar = crearBoton("Cancelar", (int)(ANCHO*0.85), (int)(ALTO*0.067));
        btnCancelar.setVisible(false);

        String[] columnNames = { "Equipo 1", "Tipo Puerto 1", "Equipo 2", "Tipo Puerto 2", "Tipo Cable" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setBackground(Color.BLACK);
        table.setForeground(NEON_GREEN);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        JTableHeader header = table.getTableHeader();
        header.setBackground(NEON_GRAY);
        header.setForeground(NEON_GREEN);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBorder(new LineBorder(NEON_GREEN, 2));
        table.setGridColor(NEON_GREEN);
        table.setShowGrid(true);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
    }

    private void inicializarEventos() {
        comboBoxEquipo1.addActionListener(e -> actualizarComboTipoPuerto(comboBoxEquipo1, comboBoxTipoPuerto1));
        comboBoxEquipo2.addActionListener(e -> actualizarComboTipoPuerto(comboBoxEquipo2, comboBoxTipoPuerto2));

        btnAgregar.addActionListener(e -> agregarConexion());
        btnBorrar.addActionListener(e -> borrarConexion());
        btnModificar.addActionListener(e -> prepararModificacion());
        btnActualizar.addActionListener(e -> actualizarConexion());
        btnCancelar.addActionListener(e -> cancelarModificacion());
    }

    private void agregarConexion() {
        StringBuilder mensajesError = new StringBuilder();
        String equipo1 = (String) comboBoxEquipo1.getSelectedItem();
        String tipoPuerto1 = (String) comboBoxTipoPuerto1.getSelectedItem();
        String equipo2 = (String) comboBoxEquipo2.getSelectedItem();
        String tipoPuerto2 = (String) comboBoxTipoPuerto2.getSelectedItem();
        String tipoCable = (String) comboBoxtipocable.getSelectedItem();

        if (!validarCampos(equipo1, tipoPuerto1, equipo2, tipoPuerto2, tipoCable, mensajesError)) return;

        Equipo e1 = coordinador.buscarEquipo(equipo1);
        TipoPuerto tP1 = coordinador.buscarTipoPuerto(tipoPuerto1);
        Equipo e2 = coordinador.buscarEquipo(equipo2);
        TipoPuerto tP2 = coordinador.buscarTipoPuerto(tipoPuerto2);
        TipoCable tC = coordinador.buscarTipoCable(tipoCable);

        if (e1.equals(e2)) {
            mostrarMensaje("Error: No se permite establecer una conexión entre el mismo equipo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (coordinador.existeConexion(e1, e2)) {
            mostrarMensaje("Error: Ya existe una conexión entre estos equipos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (coordinador.getPuertosDisponibles(e1) <= 0) {
            mensajesError.append("Error: No hay puertos disponibles en el equipo " + e1.getCodigo() + ".\n");
        }
        if (coordinador.getPuertosDisponibles(e2) <= 0) {
            mensajesError.append("Error: No hay puertos disponibles en el equipo " + e2.getCodigo() + ".\n");
        }

        if (mensajesError.length() > 0) {
            mostrarMensaje(mensajesError.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Conexion nuevaConexion = new Conexion(e1, tP1, e2, tP2, tC);

        try {
            coordinador.agregarConexion(nuevaConexion);
            tableModel.addRow(new Object[] { equipo1, tipoPuerto1, equipo2, tipoPuerto2, tipoCable });
            JOptionPane.showMessageDialog(null, "Conexión agregada exitosamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error inesperado al agregar la conexión: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrarConexion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de borrar esta conexion?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            Conexion conexion = obtenerConexionDeFila(selectedRow);

            if (!coordinador.existeConexion(conexion.getEquipo1(), conexion.getEquipo2())) {
                JOptionPane.showMessageDialog(null, "La conexión no existe en la red.");
                return;
            }

            try {
                coordinador.borrarConexion(conexion);
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(null, "Conexion borrada con éxito");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar la conexión: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una conexión para borrar.");
        }
    }

    private void prepararModificacion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String equipo1 = (String) tableModel.getValueAt(selectedRow, 0);
            String tipoPuerto1 = (String) tableModel.getValueAt(selectedRow, 1);
            String equipo2 = (String) tableModel.getValueAt(selectedRow, 2);
            String tipoPuerto2 = (String) tableModel.getValueAt(selectedRow, 3);

            comboBoxEquipo1.setSelectedItem(equipo1);
            comboBoxEquipo2.setSelectedItem(equipo2);

            actualizarComboTipoPuerto(comboBoxEquipo1, comboBoxTipoPuerto1);
            actualizarComboTipoPuerto(comboBoxEquipo2, comboBoxTipoPuerto2);

            comboBoxTipoPuerto1.setSelectedItem(tipoPuerto1);
            comboBoxTipoPuerto2.setSelectedItem(tipoPuerto2);

            btnActualizar.setVisible(true);
            btnCancelar.setVisible(true);
            btnAgregar.setVisible(false);
            btnModificar.setEnabled(false);
            btnBorrar.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una conexión para modificar.");
        }
    }

    private void actualizarConexion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            StringBuilder mensajesError = new StringBuilder();
            String nuevoEquipo1 = (String) comboBoxEquipo1.getSelectedItem();
            String nuevoTipoPuerto1 = (String) comboBoxTipoPuerto1.getSelectedItem();
            String nuevoEquipo2 = (String) comboBoxEquipo2.getSelectedItem();
            String nuevoTipoPuerto2 = (String) comboBoxTipoPuerto2.getSelectedItem();
            String nuevoTipoCable = (String) comboBoxtipocable.getSelectedItem();

            Equipo e1 = coordinador.buscarEquipo(nuevoEquipo1);
            TipoPuerto tP1 = coordinador.buscarTipoPuerto(nuevoTipoPuerto1);
            Equipo e2 = coordinador.buscarEquipo(nuevoEquipo2);
            TipoPuerto tP2 = coordinador.buscarTipoPuerto(nuevoTipoPuerto2);
            TipoCable tC = coordinador.buscarTipoCable(nuevoTipoCable);

            // --- Ignorar la propia conexión original ---
            Conexion conexionOriginal = obtenerConexionDeFila(selectedRow);

            coordinador.borrarConexion(conexionOriginal);

            if (coordinador.getPuertosDisponibles(e1) <= 0) {
                mensajesError.append("Error: No hay puertos disponibles en el equipo " + e1.getCodigo() + ".\n");
            }
            if (coordinador.getPuertosDisponibles(e2) <= 0) {
                mensajesError.append("Error: No hay puertos disponibles en el equipo " + e2.getCodigo() + ".\n");
            }

            if (nuevoEquipo1.equals(nuevoEquipo2)) {
                mensajesError.append("Error: El equipo 1 no puede ser igual al equipo 2.\n");
            }

            if (coordinador.existeConexion(e1, e2)) {
                mensajesError.append("Error: Ya existe una conexión entre estos equipos.\n");
            }

            if (mensajesError.length() > 0) {
                // Si hay error, restaurar la conexión original
                try {
                    coordinador.agregarConexion(conexionOriginal);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                mostrarMensaje(mensajesError.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conexion nuevaConexion = new Conexion(e1, tP1, e2, tP2, tC);

                coordinador.agregarConexion(nuevaConexion);

                tableModel.setValueAt(nuevoEquipo1, selectedRow, 0);
                tableModel.setValueAt(nuevoTipoPuerto1, selectedRow, 1);
                tableModel.setValueAt(nuevoEquipo2, selectedRow, 2);
                tableModel.setValueAt(nuevoTipoPuerto2, selectedRow, 3);
                tableModel.setValueAt(nuevoTipoCable, selectedRow, 4);

                JOptionPane.showMessageDialog(null, "Conexión actualizada exitosamente.");
            } catch (Exception ex) {
                try {
                    coordinador.agregarConexion(conexionOriginal);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Error al actualizar la conexión: " + ex.getMessage());
            }

            btnActualizar.setVisible(false);
            btnAgregar.setVisible(true);
            btnModificar.setEnabled(true);
            btnBorrar.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una conexión para actualizar.");
        }
    }

    private void cancelarModificacion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String equipo1 = (String) tableModel.getValueAt(selectedRow, 0);
            String tipoPuerto1 = (String) tableModel.getValueAt(selectedRow, 1);
            String equipo2 = (String) tableModel.getValueAt(selectedRow, 2);
            String tipoPuerto2 = (String) tableModel.getValueAt(selectedRow, 3);

            comboBoxEquipo1.setSelectedItem(equipo1);
            comboBoxEquipo2.setSelectedItem(equipo2);
            comboBoxTipoPuerto1.setSelectedItem(tipoPuerto1);
            comboBoxTipoPuerto2.setSelectedItem(tipoPuerto2);
        }

        btnActualizar.setVisible(false);
        btnCancelar.setVisible(false);
        btnAgregar.setVisible(true);
        btnModificar.setEnabled(true);
        btnBorrar.setVisible(true);
    }

    private void actualizarComboTipoPuerto(JComboBox<String> comboEquipo, JComboBox<String> comboTipoPuerto) {
        comboTipoPuerto.removeAllItems();
        String equipoSeleccionado = (String) comboEquipo.getSelectedItem();
        Equipo equipo = coordinador.buscarEquipo(equipoSeleccionado);
        if (equipo != null) {
            List<String> tiposPuerto = coordinador.obtenerTiposDePuerto(equipo);
            for (String tipoPuerto : tiposPuerto) {
                comboTipoPuerto.addItem(tipoPuerto);
            }
        }
    }

    private boolean validarCampos(String equipo1, String tipoPuerto1, String equipo2, String tipoPuerto2, String tipoCable, StringBuilder mensajesError) {
        if (equipo1 == null || equipo1.isEmpty() || tipoPuerto1 == null || tipoPuerto1.isEmpty()
                || equipo2 == null || equipo2.isEmpty() || tipoPuerto2 == null || tipoPuerto2.isEmpty()
                || tipoCable == null || tipoCable.isEmpty()) {
            mostrarMensaje("Error: Todos los campos deben ser seleccionados(equipo1, equipo2 y tipoCable).",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private Conexion obtenerConexionDeFila(int row) {
        String equipo1 = (String) tableModel.getValueAt(row, 0);
        String tipoPuerto1 = (String) tableModel.getValueAt(row, 1);
        String equipo2 = (String) tableModel.getValueAt(row, 2);
        String tipoPuerto2 = (String) tableModel.getValueAt(row, 3);
        String tipoCable = (String) tableModel.getValueAt(row, 4);

        Equipo e1 = coordinador.buscarEquipo(equipo1);
        TipoPuerto tP1 = coordinador.buscarTipoPuerto(tipoPuerto1);
        Equipo e2 = coordinador.buscarEquipo(equipo2);
        TipoPuerto tP2 = coordinador.buscarTipoPuerto(tipoPuerto2);
        TipoCable tC = coordinador.buscarTipoCable(tipoCable);

        return new Conexion(e1, tP1, e2, tP2, tC);
    }

    private void cargarTablaConexiones() {
        for (Conexion conexion : conexiones) {
            String[] conecAux = {
                conexion.getEquipo1().getCodigo(),
                conexion.getTipoPuerto1().getCodigo(),
                conexion.getEquipo2().getCodigo(),
                conexion.getTipoPuerto2().getCodigo(),
                conexion.getTipoCable().getCodigo()
            };
            tableModel.insertRow(tableModel.getRowCount(), conecAux);
        }
    }

    private void agregarComponentes() {
        JLabel labelEquipo1 = crearLabel("Equipo 1");
        JLabel labelEquipo2 = crearLabel("Equipo 2");
        JLabel labelTipoPuerto1 = crearLabel("Tipo Puerto 1");
        JLabel labelTipoPuerto2 = crearLabel("Tipo Puerto 2");
        JLabel labelTipoCable = crearLabel("Tipo Cable");

        labelEquipo1.setBounds((int)(ANCHO*0.025), (int)(ALTO*0.025), (int)(ANCHO*0.125), (int)(ALTO*0.033));
        labelEquipo2.setBounds((int)(ANCHO*0.25), (int)(ALTO*0.025), (int)(ANCHO*0.125), (int)(ALTO*0.033));
        labelTipoPuerto1.setBounds((int)(ANCHO*0.025), (int)(ALTO*0.158), (int)(ANCHO*0.125), (int)(ALTO*0.033));
        labelTipoPuerto2.setBounds((int)(ANCHO*0.25), (int)(ALTO*0.158), (int)(ANCHO*0.125), (int)(ALTO*0.033));
        labelTipoCable.setBounds((int)(ANCHO*0.475), (int)(ALTO*0.092), (int)(ANCHO*0.125), (int)(ALTO*0.033));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds((int)(ANCHO*0.025), (int)(ALTO*0.267), (int)(ANCHO*0.95), (int)(ALTO*0.583));
        scrollPane.setBorder(new LineBorder(NEON_GREEN, 2));
        scrollPane.getViewport().setBackground(Color.BLACK);

        add(labelEquipo1);
        add(labelEquipo2);
        add(labelTipoPuerto1);
        add(labelTipoPuerto2);
        add(labelTipoCable);
        add(comboBoxEquipo1);
        add(comboBoxEquipo2);
        add(comboBoxTipoPuerto1);
        add(comboBoxTipoPuerto2);
        add(comboBoxtipocable);
        add(btnAgregar);
        add(btnBorrar);
        add(btnModificar);
        add(btnActualizar);
        add(btnCancelar);
        add(scrollPane);
    }

    private JComboBox<String> crearComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(Color.BLACK);
        comboBox.setForeground(NEON_GREEN);
        return comboBox;
    }

    private JButton crearBoton(String text, int x, int y) {
        JButton boton = new JButton(text);
        boton.setBounds(x, y, (int)(ANCHO*0.125), (int)(ALTO*0.05));
        boton.setForeground(Color.BLACK);
        boton.setBackground(NEON_GREEN);
        boton.setBorder(new LineBorder(NEON_GREEN, 2));
        return boton;
    }

    private JLabel crearLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(NEON_GREEN);
        return label;
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipo);
    }
}
