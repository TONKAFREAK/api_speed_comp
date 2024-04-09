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

    private boolean shardAPI;
    private boolean oxygenAPI;
    private boolean shuttleAPI;
    private boolean zukiAPI;
    private String selectedModel;
    private JButton sendButton;

    public MainScreen(){
        //no-arg constructor
    }
    
    public MainScreen(boolean shardAPI, boolean oxygenAPI, boolean shuttleAPI, boolean zukiAPI, String selectedModel) {

        this.shardAPI = shardAPI;
        this.oxygenAPI = oxygenAPI;
        this.shuttleAPI = shuttleAPI;
        this.zukiAPI = zukiAPI;
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

        // System.out.println("-------");
        // System.out.println("Shard API: " + shardAPI);
        // System.out.println("Oxygen API: " + oxygenAPI);
        // System.out.println("Shuttle API: " + shuttleAPI);
        // System.out.println("Zuki API: " + zukiAPI);
        // System.out.println("Selected Model: " + selectedModel);

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
        
       
        if (zukiAPI) {
            apiPanel.add(createApiPanel("Zuki API"));
        }
        if (shuttleAPI) {
            apiPanel.add(createApiPanel("Shuttle API"));
        }
        if (oxygenAPI) {
            apiPanel.add(createApiPanel("Oxygen API"));
        }
        if (shardAPI) {
            apiPanel.add(createApiPanel("Shard API"));
        }
        if (AddAPI.api_Endpoint.size() > 0 && AddAPI.api_Key.size() > 0 && AddAPI.api_Name.size() > 0){
            for (int i = 0; i < AddAPI.api_Endpoint.size(); i++) {
                apiPanel.add(createApiPanel(AddAPI.api_Name.get(i)));
            }
            
        }
        
        
        add(apiPanel, BorderLayout.CENTER);

    }
    
    private void addActionEvents() {

        inputField.addKeyListener(this);

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

            new Thread(() -> {
                Map<String, String> env = DotEnv.loadEnv();
                String shard_url = env.get("SHARD_URL").trim();
                String shard_key = env.get("SHARD_API_KEY").trim();

                String zuki_url = env.get("ZUKI_URL").trim();
                String zuki_key = env.get("ZUKI_API_KEY").trim();

                String shuttle_url = env.get("SHUTTLE_URL").trim();
                String shuttle_key = env.get("SHUTTLE_API_KEY").trim();

                String oxygen_url = env.get("OXYGEN_URL").trim();
                String oxygen_key = env.get("OXYGEN_API_KEY").trim();

                List<CompletableFuture<Void>> futures = new ArrayList<>();

                if (shardAPI) {
                    futures.add(sendHttpRequest("Shard API", shard_url, shard_key, selectedModel, prompt));
                }
                if (oxygenAPI) {
                    futures.add(sendHttpRequest("Oxygen API", oxygen_url, oxygen_key, selectedModel, prompt));
                }
                if (shuttleAPI) {
                    futures.add(sendHttpRequest("Shuttle API", shuttle_url, shuttle_key, selectedModel, prompt));
                }
                if (zukiAPI) {
                    futures.add(sendHttpRequest("Zuki API", zuki_url, zuki_key, selectedModel, prompt));
                }

                if (AddAPI.api_Endpoint.size() > 0 && AddAPI.api_Key.size() > 0 && AddAPI.api_Name.size() > 0){
                    for (int i = 0; i < AddAPI.api_Endpoint.size(); i++) {
                        futures.add(sendHttpRequest(AddAPI.api_Name.get(i), AddAPI.api_Endpoint.get(i), AddAPI.api_Key.get(i), selectedModel, prompt));
                    }
                }

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            

        }).start(); // Start the background thread

        Timer timer = new Timer(5000, event -> sendButton.setEnabled(true));
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

    public static Font fontLoader(String fontPath, float fontSize) {
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

    // Preparing the messages to include in the request
    JSONArray messages = new JSONArray();
    // Add previous chat history to the messages array
    List<String> chatHistory = apiChatHistories.getOrDefault(name, new ArrayList<>());
    for (String message : chatHistory) {
        JSONObject msgObj = new JSONObject();
        msgObj.put("role", message.startsWith("User:") ? "user" : "system");
        msgObj.put("content", message.substring(message.indexOf(':') + 2)); // Skipping past "User: " or "API Name Response: "
        messages.put(msgObj);
    }

    // Add the new user message to the history for the current request
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

// Adjusted to store full messages for both user and API response
private void addMessageToChatHistory(String name, String userPrompt, String apiResponse) {
    apiChatHistories.putIfAbsent(name, new ArrayList<>());
    List<String> chatHistory = apiChatHistories.get(name);
    chatHistory.add(userPrompt); // Now storing full string including "User: "
    chatHistory.add(apiResponse); // Storing full response string including "API Name Response: "
}
    

}
