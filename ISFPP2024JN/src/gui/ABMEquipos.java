package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import modelo.Equipo;

public class ABMEquipos extends JPanel {

	private TreeMap<String, Equipo> equipos;
	private static final Color NEON_GREEN = new Color(57, 255, 20);

	public ABMEquipos(TreeMap<String, Equipo> equipos) {
		super();
		this.equipos = new TreeMap<String, Equipo>();
		crearPanelEquipo(); // Llamar al método para crear el panel de equipo
	}

	public void crearPanelEquipo() {
		// Crear el panel principal para el equipo
		JPanel panelEquipo = new JPanel();
		panelEquipo.setLayout(new BorderLayout());

		// Crear el modelo de la tabla y asignar los nombres de las columnas
		String ids[] = { "Codigo", "Descripcion", "Marca", "Modelo", "Tipo de equipo", "Ubicacion", "Puertos",
				"Direcciones IP", "Estado", "Eliminar", "Modificar" };
		DefaultTableModel tEquipos = new DefaultTableModel();
		tEquipos.setColumnIdentifiers(ids);

		Object[] filaBoton = { "Agregar fila" };
		tEquipos.addRow(filaBoton);

		// Inicializar la tabla y asignar el modelo
		JTable tablaEquipos = new JTable(tEquipos) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Hacer que ninguna celda sea editable
			}
		};
		// Cambiar el modo de selección para que solo se seleccionen celdas
		tablaEquipos.setCellSelectionEnabled(true);
		tablaEquipos.addMouseListener(new MouseAdapter() {// Añadir un MouseListener para detectar clics
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tablaEquipos.rowAtPoint(e.getPoint());
				int column = tablaEquipos.columnAtPoint(e.getPoint());

				// Verificar si se hizo clic en la fila del botón
				if (row == tEquipos.getRowCount() - 1 && column == 0) {
					formularioAgregarEquipo(tEquipos);
				}

				for (int i = 0; i < tEquipos.getRowCount(); i++) {
					if (i < tEquipos.getRowCount() - 1) { // No agregar a la fila del botón
						tEquipos.setValueAt("Eliminar", i, 9); // Colocar "Eliminar" en la nueva columna
					}
				}

				for (int i = 0; i < tEquipos.getRowCount(); i++) {
					if (i < tEquipos.getRowCount() - 1) { // No agregar a la fila del botón
						tEquipos.setValueAt("Modificar", i, 10); // Colocar "Eliminar" en la nueva columna
					}
				}

				// Verificar si se hizo clic en la columna de "Eliminar"
				if (column == 9)
					if (row >= 0 && row < tEquipos.getRowCount() - 1) {// No permitir eliminar la fila del botón

						int confirmacion = JOptionPane.showConfirmDialog(null,
								"¿Estás seguro de que deseas eliminar este equipo?", "Confirmación de eliminación",
								JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

						if (confirmacion == JOptionPane.YES_OPTION) {
							tEquipos.removeRow(row); // Eliminar la fila si se confirma
						}
					}

				if (column == 10)
					if ((row >= 0) && (row < tEquipos.getRowCount() - 1))
						modificarEquipo(tEquipos, row);
			}
		});
		tablaEquipos.setBackground(Color.BLACK); // Fondo de la tabla
		tablaEquipos.setForeground(NEON_GREEN); // Letras de la tabla
		tablaEquipos.setGridColor(NEON_GREEN); // Bordes de la tabla

		// Personalizar la cabecera
		JTableHeader header = tablaEquipos.getTableHeader();
		header.setBackground(Color.BLACK); // Fondo de la cabecera
		header.setForeground(NEON_GREEN); // Color del texto de la cabecera
		header.setFont(header.getFont().deriveFont(Font.BOLD, 14f)); // Fuente en negrita para la cabecera

		// Configurar el JScrollPane para contener la tabla
		JScrollPane pane = new JScrollPane(tablaEquipos);
		pane.setBackground(Color.BLACK);
		pane.getViewport().setBackground(Color.BLACK); // Color de fondo del viewport

		// Añadir el panel de scroll (con la tabla) al panel principal
		panelEquipo.add(pane, BorderLayout.CENTER);

		// Agregar el panelEquipo a la instancia actual de ABMEquipos
		this.setLayout(new BorderLayout());
		this.add(panelEquipo, BorderLayout.CENTER);
	}

	public void formularioAgregarEquipo(DefaultTableModel table) {
		// Aquí puedes abrir un formulario para agregar nuevos datos
		// Crear un nuevo JDialog para el formulario
		JDialog dialog = new JDialog((Frame) null, "Agregar equipo", true);
		dialog.setTitle("Agregar Nuevo Equipo");
		dialog.setSize(400, 300);
		dialog.setLocationRelativeTo(null); // Centra el diálogo

		// Aquí puedes agregar los componentes necesarios al diálogo
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new GridLayout(0, 2)); // Usar un GridLayout para el formulario
		panelFormulario.setBackground(Color.BLACK);
		panelFormulario.setForeground(NEON_GREEN);

		// Crear etiquetas y campos en arreglos
		String[] etiquetas = { "Código", "Descripción", "Marca", "Modelo", "Tipo de equipo", "Ubicación", "Puertos",
				"Direcciones IP", "Estado" };
		JTextField[] camposTexto = new JTextField[etiquetas.length];
		// Inicializar el panel y los campos
		for (int i = 0; i < etiquetas.length; i++) {
			JLabel label = new JLabel(etiquetas[i] + ":");
			label.setForeground(NEON_GREEN);
			camposTexto[i] = new JTextField();
			camposTexto[i].setBackground(Color.BLACK);
			camposTexto[i].setForeground(NEON_GREEN);
			camposTexto[i].setCaretColor(Color.GREEN); // Color del cursor en el JTextField
			camposTexto[i].setBorder(BorderFactory.createLineBorder(Color.GREEN, 1)); // Borde verde
			panelFormulario.add(label);
			panelFormulario.add(camposTexto[i]);
		}

		// Botón para confirmar la adición
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(e -> {
			// Crear un arreglo de objetos para la nueva fila con los valores de cada
			// JTextField
			Object[] nuevaFila = new Object[camposTexto.length];

			for (int i = 0; i < camposTexto.length; i++) {
				nuevaFila[i] = camposTexto[i].getText();
			}

			// Agregar la nueva fila al modelo de la tabla
			table.insertRow(table.getRowCount() - 1, nuevaFila);

			// Cerrar el diálogo después de agregar
			dialog.dispose();
		});

		JButton cancelar = crearBotonCancelar(dialog);

		// Agregar el botón al formulario
		panelFormulario.add(btnAgregar);
		panelFormulario.add(cancelar);
		dialog.add(panelFormulario);
		dialog.setVisible(true); // Muestra el diálogo
		dialog.setResizable(false);
	}

	private void modificarEquipo(DefaultTableModel tEquipos, int row) {
		// Crear un formulario para editar el equipo
		JDialog dialog = new JDialog((Frame) null, "Modificar equipo", true);
		dialog.setTitle("Modificar Equipo");
		dialog.setSize(400, 300);
		dialog.setLocationRelativeTo(null); // Centra el diálogo

		// Obtener los datos actuales de la fila
		String[] labels = { "Código", "Descripción", "Marca", "Modelo", "Tipo de equipo", "Ubicación", "Puertos",
				"Direcciones IP", "Estado" };

		String[] currentValues = { (String) tEquipos.getValueAt(row, 0), (String) tEquipos.getValueAt(row, 1),
				(String) tEquipos.getValueAt(row, 2), (String) tEquipos.getValueAt(row, 3),
				(String) tEquipos.getValueAt(row, 4), (String) tEquipos.getValueAt(row, 5),
				(String) tEquipos.getValueAt(row, 6), (String) tEquipos.getValueAt(row, 7),
				(String) tEquipos.getValueAt(row, 8) };

		// Crear un panel para el formulario
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new GridLayout(labels.length + 1, 2));
		panelFormulario.setBackground(Color.BLACK);

		// Crear campos de texto y agregar etiquetas y campos al panel
		JTextField[] textFields = new JTextField[labels.length];
		for (int i = 0; i < labels.length; i++) {
			JLabel label = new JLabel(labels[i] + ":");
			label.setForeground(NEON_GREEN);
			textFields[i] = new JTextField(currentValues[i]);
			textFields[i].setBackground(Color.BLACK);
			textFields[i].setForeground(NEON_GREEN);
			textFields[i].setCaretColor(NEON_GREEN); // Color del cursor en el JTextField
			textFields[i].setBorder(BorderFactory.createLineBorder(Color.GREEN, 1)); // Borde verde
			panelFormulario.add(label);
			panelFormulario.add(textFields[i]);
		}

		// Botón para confirmar la modificación
		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(evt -> {
			// Actualizar la fila en el modelo
			for (int i = 0; i < textFields.length; i++) {
				tEquipos.setValueAt(textFields[i].getText(), row, i);
			}
			dialog.dispose(); // Cerrar el diálogo después de modificar
		});

		JButton cancelar = crearBotonCancelar(dialog);

		// Agregar el botón al formulario
		panelFormulario.add(btnModificar);
		panelFormulario.add(cancelar);
		dialog.add(panelFormulario);
		dialog.setVisible(true);
		dialog.setResizable(false);
	}

	public JButton crearBotonCancelar(JDialog dialog) {
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		return btnCancelar;
	}
}
