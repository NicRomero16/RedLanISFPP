package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class MiPanel extends JPanel {
    private JTextField[] textFields;
    private JTable table;
    DefaultTableModel tableModel;

    // Colores neón
    Color neonGreen = new Color(57, 255, 20);
    Color neonGray = new Color(30, 30, 30);
    Color neonBlack = Color.BLACK;
    Color neonWhite = Color.WHITE;

    public MiPanel() {
        setLayout(new BorderLayout());
        setBackground(neonGray); // Color de fondo del panel principal
        
        // Panel superior con etiquetas y campos de texto
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(neonGray); // Mismo color de fondo
        JLabel[] labels = new JLabel[6];
        textFields = new JTextField[6];
        
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel("Campo " + (i + 1));
            labels[i].setFont(new Font("Arial", Font.BOLD, 14)); // Fuente más grande y negrita
            labels[i].setForeground(neonWhite); // Color de texto blanco
            textFields[i] = new JTextField(10);
            textFields[i].setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente normal
            textFields[i].setBackground(neonWhite); // Color de fondo blanco para campos de texto
            inputPanel.add(labels[i]);
            inputPanel.add(textFields[i]);
        }

        // Botón para agregar datos a la tabla
        JButton addButton = new JButton("Agregar");
        addButton.setBackground(neonGreen); // Color de fondo del botón
        addButton.setForeground(neonBlack); // Color de texto negro
        addButton.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente en negrita
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarFila();
            }
        });
        
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPanel.setBackground(neonGray); // Mismo color de fondo
        addPanel.add(addButton);
        
        // Tabla para mostrar los datos
        String[] columnNames = {"Código", "Descripción", "Modelo", "Etc", "Etc", "Editar", "Eliminar"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Solo permite editar las columnas de botones
            }
        };

        // Configurar colores de la tabla
        table.setBackground(neonWhite); // Color de fondo blanco para la tabla
        table.setForeground(neonGray); // Color de texto oscuro
        table.setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente normal
        
        // Agregar botones de edición y eliminación
        table.getColumn("Editar").setCellRenderer(new ButtonRenderer("✏️"));
        table.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox(), this, true));
        table.getColumn("Eliminar").setCellRenderer(new ButtonRenderer("❌"));
        table.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox(), this, false));
        
        JScrollPane scrollPane = new JScrollPane(table);

        // Añadir todo al panel principal
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(addPanel, BorderLayout.SOUTH);
    }

    // Método para agregar una nueva fila a la tabla
    private void agregarFila() {
        String[] data = new String[textFields.length];
        for (int i = 0; i < textFields.length; i++) {
            data[i] = textFields[i].getText();
        }
        tableModel.addRow(new Object[]{data[0], data[1], data[2], "Etc", "Etc", "✏️", "❌"});
        
        // Limpiar campos de texto después de agregar la fila
        for (JTextField textField : textFields) {
            textField.setText("");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mi Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MiPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// Renderizador de botones
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer(String text) {
        setText(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(new Color(30, 30, 30)); // Color de texto oscuro
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

// Editor de botones
class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private boolean isEditButton;
    private MiPanel panel;
    private int row;

    public ButtonEditor(JCheckBox checkBox, MiPanel panel, boolean isEditButton) {
        super(checkBox);
        this.isEditButton = isEditButton;
        this.panel = panel;
        button = new JButton();
        button.setForeground(new Color(30, 30, 30)); // Color de texto oscuro
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                if (isEditButton) {
                    // Acciones de edición
                    JOptionPane.showMessageDialog(panel, "Editar fila " + row);
                } else {
                    // Eliminar fila
                    panel.tableModel.removeRow(row);
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        button.setText(isEditButton ? "✏️" : "❌");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}
