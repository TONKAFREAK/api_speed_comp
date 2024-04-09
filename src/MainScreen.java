import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MainScreen extends JFrame implements ActionListener, KeyListener {

    JTextArea textArea;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JTextField inputField;

    private String selectedModel;
    private JButton sendButton;
    private Map<String, Boolean> apiSelections;
    private Map<String, String[]> apiDetails = new HashMap<>();

    public MainScreen(){
        //no-arg constructor
    }
    
    public MainScreen(Map<String, Boolean> apiSelections, String selectedModel) {

        this.apiSelections = apiSelections;
        this.selectedModel = selectedModel;

        
        initUI();
        addActionEvents();
        setUndecorated(false);
        setTitle("");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(false);
        setTitle(selectedModel);

    }
    
    private void initUI() {
        

        this.setLayout(new BorderLayout());

        // -----FONTS--------
        
        Font consolab = fontLoader("/res/fonts/CONSOLAB.TTF", 12f);

        //-------------- MENU BAR------------------------

        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(31,31,31));

        //-------------- INPUT FIELD PANEL ---------------------

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        inputField = new JTextField(100);
        inputField.addActionListener(this); 
        inputField.setBackground(new Color(50, 50, 50));
        inputField.setForeground(Color.WHITE);
        inputField.setFont(consolab);

        gbc.insets = new Insets(10, 20, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0; 
        gbc.gridwidth = GridBagConstraints.RELATIVE; 
        gbc.ipady = 20;
        inputPanel.add(inputField, gbc);

        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0; 
        gbc.gridwidth = GridBagConstraints.REMAINDER; 

        inputPanel.add(sendButton, gbc);

      
        this.add(inputPanel, BorderLayout.SOUTH);

        //-------------- API PANEL ---------------------

        JPanel apiPanel = new JPanel();
        apiPanel.setLayout(new BoxLayout(apiPanel, BoxLayout.X_AXIS));
    
        apiSelections.forEach((apiName, isSelected) -> {
            if (Boolean.TRUE.equals(isSelected)) {
                apiPanel.add(createApiPanel(apiName));
            }
        });
    
        add(apiPanel, BorderLayout.CENTER);

    }
    
    private void addActionEvents() {

        inputField.addKeyListener(this);

    }

    private void loadApiDetailsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentApiName = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--")) {
                    currentApiName = line.substring(2).trim(); 
                } else if (!line.trim().isEmpty() && currentApiName != null) {
                   
                    String url = line.trim();
                    String key = reader.readLine().trim();
                    apiDetails.put(currentApiName, new String[]{url, key});
                    currentApiName = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Send")) {
            sendButton.setEnabled(false);
            if (inputField.getText().trim().isEmpty()) {
                sendButton.setEnabled(true);
                return;
            }
            String prompt = inputField.getText();
            inputField.setText(""); 

            loadApiDetailsFromFile("api_list.txt");

            new Thread(() -> {
                 
                List<CompletableFuture<Void>> futures = new ArrayList<>();

                apiSelections.forEach((apiName, isSelected) -> {
                    if (Boolean.TRUE.equals(isSelected)) {
                        String[] details = apiDetails.get(apiName);
                        if (details != null) {
                            futures.add(sendHttpRequest(apiName, details[0], details[1], selectedModel, prompt));
                        }
                    }
                });
                
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            

        }).start(); // Start the background thread

        Timer timer = new Timer(3000, event -> sendButton.setEnabled(true));
        timer.setRepeats(false); 
        timer.start();
    }
}
    //----------------- KEY LISTENERS -------------------

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == inputField && e.getKeyCode() == KeyEvent.VK_ENTER) {
           
            ActionEvent sendEvent = new ActionEvent(sendButton, ActionEvent.ACTION_PERFORMED, "Send");
            actionPerformed(sendEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    //----------------- FONT LOADER -------------------

    private Font fontLoader(String fontPath, float fontSize) {
        try {
            InputStream is = MainScreen.class.getResourceAsStream(fontPath);
            if (is == null) {
                System.err.println("Font file not found at " + fontPath);
                return null;
            }
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontSize);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, JTextArea> apiTextAreas = new HashMap<>();

    //----------------- API PANEL -------------------

    private JPanel createApiPanel(String apiName) {
        JPanel panel = new JPanel(new GridBagLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder(apiName);
        titledBorder.setTitleFont(new Font("Serif", Font.BOLD, 14));
        panel.setBorder(titledBorder);  
    
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 0, 0, 0); 
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0; 
        gbc.gridx = 0; 
        gbc.gridy = 0; 

        
        JTextArea responseArea = new JTextArea();
        responseArea.setEditable(false);
        responseArea.setForeground(Color.WHITE);
        responseArea.setLineWrap(true);
        responseArea.setFocusable(false);
        responseArea.setWrapStyleWord(true);
        responseArea.setOpaque(true);
        responseArea.setBorder(null);
    
        JScrollPane scrollPane = new JScrollPane(responseArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
    
        panel.add(scrollPane,gbc);

        apiTextAreas.put(apiName, responseArea);

        System.out.println("Panel created for: " + apiName);
        
        return panel;
    }

    //----------------- HTTP REQUEST -------------------

    private Map<String, List<String>> apiChatHistories = new HashMap<>();

    private CompletableFuture<Void> sendHttpRequest(String name, String url, String api_key, String model, String prompt) {
    HttpClient client = HttpClient.newBuilder()
        .version(Version.HTTP_2)
        .build();

   
    JSONArray messages = new JSONArray();
    
    List<String> chatHistory = apiChatHistories.getOrDefault(name, new ArrayList<>());
    for (String message : chatHistory) {
        JSONObject msgObj = new JSONObject();
        msgObj.put("role", message.startsWith("User:") ? "user" : "system");
        msgObj.put("content", message.substring(message.indexOf(':') + 2)); 
        messages.put(msgObj);
    }

   
    JSONObject newUserMessage = new JSONObject();
    newUserMessage.put("role", "user");
    newUserMessage.put("content", prompt);
    messages.put(newUserMessage);

    JSONObject data = new JSONObject();
    data.put("model", model);
    data.put("messages", messages);
    data.put("stream", false);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofMinutes(2))
        .header("Authorization", "Bearer " + api_key)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
        .build();

    long startTime = System.nanoTime();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(responseBody -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    if (responseBody.trim().startsWith("{")) {
                        JSONObject responseJson = new JSONObject(responseBody);
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime) / 1_000_000;
                        JTextArea textArea = apiTextAreas.get(name); // Assuming this is defined elsewhere
                        String content = "";

                        try {
                            content = responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                            // Add the new messages (user prompt and API response) to chat history
                            addMessageToChatHistory(name, "User: " + prompt, name + " Response: " + content);
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage() + " " + responseJson.toString());
                            textArea.append("\n>> Need premium key to use " + model);
                            return;
                        }

                        if (textArea != null) {
                            textArea.append("\n>> Response Time: " + duration + " ms\n" + content + "\n");
                        }
                    } else {
                        System.err.println("Response is not JSON or there was an error: " + responseBody);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        })
        .exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
}

private void addMessageToChatHistory(String name, String userPrompt, String apiResponse) {
    apiChatHistories.putIfAbsent(name, new ArrayList<>());
    List<String> chatHistory = apiChatHistories.get(name);
    chatHistory.add(userPrompt); 
    chatHistory.add(apiResponse); 
}
    

}
