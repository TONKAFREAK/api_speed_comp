import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

import javax.swing.*;

public class RemoveModel extends JFrame implements ActionListener, KeyListener {

    private JPanel mainPanel;
    private int wWidth = 300;
    private int wHeight = 200;

    JTextField modelNameTextField;
    JTextArea errorTextArea;

    private StartScreen startScreen;

    public RemoveModel(StartScreen startScreen) {
        this.startScreen = startScreen;
        initUI();
        setUndecorated(false);
        setTitle("Remove Model");
        setSize(wWidth, wHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void initUI() {
        this.setLayout(new BorderLayout());
        mainPanel = new JPanel(null);

        JLabel modelNameLabel = new JLabel("Model Name:");
        modelNameLabel.setBounds(45, 25, 100, 20);
        mainPanel.add(modelNameLabel);

        modelNameTextField = new JTextField();
        modelNameTextField.setBounds(50, 50, 200, 25);
        mainPanel.add(modelNameTextField);

        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(wWidth/2 - 50,100,100,30);
        removeButton.addActionListener(this);
        mainPanel.add(removeButton);

        errorTextArea = new JTextArea();
        errorTextArea.setBounds(wWidth/2 - 85,80,180,30);
        errorTextArea.setEditable(false);
        mainPanel.add(errorTextArea);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String modelName = modelNameTextField.getText().trim();
        if (modelName.isEmpty()) {
            errorTextArea.setText("Please enter the Model name");
            return;
        }
        removeModelFromFile(modelName);
    }

    private void removeModelFromFile(String modelName) {
        File inputFile = new File("model_list.txt");
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
    
        boolean modelExists = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String currentLine;
    
            while ((currentLine = reader.readLine()) != null) {
                
                if (!currentLine.trim().equals(modelName)) {
                    writer.write(currentLine + System.lineSeparator());
                } else {
                    modelExists = true; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorTextArea.setText("Error removing Model.");
            return;
        }
    
        if (!modelExists) {
            errorTextArea.setText("Model name does not exist.");
    
           
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
            errorTextArea.setText("Model removed successfully.");
            startScreen.selectBox.removeAllItems();
            loadModelsAndAddToComboBox();
            this.setVisible(false);
            this.dispose();
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
