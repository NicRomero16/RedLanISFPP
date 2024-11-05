package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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

	public ABMEquipos(TreeMap<String, Equipo> equipos, Coordinador coordinador) {
		super();
		this.coordinador = coordinador;
		this.equipos = equipos;
		crearPanelEquipo();
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
			TipoPuerto tPuerto = coordinador.buscarTipoPuerto(equipo.obtenerCodigoTipoPuerto(0));
			Object[] fila = { equipo.getCodigo(), equipo.getDescripcion(), equipo.getMarca(), equipo.getModelo(),
					(equipo.getTipoEquipo().getCodigo() + ", " + equipo.getTipoEquipo().getDescripcion()),
					(equipo.getUbicacion().getCodigo() + "," + equipo.getUbicacion().getDescripcion()),
					((equipo.getCantidadPuertos() + "," + tPuerto.getCodigo() + "," + tPuerto.getDescripcion() + ","
							+ tPuerto.getVelocidad())),
					String.join(", ", equipo.getDireccionesIP()), equipo.getEstado(), "Eliminar", "Modificar" };
			tEquipos.addRow(fila);
		}

		// Inicializar la tabla y asignar el modelo
		JTable tablaEquipos = new JTable(tEquipos) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Hacer que ninguna celda sea editable
			}
		};

		tablaEquipos.setCellSelectionEnabled(true);
		tablaEquipos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tablaEquipos.rowAtPoint(e.getPoint());
				int column = tablaEquipos.columnAtPoint(e.getPoint());

				for (int i = 0; i < tEquipos.getRowCount(); i++) {
					if (i < tEquipos.getRowCount()) {
						tEquipos.setValueAt("Eliminar", i, 9);
					}
				}

				for (int i = 0; i < tEquipos.getRowCount(); i++) {
					if (i < tEquipos.getRowCount()) {
						tEquipos.setValueAt("Modificar", i, 10);
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
						tEquipos.removeRow(row);
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
		header.setBackground(Color.BLACK);
		header.setForeground(NEON_GREEN);
		header.setFont(header.getFont().deriveFont(Font.BOLD, 14f)); // Fuente en negrita para la cabecera

		// Configurar el JScrollPane para contener la tabla
		JScrollPane pane = new JScrollPane(tablaEquipos);
		pane.setBackground(Color.BLACK);
		pane.getViewport().setBackground(Color.BLACK);

		panelEquipo.add(pane, BorderLayout.CENTER);

		JButton btnAgregarEquipo = new JButton("Agregar equipo");
		btnAgregarEquipo.addActionListener(e -> formularioAgregarEquipo(tablaEquipos, tEquipos));

		this.setLayout(new BorderLayout());
		this.add(panelEquipo, BorderLayout.CENTER);
		this.add(btnAgregarEquipo, BorderLayout.SOUTH);
	}

	public void formularioAgregarEquipo(JTable tablaEquipos, DefaultTableModel table) {

		// Crear un nuevo JDialog para el formulario
		JDialog dialog = new JDialog((Frame) null, "Agregar equipo", true);
		dialog.setTitle("Agregar Nuevo Equipo");
		dialog.setSize(500, 500);
		dialog.setLocationRelativeTo(null); // Centra el diálogo

		// Crear un panel para el formulario
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new GridLayout(0, 2));
		panelFormulario.setBackground(Color.BLACK);
		panelFormulario.setForeground(NEON_GREEN);

		JLabel labelCodigo = new JLabel("Codigo de equipo");
		JTextField campoCodigo = new JTextField();
		crearJTextField(labelCodigo, panelFormulario, campoCodigo);

		JLabel labelDescripcion = new JLabel("Descripcion del equipo");
		JTextField campoDescripcion = new JTextField();
		crearJTextField(labelDescripcion, panelFormulario, campoDescripcion);

		JLabel labelMarca = new JLabel("Marca del equipo");
		JTextField campoMarca = new JTextField();
		crearJTextField(labelMarca, panelFormulario, campoMarca);

		JLabel labelModelo = new JLabel("Modelo del equipo");
		JTextField campoModelo = new JTextField();
		crearJTextField(labelModelo, panelFormulario, campoModelo);

		// Tipos de equipo
		JLabel labelTipoEquipo = new JLabel("Tipo de equipo:");
		String[] listTipoEq = obtenerTiposEquipos();
		JComboBox<String> comboBoxTipoEquipos = new JComboBox<String>(listTipoEq);
		crearComboBox(labelTipoEquipo, panelFormulario, comboBoxTipoEquipos);

		// Ubicación
		JLabel labelUbicacion = new JLabel("Ubicacion:");
		String[] listUbicaciones = obtenerUbicaciones();
		JComboBox<String> comboBoxUbicacion = new JComboBox<String>(listUbicaciones);
		crearComboBox(labelUbicacion, panelFormulario, comboBoxUbicacion);

		// Puertos
		JLabel labelPuertos = new JLabel("Cantidad de puertos:");
		Integer[] cantPuertos = obtenerCantidadPuertos();
		JComboBox<Integer> comboBoxPuertos = new JComboBox<>(cantPuertos);
		crearComboBox(labelPuertos, panelFormulario, comboBoxPuertos);

		// Tipos de puertos
		JLabel labelTipoPuertos = new JLabel("Tipos de puertos:");
		String[] listPuertos = coordinador.devolverTipoPuertoCodigo();
		JComboBox<String> comboBoxTipoPuerto = new JComboBox<String>(listPuertos);
		crearComboBox(labelTipoPuertos, panelFormulario, comboBoxTipoPuerto);

		List<String> direccionesIP = new ArrayList<String>();

		abmDireccionesIP(dialog, panelFormulario, direccionesIP);

		// Estado
		JLabel labelEstado = new JLabel("Estado:");
		String[] estados = { "false", "true" };
		JComboBox<String> comboBoxEstado = new JComboBox<String>(estados);
		crearComboBox(labelEstado, panelFormulario, comboBoxEstado);

		// Botón para confirmar la adición
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(e -> {
			if (equipos.containsKey(campoCodigo.getText())) {
				JOptionPane.showMessageDialog(null, "El equipo ya existe", "Error", JOptionPane.ERROR_MESSAGE);
				campoCodigo.requestFocus();
				return;
			}

			if (campoCodigo.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "El codigo de equipo no puede ser nulo", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			TipoEquipo tEquipo = coordinador.buscarTipoEquipo(comboBoxTipoEquipos.getSelectedItem().toString());
			Ubicacion ubicacion = coordinador.buscarUbicacion(comboBoxUbicacion.getSelectedItem().toString());
			int cantidadPuertos = comboBoxPuertos.getSelectedIndex() + 1;
			TipoPuerto tipoPuerto = coordinador.buscarTipoPuerto(comboBoxTipoPuerto.getSelectedItem().toString());

			Object[] nuevaFila = { campoCodigo.getText(), campoDescripcion.getText(), campoMarca.getText(),
					campoModelo.getText(), tEquipo.getCodigo() + "," + tEquipo.getDescripcion(),
					ubicacion.getCodigo() + "," + ubicacion.getDescripcion(),
					cantidadPuertos + ", " + tipoPuerto.getCodigo() + ", " + tipoPuerto.getDescripcion() + ", "
							+ tipoPuerto.getVelocidad(),
					String.join(", ", direccionesIP), comboBoxEstado.getSelectedItem() };

			boolean estado = false;

			if (comboBoxEstado.getSelectedItem() == "false")
				estado = false;
			else
				estado = true;

			Equipo equipo = new Equipo(campoCodigo.getText(), campoDescripcion.getText(), campoMarca.getText(),
					campoModelo.getText(), tEquipo, ubicacion, estado);
			for (String direccion : direccionesIP)
				equipo.agregarDireccionIP(direccion);
			equipo.agregarPuerto(cantidadPuertos, tipoPuerto);
			coordinador.agregarEquipo(equipo);

			table.addRow(nuevaFila);
			// Cerrar el dialogo despues de agregar
			dialog.dispose();
		});

		JButton cancelar = crearBotonCancelar(dialog);
		panelFormulario.add(btnAgregar);
		panelFormulario.add(cancelar);

		dialog.add(panelFormulario);
		dialog.setVisible(true);
		dialog.setResizable(false); // No permite cambiar el tamañoo de la ventana
	}

	private void modificarEquipo(DefaultTableModel tEquipos, int row, Equipo equipo) {
		JDialog dialog = new JDialog((Frame) null, "Modificar equipo", true);
		dialog.setTitle("Modificar Equipo");
		dialog.setSize(500, 500);
		dialog.setLocationRelativeTo(null); // Centra el diálogo

		// Obtener los datos actuales de la fila
		List<String> direccionesIP = equipo.getDireccionesIP().stream().filter(ip -> !ip.trim().isEmpty())
				.collect(Collectors.toList());

		String estadoActual = tEquipos.getValueAt(row, 8).toString();

		// Crear un panel para el formulario
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new GridLayout(0, 2)); // 0 filas, 2 columnas
		panelFormulario.setBackground(Color.BLACK);
		panelFormulario.setForeground(NEON_GREEN);

		// Campos individuales sin bucles
		JLabel labelCodigo = new JLabel("Codigo de equipo");
		JTextField campoCodigo = new JTextField(equipo.getCodigo());
		crearJTextField(labelCodigo, panelFormulario, campoCodigo);

		JLabel labelDescripcion = new JLabel("Descripcion del equipo");
		JTextField campoDescripcion = new JTextField(equipo.getDescripcion());
		crearJTextField(labelDescripcion, panelFormulario, campoDescripcion);

		JLabel labelMarca = new JLabel("Marca del equipo");
		JTextField campoMarca = new JTextField(equipo.getMarca());
		crearJTextField(labelMarca, panelFormulario, campoMarca);

		JLabel labelModelo = new JLabel("Modelo del equipo");
		JTextField campoModelo = new JTextField(equipo.getModelo());
		crearJTextField(labelModelo, panelFormulario, campoModelo);

		// Tipos de equipo
		JLabel labelTipoEquipo = new JLabel("Tipo de equipo:");
		String[] listTipoEq = obtenerTiposEquipos();
		JComboBox<String> comboBoxTipoEquipos = new JComboBox<String>(listTipoEq);
		crearComboBox(labelTipoEquipo, panelFormulario, comboBoxTipoEquipos);
		comboBoxTipoEquipos.setSelectedItem(equipo.getTipoEquipo().getCodigo()); // Establecer el último dato

		// Ubicación
		JLabel labelUbicacion = new JLabel("Ubicacion:");
		String[] listUbicaciones = obtenerUbicaciones();
		JComboBox<String> comboBoxUbicacion = new JComboBox<String>(listUbicaciones);
		crearComboBox(labelUbicacion, panelFormulario, comboBoxUbicacion);
		comboBoxUbicacion.setSelectedItem(equipo.getUbicacion().getCodigo()); // Establecer el último dato

		// Puertos
		JLabel labelPuertos = new JLabel("Cantidad de puertos:");
		Integer[] cantPuertos = obtenerCantidadPuertos();
		JComboBox<Integer> comboBoxPuertos = new JComboBox<>(cantPuertos);
		crearComboBox(labelPuertos, panelFormulario, comboBoxPuertos);
		comboBoxPuertos.setSelectedItem(equipo.getCantidadPuertos()); // Establecer el último dato

		// Tipos de puertos
		JLabel labelTipoPuertos = new JLabel("Tipos de puertos:");
		String[] listPuertos = coordinador.devolverTipoPuertoCodigo();
		JComboBox<String> comboBoxTipoPuerto = new JComboBox<String>(listPuertos);
		crearComboBox(labelTipoPuertos, panelFormulario, comboBoxTipoPuerto);
		comboBoxTipoPuerto.setSelectedItem(equipo.obtenerCodigoTipoPuerto(0));

		abmDireccionesIP(dialog, panelFormulario, direccionesIP);

		// Estado
		JLabel labelEstado = new JLabel("Estado:");
		String[] estados = { "false", "true" };
		JComboBox<String> comboBoxEstado = new JComboBox<String>(estados);
		crearComboBox(labelEstado, panelFormulario, comboBoxEstado);
		comboBoxEstado.setSelectedItem(estadoActual);

		// Botón para confirmar la modificación
		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(e -> {

			if (campoCodigo.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "El codigo de equipo no puede ser nulo", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			TipoEquipo tipoEquipo = coordinador.buscarTipoEquipo(comboBoxTipoEquipos.getSelectedItem().toString());
			Ubicacion ubicacion = coordinador.buscarUbicacion(comboBoxUbicacion.getSelectedItem().toString());
			int cantidadPuertos = comboBoxPuertos.getSelectedIndex() + 1;
			TipoPuerto tipoPuerto = coordinador.buscarTipoPuerto(comboBoxTipoPuerto.getSelectedItem().toString());

			boolean estado = false;

			if (comboBoxEstado.getSelectedItem() == "false")
				estado = false;
			else
				estado = true;

			// Crear un nuevo objeto Equipo con los valores modificados
			Equipo equipoModificado = new Equipo(campoCodigo.getText(), campoDescripcion.getText(),
					campoMarca.getText(), campoModelo.getText(), tipoEquipo, ubicacion, estado);

			String direccionesIPTexto = String.join(", ", direccionesIP);

			// Actualizar el modelo de la tabla
			tEquipos.setValueAt(equipoModificado.getCodigo(), row, 0);
			tEquipos.setValueAt(equipoModificado.getDescripcion(), row, 1);
			tEquipos.setValueAt(equipoModificado.getMarca(), row, 2);
			tEquipos.setValueAt(equipoModificado.getModelo(), row, 3);
			tEquipos.setValueAt(equipoModificado.getTipoEquipo().getCodigo() + ","
					+ equipoModificado.getTipoEquipo().getDescripcion(), row, 4);
			tEquipos.setValueAt(equipoModificado.getUbicacion().getCodigo() + ","
					+ equipoModificado.getUbicacion().getDescripcion(), row, 5);
			tEquipos.setValueAt(comboBoxPuertos.getSelectedItem().toString() + "," + tipoPuerto.getCodigo() + ","
					+ tipoPuerto.getDescripcion() + "," + tipoPuerto.getVelocidad(), row, 6);
			tEquipos.setValueAt(direccionesIPTexto, row, 7);

			tEquipos.setValueAt(comboBoxEstado.getSelectedItem().toString(), row, 8);

			for (String direccion : direccionesIP)
				equipoModificado.agregarDireccionIP(direccion);
			equipoModificado.agregarPuerto(cantidadPuertos, tipoPuerto);
			coordinador.modificarEquipo(equipo.getCodigo(), equipoModificado); // Corregido
			coordinador.cargarDatos();

			// Cerrar el diálogo después de modificar
			dialog.dispose();
		});

		// Botón para cancelar
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(e -> dialog.dispose());

		// Agregar los botones al panel, ocupando una sola fila
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

	public void crearJTextField(JLabel label, JPanel panel, JTextField text) {
		label.setForeground(NEON_GREEN);
		text.setBackground(Color.BLACK);
		text.setForeground(NEON_GREEN);
		text.setCaretColor(Color.GREEN);
		text.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
		panel.add(label);
		panel.add(text);
	}

	public void agregar(JLabel label, JPanel panel, JButton btn) {
		label.setForeground(NEON_GREEN);
		btn.setBackground(Color.BLACK);
		btn.setForeground(NEON_GREEN);
		btn.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 2));
		panel.add(label);
		panel.add(btn);
	}

	public void crearComboBox(JLabel label, JPanel panel, JComboBox<?> comboBox) {
		label.setForeground(NEON_GREEN);
		comboBox.setBackground(Color.BLACK);
		comboBox.setForeground(NEON_GREEN);
		comboBox.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 2));
		panel.add(label);
		panel.add(comboBox);
	}

	private String[] obtenerTiposEquipos() {
		TreeMap<String, TipoEquipo> tipoEquipos = coordinador.listarTipoEquipos();
		List<String> listTipoEquipos = new ArrayList<String>();

		for (TipoEquipo tipos : tipoEquipos.values()) {
			listTipoEquipos.add(tipos.getCodigo());
		}
		String[] listTipoEq = new String[listTipoEquipos.size()];
		int i1 = 0;
		for (String tipos : listTipoEquipos) {
			listTipoEq[i1] = tipos;
			i1++;
		}
		return listTipoEq;
	}

	private String[] obtenerUbicaciones() {
		TreeMap<String, Ubicacion> ubicaciones = coordinador.listarUbicaciones();
		List<String> ubi = new ArrayList<String>();

		for (Ubicacion ubicacion : ubicaciones.values()) {
			ubi.add(ubicacion.getCodigo());
		}

		String[] listUbicaciones = new String[ubi.size()];
		int i2 = 0;
		for (String ubicacion : ubi) {
			listUbicaciones[i2] = ubicacion;
			i2++;
		}
		return listUbicaciones;
	}

	private Integer[] obtenerCantidadPuertos() {
		Integer[] cantPuertos = new Integer[50];
		for (int i3 = 0; i3 < 50; i3++)
			cantPuertos[i3] = i3 + 1;
		return cantPuertos;
	}

	private String agregarDireccionIP(List<String> ips) {
		JDialog dialog = new JDialog((Frame) null, "Agregar direccionIP", true);
		dialog.setTitle("Modificar Equipo");
		dialog.setSize(300, 100);
		dialog.setLocationRelativeTo(null);

		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new GridLayout(0, 2));
		panelFormulario.setBackground(Color.BLACK);
		panelFormulario.setForeground(NEON_GREEN);

		JLabel labelMarca = new JLabel("Ingrese la direccion IP");
		JTextField campoDireccionIP = new JTextField();
		crearJTextField(labelMarca, panelFormulario, campoDireccionIP);

		TreeMap<String, Equipo> equipos = coordinador.listarEquipos();

		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(e -> {
			if (campoDireccionIP.getText() == null || campoDireccionIP.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "No ingreso ninguna direccionIP", "Advertencia",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			for (Equipo eq : equipos.values()) {
				List<String> direccionesIP = eq.getDireccionesIP();
				if (direccionesIP.contains(campoDireccionIP.getText()) || (ips.contains(campoDireccionIP.getText()))) {
					JOptionPane.showMessageDialog(null, "La dirección IP ya existe", "Advertencia",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			dialog.dispose();
		});

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(e -> {
			campoDireccionIP.setText("");
			dialog.dispose();
		});

		panelFormulario.add(btnAceptar);
		panelFormulario.add(btnCancelar);

		dialog.add(panelFormulario);
		dialog.setResizable(false);
		dialog.setVisible(true);
		return campoDireccionIP.getText();
	}

	public boolean abmDireccionesIP(JDialog dialog, JPanel panel, List<String> direccionesIP) {
		JLabel labelVerDireccionesIP = new JLabel("Direcciones IP:");
		JButton btnVerDireccionesIP = new JButton("Ver, Agregar o Eliminar");
		agregar(labelVerDireccionesIP, panel, btnVerDireccionesIP);
		btnVerDireccionesIP.addActionListener(e -> {
			String[] columnNames = { "Dirección IP" };
			String[][] data = new String[direccionesIP.size()][1];

			for (int i = 0; i < direccionesIP.size(); i++) {
				data[i][0] = direccionesIP.get(i);
			}

			DefaultTableModel model = new DefaultTableModel(data, columnNames);
			JTable tablaDireccionesIP = new JTable(model) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			tablaDireccionesIP.setPreferredScrollableViewportSize(new Dimension(100, 100));
			tablaDireccionesIP.setFillsViewportHeight(true);
			tablaDireccionesIP.setBackground(Color.BLACK);
			tablaDireccionesIP.setForeground(NEON_GREEN);
			tablaDireccionesIP.setGridColor(NEON_GREEN);

			JTableHeader header = tablaDireccionesIP.getTableHeader();
			header.setBackground(Color.BLACK);
			header.setForeground(NEON_GREEN);
			header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));

			JScrollPane scrollPane = new JScrollPane(tablaDireccionesIP);

			JPanel panelTabla = new JPanel();
			panelTabla.setLayout(new BorderLayout());
			panelTabla.add(scrollPane, BorderLayout.CENTER);

			JPanel panelBotones = new JPanel();
			panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
			JButton btnEliminar = new JButton("Eliminar");
			btnEliminar.addActionListener(ev -> {
				int[] selectedRows = tablaDireccionesIP.getSelectedRows(); // Obtener todas las filas seleccionadas
				if (selectedRows.length > 0) {
					for (int i = selectedRows.length - 1; i >= 0; i--) {
						model.removeRow(selectedRows[i]);
						direccionesIP.remove(selectedRows[i]);
					}
				} else
					JOptionPane.showMessageDialog(null, "Por favor, selecciona una dirección IP para eliminar.",
							"Error", JOptionPane.ERROR_MESSAGE);
			});

			JButton btnAgregarDireccionIP = new JButton("Agregar");
			btnAgregarDireccionIP.addActionListener(ev -> {
				String direccionIP = agregarDireccionIP(direccionesIP);
				if (!direccionIP.trim().equals("")) {
					direccionesIP.add(direccionIP);
					model.addRow(new String[] { direccionIP });
				}
			});

			JButton btnRegresar = new JButton("Regresar");

			panelBotones.add(btnAgregarDireccionIP);
			panelBotones.add(btnEliminar);
			panelBotones.add(btnRegresar);

			panelTabla.add(panelBotones, BorderLayout.SOUTH);

			JDialog dialogTabla = new JDialog(dialog, "Direcciones IP", false);
			dialogTabla.add(panelTabla);
			dialogTabla.pack();
			dialogTabla.setVisible(true);
			dialogTabla.setLocationRelativeTo(null);

			btnRegresar.addActionListener(ev -> {
				dialogTabla.dispose();
			});
		});
		return false;
	}
}
