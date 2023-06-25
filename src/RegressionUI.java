import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.function.Consumer;

public class RegressionUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField xField;
    private JTextField yField;
    public JComboBox<String> regressionTypeComboBox;
    public JTextField degreeField;
    private Consumer<HashMap<Double, Double>> onDone;
    public RegressionUI( Consumer<HashMap<Double, Double>> onDone) {
        this.onDone = onDone;
        setTitle("Function Regression");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(850, 400);
        setLocationRelativeTo(null);

        // Create the table
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("X");
        tableModel.addColumn("Y");

        // Create the scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);

        // Create labels and text fields
        JLabel xLabel = new JLabel("X:");
        xField = new JTextField(10);
        JLabel yLabel = new JLabel("Y:");
        yField = new JTextField(10);

        // Create regression type dropdown
        JLabel regressionTypeLabel = new JLabel("Regression Type:");
        regressionTypeComboBox = new JComboBox<>();
        regressionTypeComboBox.addItem("Linear");
        regressionTypeComboBox.addItem("Exponential");
        regressionTypeComboBox.addItem("Logarithmic");
        regressionTypeComboBox.addItem("Polynomial");
        // Create degree label and text field for polynomial regression
        JLabel degreeLabel = new JLabel("Degree:");
        degreeField = new JTextField(5);
        degreeLabel.setEnabled(false);
        degreeField.setEnabled(false);

        // Add action listener to enable/disable degree fields based on selected regression type
        regressionTypeComboBox.addActionListener(e -> {
            String selectedType = (String) regressionTypeComboBox.getSelectedItem();
            assert selectedType != null;
            if (selectedType.equals("Polynomial")) {
                degreeLabel.setEnabled(true);
                degreeField.setEnabled(true);
            } else {
                degreeLabel.setEnabled(false);
                degreeField.setEnabled(false);
            }
        });

        // Create buttons
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addData());

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            HashMap<Double, Double> dataMap = new HashMap<>();
            for(int i = 0; i < tableModel.getRowCount(); i++)
            {
                dataMap.put((Double.parseDouble((String) tableModel.getValueAt(i, 0))), Double.parseDouble((String) tableModel.getValueAt(i, 1)));
            }
            onDone.accept(dataMap);
            for(int i = tableModel.getRowCount() - 1; i >= 0; i--)
            {
                tableModel.removeRow(i);
            }
            this.setVisible(false);
        });

        // Create a panel to hold the labels, text fields, and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(xLabel);
        inputPanel.add(xField);
        inputPanel.add(yLabel);
        inputPanel.add(yField);
        inputPanel.add(regressionTypeLabel);
        inputPanel.add(regressionTypeComboBox);
        inputPanel.add(degreeLabel);
        inputPanel.add(degreeField);
        inputPanel.add(addButton);
        inputPanel.add(doneButton);

        // Create a panel to hold the scroll pane
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add the panels to the frame
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(tablePanel, BorderLayout.CENTER);
    }

    private void addData() {
        String xValue = xField.getText();
        String yValue = yField.getText();

        if (!xValue.isEmpty() && !yValue.isEmpty()) {
            Object[] row = {xValue, yValue};
            tableModel.addRow(row);
            xField.setText("");
            yField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter both X and Y values.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            String xValue = xField.getText();
            String yValue = yField.getText();

            if (!xValue.isEmpty() && !yValue.isEmpty()) {
                table.setValueAt(xValue, selectedRow, 0);
                table.setValueAt(yValue, selectedRow, 1);
                xField.setText("");
                yField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both X and Y values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}