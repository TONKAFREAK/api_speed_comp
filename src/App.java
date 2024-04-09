import java.util.Map;

import javax.swing.*;

public class App extends JFrame{

    public static enum STATE {
		
		Start,
		Main
		
	}

    public static void openMainScreen(Map<String, Boolean> apiSelections, String selectedModel) {
        SwingUtilities.invokeLater(() -> {
            MainScreen mainScreen = new MainScreen(apiSelections, selectedModel);
            mainScreen.setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.put("MenuItem.iconTextGap", 0);

                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            StartScreen startScreen = new StartScreen();
            startScreen.setVisible(true);
            

        

        });

       
    }

}
