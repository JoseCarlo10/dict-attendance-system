import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AttendancePanel extends JPanel implements ActionListener, KeyListener {

    JLabel nameLabel;
    JLabel phoneLabel;
    JLabel emailLabel;
    JLabel sexLabel;
    JLabel sectorLabel;
    JLabel purposeLabel;
    JLabel specifyLabel;

    private static JTextField nameField;
    private static JTextField phoneField;
    private static JTextField emailField;
    private static JTextField sectorField;
    private static JTextField specifyField;

    private static JCheckBox consentCheckbox;
    private static JButton submitButton;

    JScrollPane scrollPane;
    DataManagement mngmnt;

    private static String name;
    private static String phone;
    private static String email;
    private static String sex;
    private static String sector;
    private static String purpose;
    private static boolean consent;
    private static String date;
    private static String time;
    Object selectedItem;
    
 // Defining PC platform's height and width
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    int screenWidth = screenSize.width;
    int screen_width = screenWidth;
    int screenHeight = screenSize.height;
    int screen_height = screenHeight;

    private static JComboBox<String> sexComboBox; // Move declaration here
    private static JComboBox<String> purposeComboBox;

    AttendancePanel() {

        byte xTextLocation = 30;    // Sets all labels x-axis location
        byte yinputBox = 40;    // Sets all textfields and labels y-axis location
        int xtextBox = 230;        // Sets all fields x-axis location
        Font labelFont = new Font("Arial", Font.BOLD, 18); // Set font size to 20
        Font normalFont = new Font("Arial", Font.PLAIN, 18); // Set font size to 20
        this.setLayout(null); // Use null layout for manual component placement

        this.setBackground(new Color(230, 230, 230));    // Sets Attendance form's properties
        this.setBounds(((screenWidth/2)-(750/2)), ((screenHeight/2)-(650/3)), 750, 650);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Create labels
        nameLabel = new JLabel("NAME:");
        nameLabel.setBounds(xTextLocation, 50, 100, yinputBox);
        nameLabel.setFont(labelFont);

        phoneLabel = new JLabel("PHONE (OPTIONAL) :");
        phoneLabel.setBounds(xTextLocation, 100, 190, yinputBox);
        phoneLabel.setFont(labelFont);

        emailLabel = new JLabel("EMAIL (OPTIONAL) :");
        emailLabel.setBounds(xTextLocation, 150, 190, yinputBox);
        emailLabel.setFont(labelFont);

        sexLabel = new JLabel("SEX:");
        sexLabel.setBounds(xTextLocation, 200, 100, yinputBox);
        sexLabel.setFont(labelFont);

        sectorLabel = new JLabel("SECTOR:");
        sectorLabel.setBounds(xTextLocation, 250, 100, yinputBox);
        sectorLabel.setFont(labelFont);

        purposeLabel = new JLabel("PURPOSE OF VISIT:");
        purposeLabel.setBounds(xTextLocation, 300, 180, yinputBox);
        purposeLabel.setFont(labelFont);

        specifyLabel = new JLabel("PLEASE SPECIFY:");
        specifyLabel.setBounds(xTextLocation, 350, 180, yinputBox);
        specifyLabel.setFont(labelFont);
        specifyLabel.setVisible(false);

        // Create text fields
        nameField = new JTextField();
        nameField.setBounds(xtextBox, 50, 490, yinputBox);
        nameField.setFont(normalFont);

        nameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isLetter(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE || c == '.')) {
                    e.consume(); // Ignore the event
                }
            }


            @Override
            public void keyPressed(KeyEvent e) {
                // Not needed, but required by the KeyListener interface
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not needed, but required by the KeyListener interface
            }
        });


        // Name field is limited to only 100 characters
        AbstractDocument nameDoc = (AbstractDocument) nameField.getDocument();
        nameDoc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= 100) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (fb.getDocument().getLength() - length + text.length() <= 100) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        phoneField = new JTextField();
        phoneField.setBounds(xtextBox, 100, 200, yinputBox);
        phoneField.setFont(normalFont);

        // Phone field is limited to 15 characters and only accepts numbers
        AbstractDocument phoneDoc = (AbstractDocument) phoneField.getDocument();
        phoneDoc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isNumeric(string) && fb.getDocument().getLength() + string.length() <= 15) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                if (isNumeric(newText) && newText.length() <= 15) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isNumeric(String str) {
                return str.matches("\\d*"); // Only allow digits
            }
        });


        emailField = new JTextField();
        emailField.setBounds(xtextBox, 150, 490, yinputBox);
        emailField.setFont(normalFont);

        // Name field is limited to only 100 characters
        AbstractDocument emailDoc = (AbstractDocument) emailField.getDocument();
        emailDoc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= 100) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (fb.getDocument().getLength() - length + text.length() <= 100) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Create combo box for sex
        String[] sexes = {"--Please Select--", "Male", "Female"};
        sexComboBox = new JComboBox<>(sexes);
        sexComboBox.setBounds(xtextBox, 200, 180, yinputBox);
        sexComboBox.setFont(normalFont);

        // Add action listener to handle Enter key for dropping down combo box
        sexComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxEdited")) {
                    Object selectedItem = sexComboBox.getSelectedItem();
                    if (selectedItem != null) {
                        sexComboBox.setSelectedItem(selectedItem);
                    }
                    sexComboBox.setPopupVisible(false);
                }
            }
        });


        // Create text area for purpose of visit
        sectorField = new JTextField(); // Fix bounds here
        sectorField.setBounds(xtextBox, 250, 490, yinputBox);
        sectorField.setFont(normalFont);

        // Sector field is limited to only 100 characters
        AbstractDocument sectorDoc = (AbstractDocument) sectorField.getDocument();
        sectorDoc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= 100) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (fb.getDocument().getLength() - length + text.length() <= 100) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

     // Create combo box for sector
        String[] sectors = {
                "--Please Select--",
                "Internet Connectivity",
                "Capability Building for Center Managers",
                "Online Transactions on Government Sites",
                "School-related Support",
                "Venue for Meetings / Webinars / Online Class",
                "Lending of ICT Equipment",
                "General Client-related",
                "Others"
        };
                
        purposeComboBox = new JComboBox<>(sectors);
        // Increase the height of the combo box dropdown
        purposeComboBox.setMaximumRowCount(10); // Set as needed to fit all options without scrolling
        purposeComboBox.setBounds(xtextBox, 300, 490, yinputBox);
        purposeComboBox.setFont(normalFont);


        specifyField = new JTextField();
        specifyField.setBounds(xtextBox, 350, 490, yinputBox);
        specifyField.setFont(normalFont);
        specifyField.setVisible(false);

        consentCheckbox = new JCheckBox();
        consentCheckbox.setBounds(50, 370, 650, 150);
        consentCheckbox.setText("<html><body style='width: 480px;'><div style='display: inline-block; text-align: justify; vertical-align: top;'>I am giving consent to DICT Region V Catanduanes to collect and process my information in order for me to join any of the webinars under this cluster. My information will not be shared to any DICT Region V affiliated partners or service providers, and will only be used for data reports and for communication before the start and end of the webinar.</div></body></html>");
        consentCheckbox.setHorizontalTextPosition(SwingConstants.RIGHT);
        consentCheckbox.setPreferredSize(new Dimension(600, 200));
        consentCheckbox.setFocusable(false);
        consentCheckbox.setFont(normalFont);
        consentCheckbox.setBackground(new Color(250, 250, 250));
        consentCheckbox.addActionListener(this); // Add action listener here

        // Create the submit button
        submitButton = new JButton("Submit");
        submitButton.setBounds(250, 560, 250, 50);
        submitButton.setFocusable(false);
        submitButton.setFont(normalFont);
        submitButton.setEnabled(false);
        submitButton.addActionListener(this); // Add action listener for button click

        // Add components to panel
        this.add(nameLabel);
        this.add(nameField);
        this.add(phoneLabel);
        this.add(phoneField);
        this.add(emailLabel);
        this.add(emailField);
        this.add(sectorLabel);
        this.add(sectorField);
        this.add(sexLabel);
        this.add(sexComboBox);
        this.add(purposeLabel);
        this.add(purposeComboBox);
        this.add(specifyLabel);
        this.add(specifyField);
        this.add(consentCheckbox);
        this.add(submitButton);

        sexComboBox.addKeyListener(this);
        purposeComboBox.addKeyListener(this);
        purposeComboBox.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == consentCheckbox) {
            if (consentCheckbox.isSelected()) {
                submitButton.setEnabled(true);
            } else {
                submitButton.setEnabled(false);
            }
        }
        if (e.getSource() == submitButton) {
            highlightEmptyFields();
            name = nameField.getText();
            phone = phoneField.getText();
            email = emailField.getText();
            sex = (String) sexComboBox.getSelectedItem();
            sector = sectorField.getText();
            
            // Check if the "Others" option is selected in the purposeComboBox
            if (purposeComboBox.getSelectedItem().equals("Others")) {
                // Retrieve the purpose from the specifyField
                purpose = specifyField.getText(); 
                
                // Check if the specifyField is empty
                if (purpose.isEmpty()) {
                	specifyField.requestFocus();
                    return;
                }
            } else {
                // Retrieve the purpose from the combo box
                purpose = (String) purposeComboBox.getSelectedItem();
            }

            boolean consent = consentCheckbox.isSelected();
            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Define the format you want for the date string
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Example format
            LocalTime currentTime = LocalTime.now();

            // Define the format you want for the time string
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a"); // Example format

            // Format the date into a string
            date = currentDate.format(dateFormat);
            // Format the time into a string
            time = currentTime.format(timeFormat);

            if (!consent) {
                System.out.println("Error Consent!");
            } else if (name.isEmpty()) {
                System.out.println("Error name!");
            } else if (sex.equals("--Please Select--")) {
                System.out.println("Error Gender!");
            } else if (sector.isEmpty()) {
                System.out.println("Error Sector!");
            } else if (purpose.equals("--Please Select--")) {
                System.out.println("Error Purpose!");
            } else {
            	// After successful submission block
            	specifyField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));

                DataManagement dm = new DataManagement();
                dm.addData(date, time, name, phone, email, sex, sector, purpose);
                // Clear the text fields
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                sexComboBox.setSelectedIndex(0);
                sectorField.setText("");
                // Reset combo boxes and check box
                purposeComboBox.setSelectedIndex(0);
                specifyField.setText("");
                consentCheckbox.setSelected(false);
                submitButton.setEnabled(false);
                String message = "<html><center><div style='font-size: 15px;'>Your attendance has been successfully submitted.<br>Thank you for your participation!</div></center></html>";

                // Create a dialog with the custom JOptionPane
                JOptionPane.showMessageDialog(null, message, "Popup Message", JOptionPane.PLAIN_MESSAGE);

                nameField.requestFocus();
                // Repaint the panel to reflect the changes
                
                
                this.repaint();
            }
        }
        // Check if the action event is triggered by the purposeComboBox
        if (e.getSource() == purposeComboBox) {
            Object selectedItem = purposeComboBox.getSelectedItem();
            if (selectedItem != null && selectedItem.equals("Others")) {
                specifyLabel.setVisible(true);
                specifyField.setVisible(true);
                consentCheckbox.setBounds(50, 400, 650, 150); // Adjust Y position of consentCheckbox
            } else {
                specifyLabel.setVisible(false);
                specifyField.setVisible(false);
                consentCheckbox.setBounds(50, 370, 650, 150); // Restore Y position of consentCheckbox
            }
        }
    }

    public static void showMissingRequirementDialog() {
        String message = "<html><div style='font-size: 15px;'>One or more required fields are missing. Please fill in all required fields.</div></html>";
        JOptionPane.showMessageDialog(null, message, "Missing Requirement", JOptionPane.INFORMATION_MESSAGE);
    }

    private void highlightEmptyFields() {
        boolean hasError = false;
        byte thickness = 3;

        if (nameField.getText().isEmpty()) {
            nameField.setBorder(BorderFactory.createLineBorder(Color.RED, thickness));
            hasError = true;
        } else {
            nameField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }

        if (sexComboBox.getSelectedIndex() == 0) {
            sexComboBox.setBorder(BorderFactory.createLineBorder(Color.RED, thickness));
            hasError = true;
        } else {
            sexComboBox.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("ComboBox.border"));
        }

        if (sectorField.getText().isEmpty()) {
            sectorField.setBorder(BorderFactory.createLineBorder(Color.RED, thickness));
            hasError = true;
        } else {
            sectorField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }

        if (purposeComboBox.getSelectedIndex() == 0) {
            purposeComboBox.setBorder(BorderFactory.createLineBorder(Color.RED, thickness));
            hasError = true;
        } else {
            purposeComboBox.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("ComboBox.border"));
        }
        
        Object selectedItem = purposeComboBox.getSelectedItem();
        if (selectedItem.equals("Others") && specifyField.getText().isEmpty()) {
            System.out.println("Hello World Goodbye!");
            specifyField.setBorder(BorderFactory.createLineBorder(Color.RED, thickness));
            hasError = true;
        }

        if (hasError) {
            showMissingRequirementDialog();
        }
    }

    // Highlights certain field if left empty
    public static void highlightField() {
        if (!consent) {
            showMissingRequirementDialog();
            consentCheckbox.setBorder(BorderFactory.createLineBorder(Color.RED));
            consentCheckbox.setToolTipText("This field cannot be empty");

            System.out.println("Error Consent!");
        }
        if (name.isEmpty()) {
            showMissingRequirementDialog();
            nameField.setBorder(BorderFactory.createLineBorder(Color.RED));
            nameField.setToolTipText("This field cannot be empty");

            System.out.println("Error name!");
        }
        if (sex.equals("--Please Select--")) {
            showMissingRequirementDialog();
            sexComboBox.setBorder(BorderFactory.createLineBorder(Color.RED));
            sexComboBox.setToolTipText("This field cannot be empty");

            System.out.println("Error Gender!");
        }
        if (sector.isEmpty()) {
            showMissingRequirementDialog();
            sectorField.setBorder(BorderFactory.createLineBorder(Color.RED));
            sectorField.setToolTipText("This field cannot be empty");

            System.out.println("Error Sector!");
        }
        if (purpose.equals("--Please Select--")) {
            showMissingRequirementDialog();
            purposeComboBox.setBorder(BorderFactory.createLineBorder(Color.RED));
            purposeComboBox.setToolTipText("This field cannot be empty");

            System.out.println("Error Purpose!");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
        if (comboBox.isPopupVisible() && e.getKeyCode() == KeyEvent.VK_ENTER) {
            e.consume(); // Consume the event to prevent further processing
            Object selectedItem = comboBox.getSelectedItem();
            if (selectedItem != null) {
                comboBox.setSelectedItem(selectedItem);
            }
            if (selectedItem.equals("Others")) {
                specifyLabel.setVisible(true);
                specifyField.setVisible(true);
                consentCheckbox.setBounds(50, 400, 650, 150);
            } else if (selectedItem.equals("Male") || selectedItem.equals("Female") || sexComboBox.getSelectedItem().equals("--Please Select--")) {
            	// Prevents specifyLabel and specifyField from getting included
            } else {
            	
                specifyLabel.setVisible(false);
                specifyField.setVisible(false);
                consentCheckbox.setBounds(50, 370, 650, 150);
            }
            comboBox.setPopupVisible(false);
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }
}
