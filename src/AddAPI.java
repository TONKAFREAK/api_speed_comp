import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public static ArrayList<String> api_Name = new ArrayList<String>();
    public static ArrayList<String> api_Endpoint = new ArrayList<String>();
    public static ArrayList<String> api_Key = new ArrayList<String>();


    public AddAPI() {
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
        
        Font Enigma = fontLoader("/res/fonts/Enigma_2i.TTF", 15f);
        
        // -----MAIN PANEL--------

        mainPanel = new JPanel(null);

        JTextArea apiText = new JTextArea();
        apiText.setBounds( 45, 25, 100, 20);
        apiText.setEditable(false);
        apiText.setLineWrap(false);
        apiText.setFocusable(false);
        apiText.setWrapStyleWord(true);
        apiText.setOpaque(false);
        apiText.setFont(fontLoader("/res/fonts/CONSOLAB.TTF", 14f));
        apiText.setText("API Name:");
        mainPanel.add(apiText);

        apiName = new JTextField();
        apiName.setBounds(50, 50, 400, 25);
        apiName.setEditable(true);
        apiName.setFocusable(true);
        apiName.setOpaque(false);
        apiName.setFont(fontLoader("/res/fonts/CONSOLAB.TTF", 14f));
        mainPanel.add(apiName);

        JTextArea endPointText = new JTextArea();
        endPointText.setBounds( 45, 85, 110, 20);
        endPointText.setEditable(false);
        endPointText.setLineWrap(false);
        endPointText.setFocusable(false);
        endPointText.setWrapStyleWord(true);
        endPointText.setOpaque(false);
        endPointText.setFont(fontLoader("/res/fonts/CONSOLAB.TTF", 14f));
        endPointText.setText("Endpoint URL:");
        mainPanel.add(endPointText);

        endpointURL = new JTextField();
        endpointURL.setBounds(  50, 110, 400, 25);
        endpointURL.setEditable(true);
        endpointURL.setFocusable(true);
        endpointURL.setOpaque(false);
        endpointURL.setFont(fontLoader("/res/fonts/CONSOLAB.TTF", 14f));
        mainPanel.add(endpointURL);

        JTextArea keyText = new JTextArea();
        keyText.setBounds( 45, 145, 110, 25);
        keyText.setEditable(false);
        keyText.setLineWrap(false);
        keyText.setFocusable(false);
        keyText.setWrapStyleWord(true);
        keyText.setOpaque(false);
        keyText.setFont(fontLoader("/res/fonts/CONSOLAB.TTF", 14f));
        keyText.setText("API Key:");
        mainPanel.add(keyText);

        keyAPI = new JTextField();
        keyAPI.setBounds(  50, 170, 400, 25);
        keyAPI.setEditable(true);
        keyAPI.setFocusable(true);
        keyAPI.setOpaque(false);
        keyAPI.setHorizontalAlignment(SwingConstants.LEFT);
        keyAPI.setFont(fontLoader("/res/fonts/CONSOLAB.TTF", 14f));
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
                error.setFont(fontLoader("/res/fonts/Roboto-Regular.TTF", 12f));
                error.setText("Please fill in all the fields");
                error.setForeground(java.awt.Color.RED);
                error.setBounds( wWidth/2-65, 200, 150, 25);
                mainPanel.add(error);
                mainPanel.repaint();
            } 

            if (!keyAPI.getText().isEmpty() && !endpointURL.getText().isEmpty() && !apiName.getText().isEmpty()) {
                api_Name.add(apiName.getText().trim());
                api_Endpoint.add(endpointURL.getText().trim());
                api_Key.add(keyAPI.getText().trim());

                this.setVisible(false);
                this.dispose();
               
            }
            
        }
    }

    // -----FONT LOADER--------

    public static Font fontLoader(String fontPath, float fontSize) {
        try {
            InputStream is = StartScreen.class.getResourceAsStream(fontPath);
            if (is == null) {
                System.err.println("Font file not found at " + fontPath);
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
    
}
