import javax.swing.*;

public class App extends JFrame{

    public static enum STATE {
		
		Start,
		Main
		
	}

    public static STATE State = STATE.Start;

    public static void transitionToState(STATE newState) {

        State = newState; 

        SwingUtilities.invokeLater(() -> {
            switch (State) {
                case Start:
                    StartScreen startScreen = new StartScreen();
                    startScreen.setVisible(true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + State);
            }
        });
    }

    public static void openMainScreen(boolean shardAPI, boolean oxygenAPI, boolean shuttleAPI, boolean zukiAPI, String selectedModel) {
        SwingUtilities.invokeLater(() -> {
            MainScreen mainScreen = new MainScreen(shardAPI, oxygenAPI, shuttleAPI, zukiAPI, selectedModel);
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

            transitionToState(State);
            

        

        });

       
    }

}
