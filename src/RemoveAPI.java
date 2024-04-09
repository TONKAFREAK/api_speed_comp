import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

import javax.swing.*;

public class RemoveAPI extends JFrame implements ActionListener, KeyListener {

    private JPanel mainPanel;
    private int wWidth = 500;
    private int wHeight = 300;

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
        apiNameLabel.setBounds(50, 50, 400, 25);
        mainPanel.add(apiNameLabel);

        apiNameTextField = new JTextField();
        apiNameTextField.setBounds(50, 80, 400, 25);
        mainPanel.add(apiNameTextField);

        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(wWidth / 2 - 50, 220, 100, 30);
        removeButton.addActionListener(this);
        mainPanel.add(removeButton);

        errorTextArea = new JTextArea();
        errorTextArea.setBounds(50, 110, 400, 50);
        errorTextArea.setEditable(false);
        mainPanel.add(errorTextArea);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String apiName = apiNameTextField.getText().trim();
        if (apiName.isEmpty()) {
            errorTextArea.setText("Please enter the API name.");
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
            
        }

        startScreen.reloadAPIsAndCheckboxes();
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
