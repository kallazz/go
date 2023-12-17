package kallas.zubrzycki;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GameHistory {
    public GameHistory() {
        final Path path = Paths.get("./database.txt");
        try {
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    public void addToDatabase(String move) {
        try {
            final FileWriter writer = new FileWriter("database" + ".txt", true);
            writer.write(move + '\n');
            writer.close();
        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    public static String getPreviousMove(int index) {
        ArrayList<String> moves = new ArrayList<String>();

        try {
            final FileReader fileReader = new FileReader("database" + ".txt");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                moves.add(line);
            }
            bufferedReader.close();

            // Check if array index isn't out of bounds
            if (moves.size() - 1 - index < 0 || moves.size() - 1 - index > moves.size() - 1) {
                return "";
            }
            return moves.get(moves.size() - 1 - index);
        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
            return "";
        }
    }
}
