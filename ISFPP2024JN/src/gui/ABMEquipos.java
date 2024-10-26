package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
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
		panelEquipo.setBackground(Color.BLACK);
		panelEquipo.setLayout(new BorderLayout());

		// Crear el modelo de la tabla y asignar los nombres de las columnas
		DefaultTableModel tEquipos = new DefaultTableModel();
		String ids[] = { "Codigo", "Descripcion", "Marca", "Modelo", "Tipo de equipo", "Ubicacion", "Puertos",
				"Direcciones IP", "Estado", "Eliminar", "Modificar" };
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

		// Añadir un MouseListener para detectar clics
		tablaEquipos.addMouseListener(new MouseAdapter() {
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
				if (column == 9) {
					if (row >= 0 && row < tEquipos.getRowCount() - 1) // No permitir eliminar la fila del botón
						tEquipos.removeRow(row);

				}

				if (column == 10)
					if ((row >= 0) && (row < tEquipos.getRowCount() - 1))
						modificarEquipo(tEquipos, row);

			}
		});

		tablaEquipos.setBackground(Color.BLACK);
		tablaEquipos.setForeground(NEON_GREEN);
		tablaEquipos.setGridColor(NEON_GREEN);

		// Personalizar la cabecera
		JTableHeader header = tablaEquipos.getTableHeader();
		header.setBackground(Color.BLACK); // Fondo de la cabecera
		header.setForeground(NEON_GREEN); // Color del texto de la cabecera
		header.setFont(header.getFont().deriveFont(Font.BOLD, 14f)); // Fuente en negrita para la cabecera

		// Configurar el JScrollPane para contener la tabla
		JScrollPane pane = new JScrollPane(tablaEquipos);
		pane.setBackground(Color.BLACK); // Color de fondo del JScrollPane
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
		JDialog dialog = new JDialog((Frame) null, "Modificar equipo", true);
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
			camposTexto[i] = new JTextField();
			panelFormulario.add(new JLabel(etiquetas[i] + ":"));
			panelFormulario.add(camposTexto[i]);
		}

		// Botón para confirmar la adición
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.setBackground(Color.BLACK);
		btnAgregar.setForeground(NEON_GREEN);

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

		// Agregar el botón al formulario
		panelFormulario.add(btnAgregar);

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

		// Crear campos de texto y agregar etiquetas y campos al panel
		JTextField[] textFields = new JTextField[labels.length];
		for (int i = 0; i < labels.length; i++) {
			textFields[i] = new JTextField(currentValues[i]);
			panelFormulario.add(new JLabel(labels[i] + ":"));
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

		// Agregar el botón al formulario
		panelFormulario.add(btnModificar);

		dialog.add(panelFormulario);
		dialog.setVisible(true);
		dialog.setResizable(false);
	}

}
