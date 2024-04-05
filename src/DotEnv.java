import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DotEnv {
    public static Map<String, String> loadEnv() {
        Map<String, String> env = new HashMap<>();
        try {
            // Assume the .env file is in the current working directory
            Path envPath = Path.of(".env");
            List<String> lines = Files.readAllLines(envPath);

            for (String line : lines) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    // Ignore empty lines and comments in the .env file
                    continue;
                }

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim().replaceAll("^\"|\"$", ""); // Remove surrounding quotes
                    env.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return env;
    }
}
