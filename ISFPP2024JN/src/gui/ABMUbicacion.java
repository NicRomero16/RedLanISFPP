package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controlador.Coordinador;
import modelo.Equipo;
import modelo.Ubicacion;

public class ABMUbicacion extends JPanel {

	private TreeMap<String, Ubicacion> ubicaciones;
	private Coordinador coordinador;
	private static final Color NEON_GREEN = new Color(57, 255, 20);

	public ABMUbicacion(TreeMap<String, Ubicacion> ubicaciones, Coordinador coordinador) {
		super();
		this.ubicaciones = ubicaciones;
		this.coordinador = coordinador;
		crearPanelUbicaciones();
	}

	public void crearPanelUbicaciones() {
		// Crear el panel principal para el equipo
		JPanel panelUbicaciones = new JPanel();
		panelUbicaciones.setLayout(new BorderLayout());

		// Crear el modelo de la tabla y asignar los nombres de las columnas
		String ids[] = { "Codigo", "Descripcion" };
		DefaultTableModel tUbicaciones = new DefaultTableModel();
		tUbicaciones.setColumnIdentifiers(ids);
		tUbicaciones.setRowCount(0);

		for (Ubicacion ubicacion : ubicaciones.values()) {
			Object[] fila = { ubicacion.getCodigo(), ubicacion.getDescripcion() };
			tUbicaciones.addRow(fila);
		}
		// Inicializar la tabla y asignar el modelo
		JTable tablaUbicaciones = new JTable(tUbicaciones) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Hacer que ninguna celda sea editable
			}
		};
		tablaUbicaciones.setCellSelectionEnabled(true);
		tablaUbicaciones.setBackground(Color.BLACK); // Fondo de la tabla
		tablaUbicaciones.setForeground(NEON_GREEN); // Letras de la tabla
		tablaUbicaciones.setGridColor(NEON_GREEN); // Bordes de la tabla

		// Personalizar la cabecera
		JTableHeader header = tablaUbicaciones.getTableHeader();
		header.setBackground(Color.BLACK);
		header.setForeground(NEON_GREEN);
		header.setFont(header.getFont().deriveFont(Font.BOLD, 14f)); // Fuente en negrita para la cabecera

		// Configurar el JScrollPane para contener la tabla
		JScrollPane pane = new JScrollPane(tablaUbicaciones);
		pane.setBackground(Color.BLACK);
		pane.getViewport().setBackground(Color.BLACK);

		panelUbicaciones.add(pane, BorderLayout.CENTER);

		JButton btnAgregarUbicacion = new JButton("Agregar Ubicacion");
		btnAgregarUbicacion.addActionListener(e -> {
			JDialog dialog = new JDialog((Frame) null, "Agregar ubicacion", true);
			dialog.setTitle("Agregar ubicacion");
			dialog.setSize(350, 150);
			dialog.setLocationRelativeTo(null);

			JPanel panelFormulario = new JPanel();
			panelFormulario.setLayout(new GridLayout(0, 2));
			panelFormulario.setBackground(Color.BLACK);
			panelFormulario.setForeground(NEON_GREEN);

			JLabel labelCodigo = new JLabel("Codigo de ubicacion");
			JTextField campoCodigo = new JTextField();
			crearJTextField(labelCodigo, panelFormulario, campoCodigo);

			JLabel labelDescripcion = new JLabel("Descripcion de la ubicacion");
			JTextField campoDescripcion = new JTextField();
			crearJTextField(labelDescripcion, panelFormulario, campoDescripcion);

			JButton btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(ev -> {
				if (campoCodigo.getText() == null || campoCodigo.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "El codigo de ubicacion no puede ser nulo", "Advertencia",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (ubicaciones.containsKey(campoCodigo.getText())) {
					JOptionPane.showMessageDialog(null, "La ubicacion ya existe", "Advertencia",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				Ubicacion ubicacion = new Ubicacion(campoCodigo.getText(), campoDescripcion.getText());

				if (!campoCodigo.getText().trim().equals("")) {
					Object[] fila = { campoCodigo.getText(), campoDescripcion.getText() };
					tUbicaciones.addRow(fila);
					coordinador.agregarUbicacion(ubicacion);
				}

				dialog.dispose();
			});

			JButton btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(ev -> {
				campoCodigo.setText("");
				dialog.dispose();
			});

			panelFormulario.add(btnAceptar);
			panelFormulario.add(btnCancelar);

			dialog.add(panelFormulario);
			dialog.setResizable(false);
			dialog.setVisible(true);
		});

		JButton btnEliminarUbicacion = new JButton("Eliminar Ubicacion");
		btnEliminarUbicacion.addActionListener(e -> {
			int row = tablaUbicaciones.getSelectedRow();
			String codigo = null;
			int confirmacion = 0;

			if (row == -1) {
				JOptionPane.showMessageDialog(null, "Seleccione una ubicación", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				codigo = tablaUbicaciones.getValueAt(row, 0).toString();
				confirmacion = JOptionPane.showConfirmDialog(null,
						"¿Estás seguro de que deseas eliminar esta ubicación?", "Confirmación de eliminación",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			}

			if (codigo != null && !codigo.trim().isEmpty()) {
				Ubicacion ubicacion = coordinador.buscarUbicacion(codigo);

				if (confirmacion == JOptionPane.YES_OPTION) {
					TreeMap<String, Equipo> equipos = coordinador.listarEquipos();
					boolean sePuedeEliminar = true;

					for (Equipo equipo : equipos.values())
						if (equipo.getUbicacion().equals(ubicacion)) {
							sePuedeEliminar = false;
							JOptionPane.showMessageDialog(null,
									"La ubicacion no se puede eliminar porque esta asociada a un equipo", "Erro",
									JOptionPane.ERROR_MESSAGE);
						}

					if (sePuedeEliminar) {
						tUbicaciones.removeRow(row);
						coordinador.eliminarUbicacion(codigo);
					}
				}
			}
		});

		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new BorderLayout()); // Usamos BorderLayout para organizar los botones
		panelBotones.add(btnAgregarUbicacion, BorderLayout.NORTH);
		panelBotones.add(btnEliminarUbicacion, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		this.add(panelUbicaciones, BorderLayout.CENTER);
		this.add(panelBotones, BorderLayout.SOUTH);
	}

	public void crearJTextField(JLabel label, JPanel panel, JTextField text) {
		label.setForeground(NEON_GREEN);
		text.setBackground(Color.BLACK);
		text.setForeground(NEON_GREEN);
		text.setCaretColor(Color.GREEN);
		text.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
		panel.add(label);
		panel.add(text);
	}

}
