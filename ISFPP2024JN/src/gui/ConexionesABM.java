package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
import modelo.TipoCable;
import modelo.TipoPuerto;

@SuppressWarnings("serial")
public class ConexionesABM extends JPanel {
	private static final Color NEON_GRAY = new Color(60, 60, 60);
	private static final Color NEON_GREEN = new Color(57, 255, 20);
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
	private Coordinador coordinador;

	public ConexionesABM(Coordinador coordinador) {
		setBackground(Color.BLACK);
		setLayout(null);
		this.coordinador = coordinador;
		this.conexiones = coordinador.listarConexiones();
		itemConexion();
	}

	private void itemConexion() {
		String[] listaDeEquipos = coordinador.devolverEquipoCodigos();
		String[] listaDeTipoPuertos = coordinador.devolverTipoPuertoCodigo();
		String[] listaDeTipoCables = coordinador.devolverTipoCableCodigo();

		comboBoxEquipo1 = crearComboBox(new JComboBox<>(listaDeEquipos));
		comboBoxEquipo2 = crearComboBox(new JComboBox<>(listaDeEquipos));
		comboBoxTipoPuerto1 = crearComboBox(new JComboBox<>(listaDeTipoPuertos));
		comboBoxTipoPuerto2 = crearComboBox(new JComboBox<>(listaDeTipoPuertos));
		comboBoxtipocable = crearComboBox(new JComboBox<>(listaDeTipoCables));

		JLabel labelEquipo1 = crearLabel("Equipo 1");
		JLabel labelEquipo2 = crearLabel("Equipo 2");
		JLabel labelTipoPuerto1 = crearLabel("Tipo Puerto 1");
		JLabel labelTipoPuerto2 = crearLabel("Tipo Puerto 2");
		JLabel labelTipoCable = crearLabel("Tipo Cable");

		labelEquipo1.setBounds(20, 15, 100, 20);
		labelEquipo2.setBounds(200, 15, 100, 20);
		labelTipoPuerto1.setBounds(20, 95, 100, 20);
		labelTipoPuerto2.setBounds(200, 95, 100, 20);
		labelTipoCable.setBounds(380, 55, 100, 20);

		comboBoxEquipo1.setBounds(20, 40, 150, 30);
		comboBoxEquipo2.setBounds(200, 40, 150, 30);
		comboBoxTipoPuerto1.setBounds(20, 120, 150, 30);
		comboBoxTipoPuerto2.setBounds(200, 120, 150, 30);
		comboBoxtipocable.setBounds(380, 80, 150, 30);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(550, 40, 100, 30);
		btnAgregar.setForeground(Color.BLACK);
		btnAgregar.setBackground(NEON_GREEN);
		btnAgregar.setBorder(new LineBorder(NEON_GREEN, 2));

		btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(680, 40, 100, 30);
		btnBorrar.setForeground(Color.BLACK);
		btnBorrar.setBackground(NEON_GREEN);
		btnBorrar.setBorder(new LineBorder(NEON_GREEN, 2));

		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(550, 100, 100, 30);
		btnModificar.setForeground(Color.BLACK);
		btnModificar.setBackground(NEON_GREEN);
		btnModificar.setBorder(new LineBorder(NEON_GREEN, 2));

		btnActualizar = new JButton("Actualizar");
		btnActualizar.setBounds(680, 100, 100, 30);
		btnActualizar.setForeground(Color.BLACK);
		btnActualizar.setBackground(NEON_GREEN);
		btnActualizar.setBorder(new LineBorder(NEON_GREEN, 2));
		btnActualizar.setVisible(false);

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

		for (int i = 0; i < conexiones.size(); i++) {
			String[] conecAux = { conexiones.get(i).getEquipo1().getCodigo(),
					conexiones.get(i).getTipoPuerto1().getCodigo(), conexiones.get(i).getEquipo2().getCodigo(),
					conexiones.get(i).getTipoPuerto2().getCodigo(), conexiones.get(i).getTipoCable().getCodigo() };
			tableModel.insertRow(table.getRowCount(), conecAux);
		}

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 160, 740, 350);
		scrollPane.setBorder(new LineBorder(NEON_GREEN, 2));
		scrollPane.getViewport().setBackground(Color.BLACK);

		btnAgregar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder mensajesError = new StringBuilder();
				String equipo1 = (String) comboBoxEquipo1.getSelectedItem();
				String tipoPuerto1 = (String) comboBoxTipoPuerto1.getSelectedItem();
				String equipo2 = (String) comboBoxEquipo2.getSelectedItem();
				String tipoPuerto2 = (String) comboBoxTipoPuerto2.getSelectedItem();
				String tipoCable = (String) comboBoxtipocable.getSelectedItem();

				Equipo e1 = coordinador.buscarEquipo(equipo1);
				TipoPuerto tP1 = coordinador.buscarTipoPuerto(tipoPuerto1);
				Equipo e2 = coordinador.buscarEquipo(equipo2);
				TipoPuerto tP2 = coordinador.buscarTipoPuerto(tipoPuerto2);
				TipoCable tC = coordinador.buscarTipoCable(tipoCable);

				if (e1 == null || e2 == null || tP1 == null || tP2 == null || tC == null) {
					mostrarMensaje(
							"Error: Verifica que todos los datos estén seleccionados y que los equipos sean diferentes.",
							"Error", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (e1.equals(e2)) {
					mostrarMensaje("Error: No se permite establecer una conexión entre el mismo equipo.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (coordinador.existeConexion(e1, e2)) {
					mostrarMensaje("Error: Ya existe una conexión entre estos equipos.", "Error",
							JOptionPane.ERROR_MESSAGE);
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
		});

		btnBorrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {

					int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de borrar esta conexion?",
							"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
					if (confirm != JOptionPane.YES_OPTION) {
						return;
					}

					String equipo1 = (String) tableModel.getValueAt(selectedRow, 0);
					String tipoPuerto1 = (String) tableModel.getValueAt(selectedRow, 1);
					String equipo2 = (String) tableModel.getValueAt(selectedRow, 2);
					String tipoPuerto2 = (String) tableModel.getValueAt(selectedRow, 3);
					String tipoCable = (String) tableModel.getValueAt(selectedRow, 4);

					Equipo e1 = coordinador.buscarEquipo(equipo1);
					TipoPuerto tP1 = coordinador.buscarTipoPuerto(tipoPuerto1);
					Equipo e2 = coordinador.buscarEquipo(equipo2);
					TipoPuerto tP2 = coordinador.buscarTipoPuerto(tipoPuerto2);
					TipoCable tC = coordinador.buscarTipoCable(tipoCable);

					if (!coordinador.existeConexion(e1, e2)) {
						JOptionPane.showMessageDialog(null, "La conexión no existe en la red.");
						return;
					}
					Conexion conexion = new Conexion(e1, tP1, e2, tP2, tC);

					try {
						coordinador.borrarConexion(conexion);
						tableModel.removeRow(selectedRow);
						JOptionPane.showMessageDialog(null, " Conexion borrado con exito");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Error al eliminar la conexión: " + ex.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(null, "Seleccione una conexión para borrar.");
				}
			}
		});

		btnModificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {

					comboBoxEquipo1.setSelectedItem(tableModel.getValueAt(selectedRow, 0));
					comboBoxTipoPuerto1.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
					comboBoxEquipo2.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
					comboBoxTipoPuerto2.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
					comboBoxtipocable.setSelectedItem(tableModel.getValueAt(selectedRow, 4));

					btnActualizar.setVisible(true);
					btnAgregar.setVisible(false);
					btnModificar.setEnabled(false);
				} else {
					JOptionPane.showMessageDialog(null, "Seleccione una conexión para modificar.");
				}
			}
		});

		btnActualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
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

					String equipo1Actual = (String) tableModel.getValueAt(selectedRow, 0);
					String tipoPuerto1Actual = (String) tableModel.getValueAt(selectedRow, 1);
					String equipo2Actual = (String) tableModel.getValueAt(selectedRow, 2);
					String tipoPuerto2Actual = (String) tableModel.getValueAt(selectedRow, 3);
					String tipoCableActual = (String) tableModel.getValueAt(selectedRow, 4);

					Equipo e1a = coordinador.buscarEquipo(equipo1Actual);
					TipoPuerto tP1a = coordinador.buscarTipoPuerto(tipoPuerto1Actual);
					Equipo e2a = coordinador.buscarEquipo(equipo2Actual);
					TipoPuerto tP2a = coordinador.buscarTipoPuerto(tipoPuerto2Actual);
					TipoCable tCa = coordinador.buscarTipoCable(tipoCableActual);

					Conexion conexionActual = coordinador.buscarConexion(e1a, tP1a, e2a, tP2a, tCa);
					if (conexionActual == null) {
						JOptionPane.showMessageDialog(null, "Error: No se encontró la conexión a actualizar.");
						return;
					}

					try {

						Conexion nuevaConexion = new Conexion(e1, tP1, e2, tP2, tC);

						coordinador.borrarConexion(conexionActual);
						coordinador.agregarConexion(nuevaConexion);

						tableModel.setValueAt(nuevoEquipo1, selectedRow, 0);
						tableModel.setValueAt(nuevoTipoPuerto1, selectedRow, 1);
						tableModel.setValueAt(nuevoEquipo2, selectedRow, 2);
						tableModel.setValueAt(nuevoTipoPuerto2, selectedRow, 3);
						tableModel.setValueAt(nuevoTipoCable, selectedRow, 4);

						JOptionPane.showMessageDialog(null, "Conexión actualizada exitosamente.");
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Error al actualizar la conexión: " + ex.getMessage());
					}

					btnActualizar.setVisible(false);
					btnAgregar.setVisible(true);
					btnModificar.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null, "Seleccione una conexión para actualizar.");
				}
			}
		});

		add(scrollPane);
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
	}

	private JComboBox<String> crearComboBox(JComboBox<String> comboBox) {
		comboBox.setBackground(Color.BLACK);
		comboBox.setForeground(NEON_GREEN);
		return comboBox;
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
