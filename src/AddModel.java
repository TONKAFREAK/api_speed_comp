import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AddModel extends JFrame implements ActionListener, KeyListener {

    private JPanel mainPanel;
    private int wWidth = 300;
    private int wHeight = 200;

    JTextField model;
    JTextArea error;

    private String model_name = "";

    private StartScreen startScreen;

    public AddModel(StartScreen startScreen) {
        this.startScreen = startScreen;
        initUI();
        setUndecorated(false);
        setTitle("");
        setSize(wWidth, wHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(false);
        setTitle("Add Model");

    }

    private void initUI() {
        
        this.setLayout(new BorderLayout());
        
        // -----MAIN PANEL--------

        mainPanel = new JPanel(null);

        JTextArea modelText = new JTextArea();
        modelText.setBounds( 45, 25, 100, 20);
        modelText.setEditable(false);
        modelText.setLineWrap(false);
        modelText.setFocusable(false);
        modelText.setWrapStyleWord(true);
        modelText.setOpaque(false);
        modelText.setFont(fontLoader("CONSOLAB.TTF", 14f));
        modelText.setText("Model Name:");
        mainPanel.add(modelText);

        model = new JTextField();
        model.setBounds(50, 50, 200, 25);
        model.setEditable(true);
        model.setFocusable(true);
        model.setOpaque(false);
        model.setFont(fontLoader("CONSOLAB.TTF", 14f));
        mainPanel.add(model);

        JButton sendButton = new JButton("--->");
        sendButton.setBounds(wWidth/2 - 50,100,100,30);
        sendButton.setFont(fontLoader("Enigma_2i.ttf", 15f));
        sendButton.addActionListener(this);
        mainPanel.add(sendButton);

        this.add(mainPanel, BorderLayout.CENTER);

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

            if (model.getText().isEmpty()) {
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

            if (!model.getText().isEmpty() ) {
                model_name = model.getText().trim();

                saveModelToFile(model_name);

                this.setVisible(false);
                this.dispose();
               
            }
        }
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

    public void saveModelToFile(String modelName) {
        
        String filePath = "model_list.txt"; 
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write("\n"+modelName + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        startScreen.selectBox.removeAllItems();
        loadModelsAndAddToComboBox();
    }  

    private void loadModelsAndAddToComboBox() {
        String filePath = "model_list.txt"; 
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String modelName;
            while ((modelName = reader.readLine()) != null) {
                if (!modelName.trim().isEmpty()) {
                    startScreen.selectBox.addItem(modelName.trim());
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     

    
    
}
