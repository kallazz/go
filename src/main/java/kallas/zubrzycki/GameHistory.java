package kallas.zubrzycki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// Singleton class
public class GameHistory implements IGameHistory {
    private static volatile GameHistory instance = null;

    private GameHistory() {
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

    public static GameHistory getInstance() {
        if (instance == null) {
            synchronized (GameHistory.class) {
                if (instance == null) {
                    instance = new GameHistory();
                }
            }
        }
        return instance;
    }

    @Override
    public void addToDatabase(String move) {
        try {
            final FileWriter fileWriter = new FileWriter("./database.txt", true);
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(move + '\n');
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public String getPreviousMove(int index) {
        ArrayList<String> moves = new ArrayList<String>();

        try {
            final FileReader fileReader = new FileReader("./database.txt");
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

            // Look for an actual move, not a pass
            while (moves.get(moves.size() - 1 - index).equals("pass")) {
                // TODO: Trzeba się zastanowić jak to powinno działać
                // TODO: szukamy ostatniego ruchu przeciwnika czy ostatniego ruchu w ogóle?
                index += 2;

                if (moves.size() - 1 - index < 0 || moves.size() - 1 - index > moves.size() - 1) {
                    return "";
                }
            };

            return moves.get(moves.size() - 1 - index);
        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
            return "";
        }
    }
}
