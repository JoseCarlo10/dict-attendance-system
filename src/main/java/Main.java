import javax.swing.*;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Main extends JFrame implements ActionListener, WindowStateListener, MouseListener {
    
    Font normalFont = new Font("Arial", Font.PLAIN, 24); // Set font size to 20

    JLabel bagong_pilipinas_label, dict_label, ilcdb_label, dtc_label, tech4ed_label, dateTimeLabel;
    ImageIcon bagong_pilipinas, dict, ilcdb, dtc, tech4ed;
    JPanel headerImage;
    JPanel dateTime;
    JButton exitButton;
    JButton hideButton;
    JButton queryButton;
    SimpleDateFormat dateFormat;
    
    // Defining PC platform's height and width
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    int screenWidth = screenSize.width;
    int screen_width = screenWidth;
    int screenHeight = screenSize.height;
    int screen_height = screenHeight;
    
    byte yImageLocation = 30;

    public static void main(String[] args) {
        new Main();
    }

    private void updateDateTime() {
    	// Initiate date and time
        Date now = new Date();
        String date = new SimpleDateFormat("EEE, MMM d, yyyy").format(now);
        String time = new SimpleDateFormat("hh:mm:ss a").format(now);
        dateTimeLabel.setText("<html><center>" + time + "<br><br>" + date + "</center></html>");
    }

    public Main() {
        // Set background color for the frame
        this.getContentPane().setBackground(new Color(240, 240, 240));

    	// Create Query Button
    	queryButton = new JButton("Open Query");
    	queryButton.setLayout(null);
    	queryButton.setBounds((int)(screenWidth-(int)(screen_width - (screen_width * .85))), 
    	                      (int)(screenHeight-(screenHeight * .8)), 
    	                      (int)(screenWidth - (screenWidth * .87)), 
    	                      (int)(screenHeight - (screenHeight * .93)));

    	queryButton.setFocusPainted(false);
    	queryButton.setBackground(new Color(59, 89, 182)); // Background color changed
    	queryButton.setForeground(Color.WHITE);
    	queryButton.setFont(new Font("Tahoma", Font.BOLD, 22));
    	queryButton.setBorder(BorderFactory.createCompoundBorder(
    	    BorderFactory.createLineBorder(new Color(41, 64, 152), 2),
    	    BorderFactory.createEmptyBorder(10, 20, 10, 20)
    	));
    	queryButton.setOpaque(true);
    	queryButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        queryButton.addActionListener(this);
        queryButton.addMouseListener(this);
        
        // Set Date and Time Properties
        dateTime = new JPanel(new BorderLayout());
        dateTime.setBackground(Color.WHITE);
        dateTime.setBounds((screenWidth-300), (screenHeight-100), 300, 100);

        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("Arial", Font.BOLD, 27));
        dateTimeLabel.setHorizontalAlignment(JLabel.CENTER);
        updateDateTime();

        dateTime.add(dateTimeLabel, BorderLayout.CENTER);

        // Update date and time every second
        Timer timer = new Timer(1000, e -> updateDateTime());
        timer.start();

        // Fetch image resources
        bagong_pilipinas = new ImageIcon(getClass().getClassLoader().getResource("bagong-pilipinas.png"));
        dict = new ImageIcon(getClass().getClassLoader().getResource("dict.png"));
        dtc = new ImageIcon(getClass().getClassLoader().getResource("dtc.png"));
        ilcdb = new ImageIcon(getClass().getClassLoader().getResource("ilcdb.png"));
        tech4ed = new ImageIcon(getClass().getClassLoader().getResource("tech4ed.png"));

        // Adjust each image original size
        Image bagong_pilipinas_scaledImage = bagong_pilipinas.getImage().getScaledInstance(170, 170, java.awt.Image.SCALE_SMOOTH);
        Image dict_scaledImage = dict.getImage().getScaledInstance(270, 120, java.awt.Image.SCALE_SMOOTH);
        Image dtc_scaledImage = dtc.getImage().getScaledInstance(110, 160, java.awt.Image.SCALE_SMOOTH);
        Image ilcdb_scaledImage = ilcdb.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
        Image tech4ed_scaledImage = tech4ed.getImage().getScaledInstance(280, 120, java.awt.Image.SCALE_SMOOTH);

        // Set image icon
        bagong_pilipinas = new ImageIcon(bagong_pilipinas_scaledImage);
        dict = new ImageIcon(dict_scaledImage);
        dtc = new ImageIcon(dtc_scaledImage);
        ilcdb = new ImageIcon(ilcdb_scaledImage);
        tech4ed = new ImageIcon(tech4ed_scaledImage);

        /*	Toolkit toolkit = Toolkit.getDefaultToolkit();
	    	Dimension screenSize = toolkit.getScreenSize();
		    int screenWidth = screenSize.width;
		    int screen_width = screenWidth;
		    int screenHeight = screenSize.height;
		    int screen_height = screenHeight;
        */
        
        // Set image location
        bagong_pilipinas_label = new JLabel(bagong_pilipinas);
        bagong_pilipinas_label.setBounds(((screen_width/2)-500), yImageLocation, dict.getIconWidth(), dict.getIconHeight());
        
        dict_label = new JLabel(dict);
        dict_label.setBounds(((screen_width/2)-275), yImageLocation, dict.getIconWidth(), dict.getIconHeight());
        
        dtc_label = new JLabel(dtc);
        dtc_label.setBounds((screen_width/2), yImageLocation, dtc.getIconWidth(), dict.getIconHeight());
        
        ilcdb_label = new JLabel(ilcdb);
        ilcdb_label.setBounds(((screen_width/2)+110), yImageLocation, ilcdb.getIconWidth(), dict.getIconHeight());
        
        tech4ed_label = new JLabel(tech4ed);
        tech4ed_label.setBounds(((screen_width/2)+320), yImageLocation, tech4ed.getIconWidth(), dict.getIconHeight());
        
        // Set Hide Button properties
        hideButton = new JButton("Hide");
        hideButton.setBounds(screen_width - 120, 0, 60, 25);
        hideButton.addActionListener(this);
        hideButton.setFocusable(false);
        hideButton.setBackground(new Color(242, 255, 51)); // Background color changed
        hideButton.setForeground(Color.BLACK);

        // Set Exit Button properties
        exitButton = new JButton("Exit");
        exitButton.setBounds((screen_width - 60), 0, 60, 25);
        exitButton.addActionListener(this);
        exitButton.setFocusable(false);
        exitButton.setBackground(new Color(252, 55, 25)); // Background color changed
        exitButton.setForeground(Color.BLACK);

        this.add(queryButton);
        this.add(dateTime);
        this.add(hideButton);
        this.add(exitButton);
        this.add(bagong_pilipinas_label);
        this.add(dict_label);
        this.add(dtc_label);
        this.add(ilcdb_label);
        this.add(tech4ed_label);
        this.add(new AttendancePanel());

        this.setTitle("DICT Attendance");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);    // Set the size to full screen
        this.setLayout(null);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setVisible(true);

        this.addWindowStateListener(this); // Add WindowStateListener to the frame
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	if (e.getSource() == queryButton) { // If Query Button is clicked, open another JFrame Query
    		
    	    JPanel panel = new JPanel();
    	    JPasswordField passwordField = new JPasswordField(20); // Adjust the size of the password field as needed
    	    panel.add(passwordField);

    	    // Show the JOptionPane with the custom JPanel
    	    int result = JOptionPane.showConfirmDialog(null, panel, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    	    if (result == JOptionPane.OK_OPTION) {
    	        char[] password = passwordField.getPassword();
    	        String userInput = new String(password); // Convert char array to String
    	        if (userInput.equals("root")) {
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
    	        } else {
    	            JOptionPane.showMessageDialog(null, "INCORRECT PASSWORD!", "Message", JOptionPane.INFORMATION_MESSAGE);
    	        }
    	        // Clear the password after usage for security reasons
    	        Arrays.fill(password, ' '); // Clear the password array
    	    } else {
    	        // User clicked Cancel or closed the dialog
    	        JOptionPane.showMessageDialog(null, "You canceled the operation.");
    	    }
    	}

        if (e.getSource() == exitButton) {		// Closes the Application
        	int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
            	this.dispose();
            } else {
                // User clicked No or closed the dialog, do nothing or handle accordingly
            }
            
        }

        if (e.getSource() == hideButton) {		// Hides the Application
            this.setExtendedState(Frame.ICONIFIED);
        }

    }


    @Override
    public void windowStateChanged(WindowEvent e) {		// Calls back to fullscreen if hidden
        if (e.getNewState() == Frame.NORMAL) {
            this.setExtendedState(Frame.MAXIMIZED_BOTH); // Restore to fullscreen
        }
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		queryButton.setBackground(new Color(41, 64, 152));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		queryButton.setBackground(new Color(59, 89, 182));
	}
}
