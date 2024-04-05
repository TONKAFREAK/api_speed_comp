import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.*;

public class StartScreen extends JFrame implements ActionListener, KeyListener{

    private String selectedModel = "gpt-3.5-turbo";
    private boolean shardAPI = false;
    private boolean oxygenAPI = false;
    private boolean shuttleAPI = false;
    private boolean zukiAPI = false;

    private int wWidth = 800;
    private int wHeight = 600;

    JComboBox<String> selectBox;

    public StartScreen() {
        initUI();
        addActionEvents();
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
        
        Font consolab = fontLoader("/res/fonts/CONSOLAB.TTF", 20f);
        Font Duplexide = fontLoader("/res/fonts/Duplexide.TTF", 70f);
        Font Enigma = fontLoader("/res/fonts/Enigma_2i.TTF", 15f);
        
        // -----MAIN PANEL--------

        JPanel mainPanel = new JPanel(null);

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

        JCheckBox shard = new JCheckBox("Shard API");
        shard.setBounds(t3.getX()+5, 270, 120, 30);
        shard.setFont(Enigma);
        shard.addActionListener(this);
        mainPanel.add(shard);

        JCheckBox oxygen = new JCheckBox("Oxygen API");
        oxygen.setBounds(shard.getX()+shard.getWidth(), 270, 120, 30);
        oxygen.setFont(Enigma);
        oxygen.addActionListener(this);
        mainPanel.add(oxygen);

        JCheckBox shuttle = new JCheckBox("Shuttle API");
        shuttle.setBounds(shard.getX(), shard.getY()+shard.getHeight(), 120, 30);
        shuttle.setFont(Enigma);
        shuttle.addActionListener(this);
        mainPanel.add(shuttle);

        JCheckBox zuki = new JCheckBox("Zuki API");
        zuki.setBounds(shuttle.getX() + shuttle.getWidth(), shuttle.getY(), 120, 30);
        zuki.setFont(Enigma);
        zuki.addActionListener(this);
        mainPanel.add(zuki);

        JButton sendButton = new JButton("---->");
        sendButton.setBounds(wWidth/2 - 100,450,200,50);
        sendButton.setFont(Enigma);
        sendButton.addActionListener(this);
        mainPanel.add(sendButton);

        this.add(mainPanel, BorderLayout.CENTER);

    }
    
    private void addActionEvents() {
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComboBox) {
            JComboBox<?> cb = (JComboBox<?>) e.getSource();
            selectedModel = (String) cb.getSelectedItem();
        } else
        if (e.getActionCommand().equals("---->")) {
            // System.out.println("Selected model: " + selectedModel);
            // System.out.println("Shard API: " + shardAPI);
            // System.out.println("Oxygen API: " + oxygenAPI);
            // System.out.println("Shuttle API: " + shuttleAPI);
            // System.out.println("Zuki API: " + zukiAPI);

            App.openMainScreen(shardAPI, oxygenAPI, shuttleAPI, zukiAPI, selectedModel);
            this.setVisible(false);
            this.dispose();

        } else if (e.getActionCommand().equals("Shard API")) {
            shardAPI = !shardAPI;
        } else if (e.getActionCommand().equals("Oxygen API")) {
            oxygenAPI = !oxygenAPI;
        } else if (e.getActionCommand().equals("Shuttle API")) {
            shuttleAPI = !shuttleAPI;
        } else if (e.getActionCommand().equals("Zuki API")) {
            zukiAPI = !zukiAPI;
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

    public boolean getShardAPI() {
        return shardAPI;
    }

    public boolean getOxygenAPI() {
        return oxygenAPI;
    }

    public boolean getShuttleAPI() {
        return shuttleAPI;
    }

    public boolean getZukiAPI() {
        return zukiAPI;
    }

    public String getSelectedModel() {
        return selectedModel;
    }
    
}
