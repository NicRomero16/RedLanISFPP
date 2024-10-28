package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controlador.Coordinador;
import modelo.Conexion;
import modelo.Equipo;
import modelo.TipoEquipo;
import modelo.TipoPuerto;
import modelo.Ubicacion;

public class ABMEquipos extends JPanel {

	private TreeMap<String, Equipo> equipos;
	private Coordinador coordinador;
	private static final Color NEON_GREEN = new Color(57, 255, 20);
	private static final Color NEON_GRAY = new Color(30, 30, 30);

	public ABMEquipos(TreeMap<String, Equipo> equipos, Coordinador coordinador) {
		super();
		this.coordinador = coordinador;
		this.equipos = equipos;
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
		tEquipos.setRowCount(0);

		for (Equipo equipo : equipos.values()) {
			Object[] fila = { equipo.getCodigo() != null ? equipo.getCodigo() : "", // Evitar null
					equipo.getDescripcion() != null ? equipo.getDescripcion() : "", // Evitar null
					equipo.getMarca() != null ? equipo.getMarca() : "", // Evitar null
					equipo.getModelo() != null ? equipo.getModelo() : "", // Evitar null
					(equipo.getTipoEquipo() != null ? equipo.getTipoEquipo().getCodigo() : "") + ", "
							+ (equipo.getTipoEquipo() != null ? equipo.getTipoEquipo().getDescripcion() : ""), // Evitar
																												// null
					(equipo.getUbicacion() != null ? equipo.getUbicacion().getCodigo() : "") + ", "
							+ (equipo.getUbicacion() != null ? equipo.getUbicacion().getDescripcion() : ""),
					equipo.getPuertos() != null ? equipo.getPuertos() : "", // Evitar null
					equipo.getDireccionesIP() != null ? equipo.getDireccionesIP() : "", // Evitar null
					equipo.getEstado(), // Evitar null
					"Eliminar", // Placeholder para acciones
					"Modificar" // Placeholder para acciones
			};
			tEquipos.addRow(fila); // Agregar fila a la tabla
		}

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

				for (int i = 0; i < tEquipos.getRowCount(); i++) {
					if (i < tEquipos.getRowCount()) { // No agregar a la fila del botón
						tEquipos.setValueAt("Eliminar", i, 9); // Colocar "Eliminar" en la nueva columna
					}
				}

				for (int i = 0; i < tEquipos.getRowCount(); i++) {
					if (i < tEquipos.getRowCount()) { // No agregar a la fila del botón
						tEquipos.setValueAt("Modificar", i, 10); // Colocar "Eliminar" en la nueva columna
					}
				}

				// Verificar si se hizo clic en la columna de "Eliminar"
				if (column == 9) {
					String codigo = tablaEquipos.getValueAt(row, 0).toString();
					int confirmacion = JOptionPane.showConfirmDialog(null,
							"¿Estás seguro de que deseas eliminar este equipo?", "Confirmación de eliminación",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					Equipo equipo = coordinador.buscarEquipo(codigo);

					if (confirmacion == JOptionPane.YES_OPTION) {
						if (coordinador.tieneConexiones(equipo) == true) {
							List<Conexion> conexiones = coordinador.buscarConexiones(equipo);
							for (Conexion conexion : conexiones)
								coordinador.borrarConexion(conexion);
						}
						tEquipos.removeRow(row); // Eliminar la fila si se confirma
						coordinador.eliminarEquipo(codigo);
						coordinador.cargarDatos();
					}
				}

				if (column == 10) {
					String codigo = tablaEquipos.getValueAt(row, 0).toString();
					Equipo equipo = coordinador.buscarEquipo(codigo);
					modificarEquipo(tEquipos, row, equipo);
				}
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

		JButton btnAgregarEquipo = new JButton("Agregar equipo");
		btnAgregarEquipo.addActionListener(e -> formularioAgregarEquipo(tEquipos));

		// Agregar el panelEquipo a la instancia actual de ABMEquipos
		this.setLayout(new BorderLayout());
		this.add(panelEquipo, BorderLayout.CENTER);
		this.add(btnAgregarEquipo, BorderLayout.SOUTH);
	}

	public void formularioAgregarEquipo(DefaultTableModel table) {

		// Crear un nuevo JDialog para el formulario
		JDialog dialog = new JDialog((Frame) null, "Agregar equipo", true);
		dialog.setTitle("Agregar Nuevo Equipo");
		dialog.setSize(500, 500);
		dialog.setLocationRelativeTo(null); // Centra el diálogo

		// Configurar el panel de formulario
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new GridBagLayout()); // Usar GridBagLayout para mayor flexibilidad
		panelFormulario.setBackground(Color.BLACK);
		panelFormulario.setForeground(NEON_GREEN);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // Espacio entre componentes
		gbc.anchor = GridBagConstraints.WEST; // Alinear a la izquierda

		// Definir los campos de texto
		JTextField campoCodigo = new JTextField(15);
		JTextField campoDescripcion = new JTextField(15);
		JTextField campoMarca = new JTextField(15);
		JTextField campoModelo = new JTextField(15);
		JTextField campoCodTipoEquipo = new JTextField(15);
		JTextField campoDescTipoEquipo = new JTextField(15);
		JTextField campoCodUbicacion = new JTextField(15);
		JTextField campoDescUbicacion = new JTextField(15);
		JTextField campoDireccionesIP = new JTextField(15);
		JTextField campoEstado = new JTextField(15);

		// Método auxiliar para agregar etiquetas y campos
		addLabelAndField(panelFormulario, gbc, "Código de equipo:", campoCodigo);
		addLabelAndField(panelFormulario, gbc, "Descripción del equipo:", campoDescripcion);
		addLabelAndField(panelFormulario, gbc, "Marca del equipo:", campoMarca);
		addLabelAndField(panelFormulario, gbc, "Modelo del equipo:", campoModelo);

		// Tipo de equipo
		addLabelAndField(panelFormulario, gbc, "Código de tipo de equipo:", campoCodTipoEquipo);
		addLabelAndField(panelFormulario, gbc, "Descripción de tipo de equipo:", campoDescTipoEquipo);

		// Ubicación
		addLabelAndField(panelFormulario, gbc, "Código de ubicación:", campoCodUbicacion);
		addLabelAndField(panelFormulario, gbc, "Descripción de la ubicación:", campoDescUbicacion);

		// Puertos
		JLabel labelPuertos = new JLabel("Puertos:");
		labelPuertos.setForeground(NEON_GREEN);
		gbc.gridx = 0;
		gbc.gridy++; // Nueva fila
		panelFormulario.add(labelPuertos, gbc);

		Integer[] cantPuertos = new Integer[50];
		for (int i = 0; i < 50; i++)
			cantPuertos[i] = i + 1;

		JComboBox<Integer> comboBoxPuertos = new JComboBox<>(cantPuertos);
		comboBoxPuertos.setBackground(NEON_GRAY);
		comboBoxPuertos.setForeground(NEON_GREEN);
		gbc.gridx = 1; // Columna del comboBox
		panelFormulario.add(comboBoxPuertos, gbc);

		// Puertos
		JLabel labelTipoPuertos = new JLabel("Tipos de puerto:");
		labelTipoPuertos.setForeground(NEON_GREEN);
		gbc.gridx = 0;
		gbc.gridy++; // Nueva fila
		panelFormulario.add(labelTipoPuertos, gbc);

		TreeMap<String, TipoPuerto> tipoPuerto = coordinador.listarTipoPuertos();
		List<String> tiposPuertos = new ArrayList<String>();
		for (TipoPuerto tipos : tipoPuerto.values()) {
			tiposPuertos.add(tipos.getCodigo());
		}
		String[] listPuertos = new String[tiposPuertos.size()];
		int i = 0;
		for (String tipos : tiposPuertos) {
			listPuertos[i] = tipos;
			i++;
		}

		JComboBox<String> comboBoxTipoPuerto = new JComboBox<String>(listPuertos);
		comboBoxTipoPuerto.setBackground(NEON_GRAY);
		comboBoxTipoPuerto.setForeground(NEON_GREEN);
		gbc.gridx = 1; // Columna del comboBox
		panelFormulario.add(comboBoxTipoPuerto, gbc);

		// Direcciones IP
		addLabelAndField(panelFormulario, gbc, "Direcciones IP:", campoDireccionesIP);

		// Estado
		addLabelAndField(panelFormulario, gbc, "Estado:", campoEstado);

		// Botón para confirmar la adición
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(e -> {
			if (equipos.containsKey(campoCodigo.getText())) {
				JOptionPane.showMessageDialog(null, "El equipo ya existe", "Error", JOptionPane.ERROR_MESSAGE);
				campoCodigo.requestFocus();
				return;
			}

			if (!verificarCampoEstado(campoEstado)) {
				campoEstado.requestFocus();
				return;
			}

			boolean estado = Boolean.parseBoolean(campoEstado.getText());

			// Crear un arreglo de objetos para la nueva fila con los valores de cada
			// JTextField
			Object[] nuevaFila = { campoCodigo.getText(), campoDescripcion.getText(), campoMarca.getText(),
					campoModelo.getText(), campoCodTipoEquipo.getText() + "," + campoDescTipoEquipo.getText(),
					campoCodUbicacion.getText() + "," + campoDescUbicacion.getText(),
					comboBoxPuertos.getSelectedItem() + ", " + comboBoxTipoPuerto.getSelectedItem(),
					campoDireccionesIP.getText(), estado };

			Equipo equipo = new Equipo(campoCodigo.getText(), campoDescripcion.getText(), campoMarca.getText(),
					campoModelo.getText(), new TipoEquipo(campoCodTipoEquipo.getText(), campoDescTipoEquipo.getText()),
					new Ubicacion(campoCodUbicacion.getText(), campoDescUbicacion.getText()), estado);
			coordinador.agregarEquipo(equipo);

			// Agregar la nueva fila al modelo de la tabla
			table.addRow(nuevaFila);

			// Cerrar el diálogo después de agregar
			dialog.dispose();
		});

		JButton cancelar = crearBotonCancelar(dialog);

		// Agregar los botones al panel
		gbc.gridx = 0;
		gbc.gridy++; // Nueva fila
		panelFormulario.add(btnAgregar, gbc);
		gbc.gridx = 1; // Columna del botón cancelar
		panelFormulario.add(cancelar, gbc);

		dialog.add(panelFormulario);
		dialog.setVisible(true); // Muestra el diálogo
		dialog.setResizable(false);
	}

	private void modificarEquipo(DefaultTableModel tEquipos, int row, Equipo equipo) {

		// Crear un formulario para editar el equipo
		JDialog dialog = new JDialog((Frame) null, "Modificar equipo", true);
		dialog.setTitle("Modificar Equipo");
		dialog.setSize(500, 500);
		dialog.setLocationRelativeTo(null); // Centra el diálogo

		// Obtener los datos actuales de la fila
		String codigoActual = equipo.getCodigo();
		String descripcionActual = equipo.getCodigo();
		String marcaActual = equipo.getMarca();
		String modeloActual = equipo.getModelo();
		String codTipoEquipoActual = equipo.getTipoEquipo().getCodigo();
		String descTipoEquipoActual = equipo.getTipoEquipo().getDescripcion();
		String codUbicacionActual = equipo.getUbicacion().getCodigo();
		String descUbicacionActual = equipo.getUbicacion().getDescripcion();
		String puertosActual = tEquipos.getValueAt(row, 6).toString();
		String direccionesIPActual = tEquipos.getValueAt(row, 7).toString();
		String estadoActual = tEquipos.getValueAt(row, 8).toString();

		String codigo = codigoActual;

		// Crear un panel para el formulario
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new GridLayout(0, 2)); // 9 etiquetas y 1 fila para los botones
		panelFormulario.setBackground(Color.BLACK);
		panelFormulario.setForeground(NEON_GREEN);

		// Campos individuales sin bucles
		JLabel labelCodigo = new JLabel("Código de equipo:");
		labelCodigo.setForeground(NEON_GREEN);
		JTextField campoCodigo = new JTextField(codigoActual);
		crearJTextField(campoCodigo);
		panelFormulario.add(labelCodigo);
		panelFormulario.add(campoCodigo);

		JLabel labelDescripcion = new JLabel("Descripción del equipo:");
		labelDescripcion.setForeground(NEON_GREEN);
		JTextField campoDescripcion = new JTextField(descripcionActual);
		crearJTextField(campoDescripcion);
		panelFormulario.add(labelDescripcion);
		panelFormulario.add(campoDescripcion);

		JLabel labelMarca = new JLabel("Marca del equipo:");
		labelMarca.setForeground(NEON_GREEN);
		JTextField campoMarca = new JTextField(marcaActual);
		crearJTextField(campoMarca);
		panelFormulario.add(labelMarca);
		panelFormulario.add(campoMarca);

		JLabel labelModelo = new JLabel("Modelo del equipo:");
		labelModelo.setForeground(NEON_GREEN);
		JTextField campoModelo = new JTextField(modeloActual);
		crearJTextField(campoModelo);
		panelFormulario.add(labelModelo);
		panelFormulario.add(campoModelo);

		JLabel labelTipoEquipo1 = new JLabel("Codigo de tipo de equipo:");
		JLabel labelTipoEquipo2 = new JLabel("Descripcion de tipo de equipo:");
		labelTipoEquipo1.setForeground(NEON_GREEN);
		labelTipoEquipo2.setForeground(NEON_GREEN);

		JTextField campoCodTipoEquipo = new JTextField(codTipoEquipoActual);
		JTextField campoDescTipoEquipo = new JTextField(descTipoEquipoActual);
		crearJTextField(campoCodTipoEquipo);
		crearJTextField(campoDescTipoEquipo);
		panelFormulario.add(labelTipoEquipo1);
		panelFormulario.add(campoCodTipoEquipo);
		panelFormulario.add(labelTipoEquipo2);
		panelFormulario.add(campoDescTipoEquipo);

		JLabel labelCodUbicacion = new JLabel("Codigo de ubicación:");
		JLabel labelDescUbicacion = new JLabel("Descripcion de la ubicacion:");
		labelCodUbicacion.setForeground(NEON_GREEN);
		labelDescUbicacion.setForeground(NEON_GREEN);
		JTextField campoCodUbicacion = new JTextField(codUbicacionActual);
		JTextField campoDescUbicacion = new JTextField(descUbicacionActual);
		crearJTextField(campoCodUbicacion);
		crearJTextField(campoDescUbicacion);
		panelFormulario.add(labelCodUbicacion);
		panelFormulario.add(campoCodUbicacion);
		panelFormulario.add(labelDescUbicacion);
		panelFormulario.add(campoDescUbicacion);

		JLabel labelPuertos = new JLabel("Puertos:");
		labelPuertos.setForeground(NEON_GREEN);
		JTextField campoPuertos = new JTextField(puertosActual);
		crearJTextField(campoPuertos);
		panelFormulario.add(labelPuertos);
		panelFormulario.add(campoPuertos);

		JLabel labelDireccionesIP = new JLabel("Direcciones IP:");
		labelDireccionesIP.setForeground(NEON_GREEN);
		JTextField campoDireccionesIP = new JTextField(direccionesIPActual);
		crearJTextField(campoDireccionesIP);
		panelFormulario.add(labelDireccionesIP);
		panelFormulario.add(campoDireccionesIP);

		JLabel labelEstado = new JLabel("Estado:");
		labelEstado.setForeground(NEON_GREEN);
		JTextField campoEstado = new JTextField(estadoActual);
		crearJTextField(campoEstado);
		panelFormulario.add(labelEstado);
		panelFormulario.add(campoEstado);

		// Botón para confirmar la modificación
		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(e -> {
			// Validar el estado del equipo
			boolean estado = campoEstado.getText().equalsIgnoreCase("true");

			// Crear un nuevo objeto Equipo con los valores modificados
			Equipo equipoModificado = new Equipo(campoCodigo.getText(), campoDescripcion.getText(),
					campoMarca.getText(), campoModelo.getText(),
					new TipoEquipo(campoCodTipoEquipo.getText(), "," + campoDescTipoEquipo),
					new Ubicacion(campoCodUbicacion.getText(), "," + campoDescUbicacion),
					estado);

			// Actualizar el modelo de la tabla
			tEquipos.setValueAt(equipoModificado.getCodigo(), row, 0);
			tEquipos.setValueAt(equipoModificado.getDescripcion(), row, 1);
			tEquipos.setValueAt(equipoModificado.getMarca(), row, 2);
			tEquipos.setValueAt(equipoModificado.getModelo(), row, 3);
			tEquipos.setValueAt(equipoModificado.getTipoEquipo(), row, 4);
			tEquipos.setValueAt(equipoModificado.getUbicacion(), row, 5);
			tEquipos.setValueAt(estado, row, 8);

			// Actualizar el equipo en el coordinador
			coordinador.modificarEquipo(codigo, equipoModificado);

			// Cerrar el diálogo después de modificar
			dialog.dispose();
		});

		// Botón para cancelar
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(e -> dialog.dispose());

		// Agregar los botones al panel
		panelFormulario.add(btnModificar);
		panelFormulario.add(btnCancelar);

		// Agregar el panel al diálogo
		dialog.add(panelFormulario);
		dialog.setVisible(true); // Muestra el diálogo
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

	public JTextField crearJTextField(JTextField text) {
		text.setBackground(Color.BLACK);
		text.setForeground(NEON_GREEN);
		text.setCaretColor(Color.GREEN);
		text.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
		return text;
	}

	public boolean verificarCampoEstado(JTextField campoEstado) {
		String text = campoEstado.getText().toLowerCase();
		if (!text.equals("true") && !text.equals("false")) {
			JOptionPane.showMessageDialog(null, "El estado es invalido, ingrese 'true' o 'false' como valor válido",
					"Error", JOptionPane.ERROR_MESSAGE);
			campoEstado.setText("");
			return false;
		}
		return true;
	}

	// Método auxiliar para agregar etiqueta y campo al panel
	private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField textField) {
		JLabel label = new JLabel(labelText);
		label.setForeground(NEON_GREEN);
		gbc.gridx = 0; // Columna de la etiqueta
		gbc.gridy++; // Nueva fila
		panel.add(label, gbc);
		gbc.gridx = 1; // Columna del campo
		crearJTextField(textField);
		panel.add(textField, gbc);
	}

	private void guardarEquipoEnArchivo(Equipo equipo) {
		try (FileWriter fw = new FileWriter("equipo.txt", true); // true para modo append
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {

			// Escribe los datos del equipo en el archivo en el formato adecuado
			out.println(equipo.getCodigo() + ";" + equipo.getDescripcion() + ";" + equipo.getMarca() + ";"
					+ equipo.getModelo() + ";" + equipo.getTipoEquipo().getCodigo() + ","
					+ equipo.getTipoEquipo().getDescripcion() + ";" + equipo.getUbicacion().getCodigo() + ","
					+ equipo.getUbicacion().getDescripcion() + ";" + equipo.getEstado() + ";" + equipo.getPuertos()
					+ ";" + equipo.getDireccionesIP());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
