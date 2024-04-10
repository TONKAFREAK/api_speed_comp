import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class RemoveAPI extends JFrame implements ActionListener, KeyListener {

    private JPanel mainPanel;
    private int wWidth = 300;
    private int wHeight = 200;

    JTextField apiNameTextField;
    JTextArea errorTextArea;

    private StartScreen startScreen;

    public RemoveAPI(StartScreen startScreen) {
        this.startScreen = startScreen;
        initUI();
        setUndecorated(false);
        setTitle("Remove API");
        setSize(wWidth, wHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void initUI() {
        this.setLayout(new BorderLayout());
        mainPanel = new JPanel(null);

        JLabel apiNameLabel = new JLabel("API Name:");
        apiNameLabel.setBounds(45, 25, 100, 20);
        mainPanel.add(apiNameLabel);

        apiNameTextField = new JTextField();
        apiNameTextField.setBounds(50, 50, 200, 25);
        mainPanel.add(apiNameTextField);

        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(wWidth/2 - 50,100,100,30);
        removeButton.addActionListener(this);
        mainPanel.add(removeButton);

        errorTextArea = new JTextArea();
        errorTextArea.setBounds(wWidth/2 - 75,80,180,30);
        errorTextArea.setEditable(false);
        mainPanel.add(errorTextArea);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String apiName = apiNameTextField.getText().trim();
        if (apiName.isEmpty()) {
            errorTextArea.setText("Please enter the API name");
            return;
        }
        removeAPIDetailsFromFile(apiName);
    }

    private void removeAPIDetailsFromFile(String apiName) {
        File inputFile = new File("api_list.txt");
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        boolean apiExists = false;
        boolean skipNextLines = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            
            while ((currentLine = reader.readLine()) != null) {
                
                if (currentLine.trim().equals("--" + apiName)) {
                    apiExists = true;
                    skipNextLines = true;
                    continue;
                }
                if (skipNextLines) {
                    if (currentLine.trim().isEmpty() || currentLine.startsWith("--")) {
                        skipNextLines = false;
                        writer.write(currentLine + System.lineSeparator());
                    }
                } else {
                    writer.write(currentLine + System.lineSeparator());
                }

                
        }
        } catch (IOException e) {
            e.printStackTrace();
            errorTextArea.setText("Error removing API.");
            return;
        }

        if (!apiExists) {
            errorTextArea.setText("API name does not exist.");
            
            if (!tempFile.delete()) {
                System.err.println("Could not delete temporary file.");
            }
            return;
        }

        if (!inputFile.delete()) {
            errorTextArea.setText("Could not delete original file.");
            return;
        }
        
        if (!tempFile.renameTo(inputFile)) {
            errorTextArea.setText("Could not rename temporary file.");
        } else {
            errorTextArea.setText("API removed successfully.");
            removeAllCheckBoxes(startScreen.mainPanel);
            loadAPIsAndCreateCheckboxes();
            this.setVisible(false);
            this.dispose();
            
        }
    }

    private void removeAllCheckBoxes(JPanel panel) {
        
        List<Component> checkboxesToRemove = new ArrayList<>();
    
      
        for (Component comp : panel.getComponents()) {
            
            if (comp instanceof JCheckBox) {
               
                checkboxesToRemove.add(comp);
            }
        }
    
        
        for (Component checkBox : checkboxesToRemove) {
            panel.remove(checkBox);
        }
    
        
        panel.revalidate();
        panel.repaint();
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
                    startScreen.mainPanel.add(checkBox);
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Font fontLoader(String fontPath, float fontSize) {
        try {
            InputStream is = getClass().getResourceAsStream("res/fonts/" + fontPath);
            //System.out.println("Font URL: " + is);
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
