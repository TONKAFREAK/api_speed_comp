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
                    
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                } else {
                    
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                }
            } catch (UnsupportedLookAndFeelException  e) {
                e.printStackTrace();
            }
    
            StartScreen startScreen = new StartScreen();
            startScreen.setVisible(true);
        });
    }
    
}
