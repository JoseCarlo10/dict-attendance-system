import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.RowSorter.SortKey;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceList extends JFrame implements ActionListener {

    private final String databaseName = "Attendance"; // Update with your database name
    private final String collectionName = "query"; // Update with your collection name

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHeight = screenSize.height;

    private JPanel queryPanel;
    private JPanel queryHeader;

    private JButton refreshButton;
    private JButton exportButton;
    private JButton clearButton;

    private JTable table;

    JLabel emptyLabel;

    Font normalFont = new Font("Arial", Font.PLAIN, 18); // Set font size to 20

    AttendanceList() {

        // Set the size of the frame explicitly
        this.setSize((int) (screenWidth - (screenWidth * .05)), (int) (screenHeight - (screenHeight * .1)));

        // Center the frame on the screen
        this.setLocationRelativeTo(null);

        exportButton = new JButton("Export");
        exportButton.setBounds((int) (screenWidth - (screenWidth * .20)), (int) (screenHeight - (screenHeight * .974)), (int) (screenWidth - (screenWidth * .92)), (int) (screenHeight - (screenHeight * .95)));
        exportButton.setFocusable(false);
        exportButton.addActionListener(this);
        exportButton.setFont(normalFont);
        exportButton.setBackground(new Color(0, 153, 0)); // Green color
        exportButton.setForeground(Color.WHITE); // White text color
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.setVisible(true);

        clearButton = new JButton("Clear");
        clearButton.setBounds((int) (screenWidth - (screenWidth * .30)), (int) (screenHeight - (screenHeight * .974)), (int) (screenWidth - (screenWidth * .92)), (int) (screenHeight - (screenHeight * .95)));
        clearButton.setFocusable(false);
        clearButton.addActionListener(this);
        clearButton.setFont(normalFont);
        clearButton.setBackground(new Color(204, 0, 0)); // Red color
        clearButton.setForeground(Color.WHITE); // White text color
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setVisible(true);
        
        refreshButton = new JButton("Refresh");
        refreshButton.setBounds((int) (screenWidth - (screenWidth * .40)), (int) (screenHeight - (screenHeight * .974)), (int) (screenWidth - (screenWidth * .92)), (int) (screenHeight - (screenHeight * .95)));
        refreshButton.setFocusable(false);
        refreshButton.addActionListener(this);
        refreshButton.setFont(normalFont);
        refreshButton.setBackground(new Color(153, 153, 153)); // Green color
        refreshButton.setForeground(Color.WHITE); // White text color
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setVisible(true);

        queryHeader = new JPanel();
        queryHeader.setLayout(null);
        queryHeader.setBounds(0, 0, (int) (screenWidth - (screenWidth * .05)), (int) (screenHeight - (screenHeight * .9)));
        queryHeader.setBackground(new Color(66, 135, 245));

        queryHeader.add(refreshButton);
        queryHeader.add(clearButton);
        queryHeader.add(exportButton);

        queryPanel = new JPanel();
        queryPanel.setBounds(0, 0, (int) (screenWidth - (screenWidth * .05)), (int) (screenHeight - (screenHeight * .1)));
        queryPanel.setLayout(null);
        queryPanel.setBackground(Color.WHITE);

        emptyLabel = new JLabel("This Table has no Data!");
        emptyLabel.setLayout(null);
        emptyLabel.setBounds(((screenWidth / 2) - 130), ((screenHeight / 2) - 100), 300, 100);
        emptyLabel.setFont(normalFont);

        queryPanel.add(emptyLabel);
        queryPanel.add(queryHeader);

        // Create table headers
        String[] headers = {"Date", "Time", "Name", "Phone", "Email", "Sex", "Sector", "Purpose"};

        // Create table model with specified headers
        DefaultTableModel model = new DefaultTableModel(headers, 0);

        // Create JTable with the model
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    component.setBackground(getSelectionBackground());
                } else {
                    Color color = (row % 2 == 0) ? Color.WHITE : new Color(240, 240, 240);
                    component.setBackground(color);
                }
                return component;
            }
        };

        // Set the JTable to be non-editable
        table.setEnabled(false);

        // Set the font size for the table
        table.setFont(new Font("Arial", Font.PLAIN, 16)); // Adjust the font size as needed

        // Set the font for the header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16)); // Adjust the font size and style as needed

        // Disable click and drag of column headers
        table.getTableHeader().setReorderingAllowed(false);

        // Connect to MongoDB
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Find all documents sorted by SubmissionTimestamp in descending order
            List<Document> sortedDocuments = collection.find().sort(new Document("SubmissionTimestamp", 1)).into(new ArrayList<>());


            // Add sorted documents to the table model
            for (Document document : sortedDocuments) {
                addDataToModel(model, document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Create JScrollPane to hold the table
        JScrollPane scrollPane = new JScrollPane(table);

        // Set vertical scrollbar policy
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Set bounds for the scroll pane
        scrollPane.setBounds(-1, queryHeader.getHeight(), (int) (screenWidth - (screenWidth * .057)), (int) (screenHeight - (screenHeight * .24)));

        // Add scroll pane to the queryPanel
        queryPanel.add(scrollPane);

        // Center align for Date column
        TableColumn centerDate = table.getColumnModel().getColumn(0);
        DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer();
        dateRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        centerDate.setCellRenderer(dateRenderer);

        // Center align for Time column
        TableColumn centerTime = table.getColumnModel().getColumn(1);
        DefaultTableCellRenderer timeRenderer = new DefaultTableCellRenderer();
        timeRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        centerTime.setCellRenderer(timeRenderer);

     // Sort based on Date and Time in descending order
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        List<SortKey> sortKeys = new ArrayList<>();
                
        sortKeys.add(new SortKey(0, SortOrder.DESCENDING)); // Sort by Date in descending order
        sortKeys.add(new SortKey(1, SortOrder.DESCENDING)); // Sort by Time in descending order
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        // Adjust the preferred widths of the columns
        TableColumn dateColumn = table.getColumnModel().getColumn(0);
        dateColumn.setPreferredWidth(150); // Adjust the width as needed

        TableColumn timeColumn = table.getColumnModel().getColumn(1);
        timeColumn.setPreferredWidth(100); // Adjust the width as needed

        TableColumn nameColumn = table.getColumnModel().getColumn(2);
        nameColumn.setPreferredWidth(150); // Adjust the width as needed

        TableColumn phoneColumn = table.getColumnModel().getColumn(3);
        phoneColumn.setPreferredWidth(100); // Adjust the width as needed

        TableColumn emailColumn = table.getColumnModel().getColumn(4);
        emailColumn.setPreferredWidth(200); // Adjust the width as needed

        TableColumn sexColumn = table.getColumnModel().getColumn(5);
        sexColumn.setPreferredWidth(50); // Adjust the width as needed

        TableColumn sectorColumn = table.getColumnModel().getColumn(6);
        sectorColumn.setPreferredWidth(150); // Adjust the width as needed

        TableColumn purposeColumn = table.getColumnModel().getColumn(7);
        purposeColumn.setPreferredWidth(300); // Adjust the width as needed

        // Add the panel with scrollbar to the frame
        this.add(queryPanel);
        this.setResizable(false);
        this.setTitle("Query");
        this.setVisible(true);
    }

    private void addDataToModel(DefaultTableModel model, Document document) {
        // Get the time value from the document
        String time = document.getString("Time");
        
        // Convert the time to 24-hour format if it's in AM/PM format
        if (time.contains("AM") || time.contains("PM")) {
            try {
                // Parse the time string to a Date object
                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                Date parsedTime = inputFormat.parse(time);
                
                // Format the parsed time to 24-hour format
                time = outputFormat.format(parsedTime);
            } catch (ParseException e) {
                e.printStackTrace();
                // Handle the parse exception if needed
            }
        }

        // Add the formatted time to the table model
        model.addRow(new Object[]{
                document.getString("Date"),
                time,
                document.getString("Name"),
                document.getString("Phone"),
                document.getString("Email"),
                document.getString("Sex"),
                document.getString("Sector"),
                document.getString("Purpose")
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == refreshButton) {
    		this.dispose();
    		try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
				MongoDatabase database = mongoClient.getDatabase("Attendance");
				MongoCollection<Document> collection = database.getCollection("query");
				AttendanceList al = new AttendanceList();
				long count = collection.countDocuments();
				if (count == 0) {
					al.emptyLabel = new JLabel("This Table has no Data!");
					al.emptyLabel.setLayout(null);
					al.emptyLabel.setBounds(((screenWidth/2)-100), ((screenHeight/2)-100), 300, 100);
					al.setVisible(true);
				} else {
					al.emptyLabel.setVisible(false);
				}
			}

        }
        if (e.getSource() == clearButton) {
            try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
                MongoDatabase database = mongoClient.getDatabase("Attendance");
                MongoCollection<Document> collection = database.getCollection("query");
                long count = collection.countDocuments();
                if (count == 0) {
                    JOptionPane.showMessageDialog(null, "TABLE IS EMPTY!", "Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int choice = JOptionPane.showConfirmDialog(null, "Clear Data Table?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        clearAttendanceData();
                    } else {
                        // User clicked No or closed the dialog, do nothing or handle accordingly
                    }
                }
            }

        }
        if (e.getSource() == exportButton) {

            try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
                MongoDatabase database = mongoClient.getDatabase("Attendance");
                MongoCollection<Document> collection = database.getCollection("query");
                long count = collection.countDocuments();
                if (count == 0) {
                    JOptionPane.showMessageDialog(null, "TABLE IS EMPTY!", "Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int choice = JOptionPane.showConfirmDialog(null, "Export Data Table?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        exportToCSV(table);
                    } else {
                        // User clicked No or closed the dialog, do nothing or handle accordingly
                    }
                }
            }
        }
    }

    private void exportToCSV(JTable table) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("Attendance");
            MongoCollection<Document> collection = database.getCollection("query");
            long count = collection.countDocuments();
            if (count == 0) {
                JOptionPane.showMessageDialog(null, "DATA TABLE IS EMPTY!", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                try {
                    // Get user's home directory
                    String homeDir = System.getProperty("user.home");

                    // Create file in the downloads directory
                    File file = new File(homeDir + "/Downloads/Attendance Table.csv");

                    // Create FileWriter
                    FileWriter writer = new FileWriter(file);

                    // Write CSV header
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        writer.write(table.getColumnName(i));
                        if (i < table.getColumnCount() - 1) {
                            writer.write(",");
                        }
                    }
                    writer.write("\n");

                    // Write CSV data
                    for (int row = 0; row < table.getRowCount(); row++) {
                        for (int col = 0; col < table.getColumnCount(); col++) {
                            writer.write(table.getValueAt(row, col).toString());
                            if (col < table.getColumnCount() - 1) {
                                writer.write(",");
                            }
                        }
                        writer.write("\n");
                    }

                    // Close the FileWriter
                    writer.close();

                    JOptionPane.showMessageDialog(this, "Data exported successfully to " + file.getAbsolutePath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error exporting data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }


    private void clearAttendanceData() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase database = mongoClient.getDatabase("Attendance");
            MongoCollection<Document> collection = database.getCollection("query");

            // Delete all documents from the collection
            collection.deleteMany(new Document());

            // Clear the table model after deleting documents
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing rows

            // After clearing, if you need to refresh data from the collection, you can re-add the data to the table model
            // For example:
            for (Document document : collection.find()) {
                addDataToModel(model, document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the MongoDB client
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }
}
