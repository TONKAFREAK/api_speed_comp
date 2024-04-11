import java.util.Map;

import javax.swing.*;

public class App extends JFrame{

    public static void openMainScreen(Map<String, Boolean> apiSelections, String selectedModel) {
        SwingUtilities.invokeLater(() -> {
            MainScreen mainScreen = new MainScreen(apiSelections, selectedModel);
            mainScreen.setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String osName = System.getProperty("os.name").toLowerCase();
                if (osName.contains("win")) {
                    // Set Synthetica Look and Feel on Windows
                    UIManager.setLookAndFeel("de.javasoft.synthetica.dark.SyntheticaDarkLookAndFeel");
                } else {
                    // Use FlatLaf Dark Look and Feel on other OSes
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                }
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
    
            StartScreen startScreen = new StartScreen();
            startScreen.setVisible(true);
        });
    }
    
}
