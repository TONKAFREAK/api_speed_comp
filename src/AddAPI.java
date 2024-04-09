import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AddAPI extends JFrame implements ActionListener, KeyListener {

    private JPanel mainPanel;
    private int wWidth = 500;
    private int wHeight = 300;

    JTextField keyAPI;
    JTextField endpointURL;
    JTextField apiName;
    JTextArea error;

    private String api_Name = "";
    private String api_Endpoint = "";
    private String api_Key = "";

    private StartScreen startScreen;

    public AddAPI(StartScreen startScreen) {
        this.startScreen = startScreen;
        initUI();
        addActionEvents();
        setUndecorated(false);
        setTitle("");
        setSize(wWidth, wHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(false);
        setTitle("new API");

    }

    private void initUI() {
        
        this.setLayout(new BorderLayout());

        // -----FONTS--------
        
        Font Enigma = fontLoader("Enigma_2i.ttf", 15f);
        
        // -----MAIN PANEL--------

        mainPanel = new JPanel(null);

        JTextArea apiText = new JTextArea();
        apiText.setBounds( 45, 25, 100, 20);
        apiText.setEditable(false);
        apiText.setLineWrap(false);
        apiText.setFocusable(false);
        apiText.setWrapStyleWord(true);
        apiText.setOpaque(false);
        apiText.setFont(fontLoader("CONSOLAB.TTF", 14f));
        apiText.setText("API Name:");
        mainPanel.add(apiText);

        apiName = new JTextField();
        apiName.setBounds(50, 50, 400, 25);
        apiName.setEditable(true);
        apiName.setFocusable(true);
        apiName.setOpaque(false);
        apiName.setFont(fontLoader("CONSOLAB.TTF", 14f));
        mainPanel.add(apiName);

        JTextArea endPointText = new JTextArea();
        endPointText.setBounds( 45, 85, 110, 20);
        endPointText.setEditable(false);
        endPointText.setLineWrap(false);
        endPointText.setFocusable(false);
        endPointText.setWrapStyleWord(true);
        endPointText.setOpaque(false);
        endPointText.setFont(fontLoader("CONSOLAB.TTF", 14f));
        endPointText.setText("Endpoint URL:");
        mainPanel.add(endPointText);

        endpointURL = new JTextField();
        endpointURL.setBounds(  50, 110, 400, 25);
        endpointURL.setEditable(true);
        endpointURL.setFocusable(true);
        endpointURL.setOpaque(false);
        endpointURL.setFont(fontLoader("CONSOLAB.TTF", 14f));
        mainPanel.add(endpointURL);

        JTextArea keyText = new JTextArea();
        keyText.setBounds( 45, 145, 110, 25);
        keyText.setEditable(false);
        keyText.setLineWrap(false);
        keyText.setFocusable(false);
        keyText.setWrapStyleWord(true);
        keyText.setOpaque(false);
        keyText.setFont(fontLoader("CONSOLAB.TTF", 14f));
        keyText.setText("API Key:");
        mainPanel.add(keyText);

        keyAPI = new JTextField();
        keyAPI.setBounds(  50, 170, 400, 25);
        keyAPI.setEditable(true);
        keyAPI.setFocusable(true);
        keyAPI.setOpaque(false);
        keyAPI.setHorizontalAlignment(SwingConstants.LEFT);
        keyAPI.setFont(fontLoader("CONSOLAB.TTF", 14f));
        mainPanel.add(keyAPI);

        JButton sendButton = new JButton("--->");
        sendButton.setBounds(wWidth/2 - 50,220,100,30);
        sendButton.setFont(Enigma);
        sendButton.addActionListener(this);
        mainPanel.add(sendButton);

        this.add(mainPanel, BorderLayout.CENTER);


    }

    private void addActionEvents() {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("--->")) {

            if (keyAPI.getText().isEmpty() || endpointURL.getText().isEmpty() || apiName.getText().isEmpty()) {
                if (error != null) {
                    mainPanel.remove(error);
                }
                error = new JTextArea();
                error.setEditable(false);
                error.setLineWrap(false);
                error.setFocusable(false);
                error.setWrapStyleWord(true);
                error.setOpaque(false);
                error.setFont(fontLoader("Roboto-Regular.TTF", 12f));
                error.setText("Please fill in all the fields");
                error.setForeground(java.awt.Color.RED);
                error.setBounds( wWidth/2-65, 200, 150, 25);
                mainPanel.add(error);
                mainPanel.repaint();
            } 

            if (!keyAPI.getText().isEmpty() && !endpointURL.getText().isEmpty() && !apiName.getText().isEmpty()) {
                api_Name = apiName.getText().trim();
                api_Endpoint= endpointURL.getText().trim();
                api_Key= keyAPI.getText().trim();

                saveAPIDetailsToFile(apiName.getText().trim(), endpointURL.getText().trim(), keyAPI.getText().trim());

                this.setVisible(false);
                this.dispose();
               
            }
            
        }
    }

    // -----FONT LOADER--------

    private Font fontLoader(String fontPath, float fontSize) {
        try {
            InputStream is = getClass().getResourceAsStream("res/fonts/" + fontPath);
            if (is == null) {
                System.err.println("Font file not found at res/fonts/" + fontPath);
                return null;
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontSize);
            is.close();
            return font;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    public void saveAPIDetailsToFile(String apiName, String apiUrl, String apiKey) {
        
        String filePath = "api_list.txt"; 
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write("\n--" + apiName + "\n");
            writer.write(apiUrl + "\n");
            writer.write(apiKey + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        startScreen.reloadAPIsAndCheckboxes();
    }
    
}
