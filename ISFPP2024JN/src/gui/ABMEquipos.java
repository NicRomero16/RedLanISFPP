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

		// Crear un panel para el formulario
				JPanel panelFormulario = new JPanel();
				panelFormulario.setLayout(new GridLayout(0, 2)); // 0 filas, 2 columnas
				panelFormulario.setBackground(Color.BLACK);
				panelFormulario.setForeground(NEON_GREEN);

				JLabel labelCodigo = new JLabel("Codigo de equipo");
				JTextField campoCodigo = new JTextField();
				crearJTextField(labelCodigo,panelFormulario,campoCodigo);
				
				JLabel labelDescripcion = new JLabel("Descripcion del equipo");
				JTextField campoDescripcion = new JTextField();
				crearJTextField(labelDescripcion,panelFormulario,campoDescripcion);
				
				JLabel labelMarca = new JLabel("Marca del equipo");
				JTextField campoMarca = new JTextField();
				crearJTextField(labelMarca,panelFormulario,campoMarca);
				
				JLabel labelModelo = new JLabel("Modelo del equipo");
				JTextField campoModelo = new JTextField();
				crearJTextField(labelModelo,panelFormulario,campoModelo);
				
				// Tipos de equipo
				JLabel labelTipoEquipo = new JLabel("Tipo de equipo:");
				String[] listTipoEq = obtenerTiposEquipos();
				JComboBox<String> comboBoxTipoEquipos = new JComboBox<String>(listTipoEq);
				crearComboBox(labelTipoEquipo,panelFormulario, comboBoxTipoEquipos);

				// Ubicación
				JLabel labelUbicacion = new JLabel("Ubicacion:");
				String[] listUbicaciones = obtenerUbicaciones();
				JComboBox<String> comboBoxUbicacion = new JComboBox<String>(listUbicaciones);
				crearComboBox(labelUbicacion,panelFormulario, comboBoxUbicacion);

				// Puertos
				JLabel labelPuertos = new JLabel("Cantidad de puertos:");
				Integer[] cantPuertos = obtenerCantidadPuertos();
				JComboBox<Integer> comboBoxPuertos = new JComboBox<>(cantPuertos);
				crearComboBox(labelPuertos,panelFormulario, comboBoxPuertos);

				// Tipos de puertos
				JLabel labelTipoPuertos = new JLabel("Tipos de puertos:");
				String[] listPuertos = obtenerTipoPuertos();
				JComboBox<String> comboBoxTipoPuerto = new JComboBox<String>(listPuertos);
				crearComboBox(labelTipoPuertos,panelFormulario, comboBoxTipoPuerto);

				// Direcciones IP
				JLabel labelDireccionesIP = new JLabel("Direccion IP");
				JTextField campoDireccionesIP = new JTextField();
				crearJTextField(labelDireccionesIP,panelFormulario,campoDireccionesIP);

				// Estado
				JLabel labelEstado = new JLabel("Estado:");
				String[] estados = { "false", "true" };
				JComboBox<String> comboBoxEstado = new JComboBox<String>(estados);
				crearComboBox(labelEstado,panelFormulario, comboBoxEstado);

		// Botón para confirmar la adición
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(e -> {
			if (equipos.containsKey(campoCodigo.getText())) {
				JOptionPane.showMessageDialog(null, "El equipo ya existe", "Error", JOptionPane.ERROR_MESSAGE);
				campoCodigo.requestFocus();
				return;
			}

			TipoEquipo tEquipo = coordinador.buscarTipoEquipo(comboBoxTipoEquipos.getSelectedItem().toString());
			Ubicacion ubicacion = coordinador.buscarUbicacion(comboBoxUbicacion.getSelectedItem().toString());

			// Crear un arreglo de objetos para la nueva fila con los valores de cada
			// JTextField
			Object[] nuevaFila = { campoCodigo.getText(), campoDescripcion.getText(), campoMarca.getText(),
					campoModelo.getText(), tEquipo.getCodigo() + "," + tEquipo.getDescripcion(),
					ubicacion.getCodigo() + "," + ubicacion.getDescripcion(),
					comboBoxPuertos.getSelectedItem() + ", " + comboBoxTipoPuerto.getSelectedItem(),
					campoDireccionesIP.getText(), comboBoxEstado.getSelectedItem() };

			boolean estado = false;

			if (comboBoxEstado.getSelectedItem() == "false")
				estado = false;
			else
				estado = true;

			int cantidadPuertos = comboBoxPuertos.getSelectedIndex() + 1;

			TipoPuerto tipoPuerto1 = coordinador.buscarTipoPuerto(comboBoxTipoPuerto.getSelectedItem().toString());

			Equipo equipo = new Equipo(campoCodigo.getText(), campoDescripcion.getText(), campoMarca.getText(),
					campoModelo.getText(), tEquipo, ubicacion, estado);

			coordinador.agregarPuertos(equipo, cantidadPuertos, tipoPuerto1);
			coordinador.agregarDireccionesIP(equipo, campoDireccionesIP.getSelectedText());
			coordinador.agregarEquipo(equipo);
			coordinador.cargarDatos();

			// Agregar la nueva fila al modelo de la tabla
			table.addRow(nuevaFila);

			// Cerrar el diálogo después de agregar
			dialog.dispose();
		});

		JButton cancelar = crearBotonCancelar(dialog);


		panelFormulario.add(btnAgregar);
		panelFormulario.add(cancelar);

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
		String descripcionActual = equipo.getDescripcion(); // Corregido de getCodigo() a getDescripcion()
		String marcaActual = equipo.getMarca();
		String modeloActual = equipo.getModelo();
		String tipoEquipoActual = equipo.getTipoEquipo().getCodigo();
		String ubicacionActual = equipo.getUbicacion().getCodigo();
		String puertosActual = tEquipos.getValueAt(row, 6).toString();
		String direccionesIPActual = tEquipos.getValueAt(row, 7).toString();
		String estadoActual = tEquipos.getValueAt(row, 8).toString();

		// Crear un panel para el formulario
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new GridLayout(0, 2)); // 0 filas, 2 columnas
		panelFormulario.setBackground(Color.BLACK);
		panelFormulario.setForeground(NEON_GREEN);

		// Campos individuales sin bucles
		JLabel labelCodigo = new JLabel("Codigo de equipo");
		JTextField campoCodigo = new JTextField(codigoActual);
		crearJTextField(labelCodigo,panelFormulario,campoCodigo);
		
		JLabel labelDescripcion = new JLabel("Descripcion del equipo");
		JTextField campoDescripcion = new JTextField(descripcionActual);
		crearJTextField(labelDescripcion,panelFormulario,campoDescripcion);
		
		JLabel labelMarca = new JLabel("Marca del equipo");
		JTextField campoMarca = new JTextField(marcaActual);
		crearJTextField(labelMarca,panelFormulario,campoMarca);
		
		JLabel labelModelo = new JLabel("Modelo del equipo");
		JTextField campoModelo = new JTextField(modeloActual);
		crearJTextField(labelModelo,panelFormulario,campoModelo);
		
		// Tipos de equipo
		JLabel labelTipoEquipo = new JLabel("Tipo de equipo:");
		String[] listTipoEq = obtenerTiposEquipos();
		JComboBox<String> comboBoxTipoEquipos = new JComboBox<String>(listTipoEq);
		crearComboBox(labelTipoEquipo,panelFormulario, comboBoxTipoEquipos);
		comboBoxTipoEquipos.setSelectedItem(tipoEquipoActual); // Establecer el último dato
		
		// Ubicación
		JLabel labelUbicacion = new JLabel("Ubicacion:");
		String[] listUbicaciones = obtenerUbicaciones();
		JComboBox<String> comboBoxUbicacion = new JComboBox<String>(listUbicaciones);
		crearComboBox(labelUbicacion,panelFormulario, comboBoxUbicacion);
		comboBoxUbicacion.setSelectedItem(ubicacionActual); // Establecer el último dato

		// Puertos
		JLabel labelPuertos = new JLabel("Cantidad de puertos:");
		Integer[] cantPuertos = obtenerCantidadPuertos();
		JComboBox<Integer> comboBoxPuertos = new JComboBox<>(cantPuertos);
		crearComboBox(labelPuertos,panelFormulario, comboBoxPuertos);
		comboBoxPuertos.setSelectedItem(puertosActual); // Establecer el último dato

		// Tipos de puertos
		JLabel labelTipoPuertos = new JLabel("Tipos de puertos:");
		String[] listPuertos = obtenerTipoPuertos();
		JComboBox<String> comboBoxTipoPuerto = new JComboBox<String>(listPuertos);
		crearComboBox(labelTipoPuertos,panelFormulario, comboBoxTipoPuerto);
		
		// Direcciones IP
		JLabel labelDireccionesIP = new JLabel("Direcciones IP");
		JTextField campoDireccionesIP = new JTextField(direccionesIPActual);
		crearJTextField(labelDireccionesIP,panelFormulario,campoDireccionesIP);
		
		// Estado
		JLabel labelEstado = new JLabel("Estado:");
		String[] estados = { "false", "true" };
		JComboBox<String> comboBoxEstado = new JComboBox<String>(estados);
		crearComboBox(labelEstado,panelFormulario, comboBoxEstado);
		comboBoxEstado.setSelectedItem(estadoActual); // Establecer el último dato

		// Botón para confirmar la modificación
		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(e -> {

			TipoEquipo tipoEquipo = coordinador.buscarTipoEquipo(comboBoxTipoEquipos.getSelectedItem().toString());
			Ubicacion ubicacion = coordinador.buscarUbicacion(comboBoxUbicacion.getSelectedItem().toString());

			boolean estado = false;

			if (comboBoxEstado.getSelectedItem() == "false")
				estado = false;
			else
				estado = true;

			// Crear un nuevo objeto Equipo con los valores modificados
			Equipo equipoModificado = new Equipo(campoCodigo.getText(), campoDescripcion.getText(),
					campoMarca.getText(), campoModelo.getText(), tipoEquipo, ubicacion, estado);

			// Actualizar el modelo de la tabla
			tEquipos.setValueAt(equipoModificado.getCodigo(), row, 0);
			tEquipos.setValueAt(equipoModificado.getDescripcion(), row, 1);
			tEquipos.setValueAt(equipoModificado.getMarca(), row, 2);
			tEquipos.setValueAt(equipoModificado.getModelo(), row, 3);
			tEquipos.setValueAt(equipoModificado.getTipoEquipo().getDescripcion(), row, 4); // Corregido
			tEquipos.setValueAt(equipoModificado.getUbicacion().getDescripcion(), row, 5); // Corregido
			tEquipos.setValueAt(estado, row, 8);

			// Actualizar el equipo en el coordinador
			coordinador.modificarEquipo(codigoActual, equipoModificado); // Corregido

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
	
	public void crearJTextField(JLabel label, JPanel panel,JTextField text) {
		label.setForeground(NEON_GREEN);
		text.setBackground(Color.BLACK);
		text.setForeground(NEON_GREEN);
		text.setCaretColor(Color.GREEN);
		text.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
		panel.add(label);
		panel.add(text);
	}

	public void crearComboBox(JLabel label, JPanel panel, JComboBox<?> comboBox) {
		label.setForeground(NEON_GREEN);
		comboBox.setBackground(Color.BLACK);
		comboBox.setForeground(NEON_GREEN);
		comboBox.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 3));
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

	private String[] obtenerTipoPuertos() {
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
		return listPuertos;
	}
}
