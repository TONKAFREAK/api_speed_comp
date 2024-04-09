import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.*;

public class StartScreen extends JFrame implements ActionListener, KeyListener{

    private String selectedModel = "gpt-3.5-turbo";
    private int wWidth = 800;
    private int wHeight = 600;

    private JComboBox<String> selectBox;
    private AddAPI addAPI = null;

    private HashMap<String, Boolean> apiSelections = new HashMap<>();

    private JPanel mainPanel;

    public StartScreen() {
        initUI();
        setUndecorated(false);
        setTitle("");
        setSize(wWidth, wHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(false);

    }
    
    private void initUI() {

        this.setLayout(new BorderLayout());

        // -----FONTS--------
        
        Font consolab = fontLoader("CONSOLAB.TTF", 20f);
        Font Duplexide = fontLoader("Duplexide.ttf", 70f);
        Font Enigma = fontLoader("Enigma_2i.ttf", 15f);
        
        // -----MAIN PANEL--------

        mainPanel = new JPanel(null);

        JTextArea t1 = new JTextArea();
        t1.setBounds(wWidth/2 - 300, 100, 600, 150);
        t1.setEditable(false);
        t1.setLineWrap(false);
        t1.setFocusable(false);
        t1.setWrapStyleWord(true);
        t1.setOpaque(false);
        t1.setFont(Duplexide);
        t1.setText("API SPEED TEST");
        mainPanel.add(t1);

        JTextArea tonka = new JTextArea();
        tonka.setBounds(560, 180, 600, 50);
        tonka.setEditable(false);
        tonka.setLineWrap(false);
        tonka.setFocusable(false);       //REMOVE IF GAY
        tonka.setWrapStyleWord(true);
        tonka.setOpaque(false);
        tonka.setFont(fontLoader("Duplexide.ttf", 15f));
        tonka.setText("BY TONKA");
        mainPanel.add(tonka);

        JTextArea t2 = new JTextArea();
        t2.setBounds(t1.getX(), 250, 330, 30);
        t2.setEditable(false);
        t2.setLineWrap(false);
        t2.setFocusable(false);
        t2.setWrapStyleWord(true);
        t2.setOpaque(false);
        t2.setFont(consolab);
        t2.setText("Select a model to use:");
        mainPanel.add(t2);

        selectBox = new JComboBox<>();
        selectBox.addItem("gpt-3.5-turbo");
        selectBox.addItem("gpt-4");
        selectBox.addItem("claude-3-opus");
        selectBox.addItem("claude-3-sonnet");
        selectBox.addItem("claude-3-haiku");
        selectBox.setBounds(t2.getX()+5, 280, 240, 30);
        selectBox.addActionListener(this);
        mainPanel.add(selectBox);

        JTextArea t3 = new JTextArea();
        t3.setBounds(t2.getX()+t2.getWidth(), 250, 300, 30);
        t3.setEditable(false);
        t3.setLineWrap(false);
        t3.setFocusable(false);
        t3.setWrapStyleWord(true);
        t3.setOpaque(false);
        t3.setFont(consolab);
        t3.setText("Select APIs to use:");
        mainPanel.add(t3);

        JButton sendButton = new JButton("---->");
        sendButton.setBounds(wWidth/2 - 100,450,200,50);
        sendButton.setFont(Enigma);
        sendButton.addActionListener(this);
        mainPanel.add(sendButton);

        JButton addButton = new JButton("+");
        addButton.setBounds(700, 275,25,25);
        addButton.setFont(fontLoader("CONSOLAB.TTF", 20f));
        addButton.setBorder(BorderFactory.createEmptyBorder());
        addButton.addActionListener(this);
        mainPanel.add(addButton);

        JButton removeButton = new JButton("-");
        removeButton.setBounds(700, 310,25,25);
        removeButton.setFont(fontLoader("CONSOLAB.TTF", 20f));
        removeButton.setBorder(BorderFactory.createEmptyBorder());
        removeButton.addActionListener(this);
        mainPanel.add(removeButton);

        loadAPIsAndCreateCheckboxes();
        this.add(mainPanel, BorderLayout.CENTER);

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            apiSelections.put(checkBox.getActionCommand(), checkBox.isSelected());
        } else
        if (e.getActionCommand().equals("---->") ) {
           

            if(apiSelections.containsValue(true)){
            App.openMainScreen(apiSelections, selectedModel);
            this.setVisible(false);
            this.dispose();
            } else {
                JTextArea error = new JTextArea();
                error.setBounds(wWidth/2 - 75,430, 300, 300);
                error.setEditable(false);
                error.setLineWrap(false);
                error.setFocusable(false);
                error.setWrapStyleWord(true);
                error.setOpaque(true);
                error.setFont(fontLoader("CONSOLAB.TTF", 12f));
                error.setForeground(java.awt.Color.RED);
                error.setText("Select APIs to use");
                mainPanel.add(error);
                mainPanel.repaint();
            }

        } 

        if (e.getActionCommand().equals("+") ) {

            if (addAPI == null || !addAPI.isVisible()) {
                addAPI = new AddAPI(this);
                addAPI.setVisible(true);
            } else {
                addAPI.toFront(); 
                addAPI.requestFocus(); 
            }

        }

        if (e.getActionCommand().equals("-") ) {
            
            if(panelHasCheckBox(mainPanel)){
            
            RemoveAPI addAPI = new RemoveAPI(this);
            addAPI.setVisible(true);
            }

        }
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

    private Font fontLoader(String fontPath, float fontSize) {
        try {
            InputStream is = getClass().getResourceAsStream("res/fonts/" + fontPath);
            System.out.println("Font URL: " + is);
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
    

    private void loadAPIsAndCreateCheckboxes() {
        
        String filePath = "api_list.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            int yPosition = 275; 
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--")) {
                    String apiName = line.substring(2).trim();
                    JCheckBox checkBox = new JCheckBox(apiName + " ");
                    
                    int xPosition = (count % 2 == 0) ? 435 : 435 + 120 + 5; 
                    if (count % 2 == 0 && count > 0) {
                        yPosition += 35; 
                    }
                    checkBox.setBounds(xPosition, yPosition, 120, 30);
                    checkBox.setFont(fontLoader("Enigma_2i.ttf", 15f));
                    checkBox.setActionCommand(apiName);
                    checkBox.addActionListener(this);
                    mainPanel.add(checkBox);
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadAPIsAndCheckboxes() {
        mainPanel.removeAll(); 
        initUI(); 
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private boolean panelHasCheckBox(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JCheckBox) {
                return true; 
            }
        }
        return false; 
    }
    
    
}
