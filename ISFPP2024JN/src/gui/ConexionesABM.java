package gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import modelo.Conexion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConexionesABM extends JPanel {
	private static final Color NEON_GRAY = new Color(60, 60, 60);
	private static final Color NEON_GREEN = new Color(57, 255, 20);
	private static final int ANCHO_VENTANA_PRINCIPAL = 800;
	private static final int LARGO_VENTANA_PRINCIPAL = 600;

	private DefaultTableModel tableModel;
	private JTable table;
	private JComboBox<String> comboBoxEquipo1;
	private JComboBox<String> comboBoxEquipo2;
	private JComboBox<String> comboBoxTipoPuerto1;
	private JComboBox<String> comboBoxTipoPuerto2;
	private JComboBox<String> comboBoxtipocable;
	private static List<Conexion> conexiones;
	private JButton btnActualizar;
	private JButton btnModificar;
	private JButton btnAgregar;
	private JButton btnBorrar;

	public ConexionesABM(List<Conexion> conexiones) {
		setBackground(Color.BLACK);
		setLayout(null);
		this.conexiones = conexiones;
		itemConexion(); // Inicializa los componentes llamando al método itemConexion
	}

	private void itemConexion() {
		comboBoxEquipo1 = crearComboBox(new JComboBox<>(new String[] { "Opción 1", "Opción 2", "Opción 3" }));
		comboBoxEquipo2 = crearComboBox(new JComboBox<>(new String[] { "Opción 1", "Opción 2", "Opción 3" }));
		comboBoxTipoPuerto1 = crearComboBox(new JComboBox<>(new String[] { "Opción 1", "Opción 2", "Opción 3" }));
		comboBoxTipoPuerto2 = crearComboBox(new JComboBox<>(new String[] { "Opción 1", "Opción 2", "Opción 3" }));
		comboBoxtipocable = crearComboBox(new JComboBox<>(new String[] { "Opción 1", "Opción 2", "Opción 3" }));

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

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 160, 740, 350);
		scrollPane.setBorder(new LineBorder(NEON_GREEN, 2));
		scrollPane.getViewport().setBackground(Color.BLACK);

		btnAgregar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String equipo1 = (String) comboBoxEquipo1.getSelectedItem();
				String tipoPuerto1 = (String) comboBoxTipoPuerto1.getSelectedItem();
				String equipo2 = (String) comboBoxEquipo2.getSelectedItem();
				String tipoPuerto2 = (String) comboBoxTipoPuerto2.getSelectedItem();
				String tipoCable = (String) comboBoxtipocable.getSelectedItem();
				tableModel.addRow(new Object[] { equipo1, tipoPuerto1, equipo2, tipoPuerto2, tipoCable });
			}
		});

		btnBorrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					tableModel.removeRow(selectedRow);
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
					tableModel.setValueAt(comboBoxEquipo1.getSelectedItem(), selectedRow, 0);
					tableModel.setValueAt(comboBoxTipoPuerto1.getSelectedItem(), selectedRow, 1);
					tableModel.setValueAt(comboBoxEquipo2.getSelectedItem(), selectedRow, 2);
					tableModel.setValueAt(comboBoxTipoPuerto2.getSelectedItem(), selectedRow, 3);
					tableModel.setValueAt(comboBoxtipocable.getSelectedItem(), selectedRow, 4);

					btnActualizar.setVisible(false);
					btnAgregar.setVisible(true);
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

	public static void main(String[] args) {
		JFrame frame = new JFrame("Conexiones ABM");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(ANCHO_VENTANA_PRINCIPAL, LARGO_VENTANA_PRINCIPAL);
		frame.add(new ConexionesABM(conexiones));
		frame.setVisible(true);
	}
}
