package kallas.zubrzycki;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GameHistory implements IGameHistory{

    @Override
    public void addToDatabase(String move){
        try {
            FileWriter writer = new FileWriter("database.txt");
            writer.write(move + '\n');
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    @Override
    public String getPreviousMove(int index){
        ArrayList<String> moves = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader("database.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                moves.add(line);
            }

            bufferedReader.close();

            return moves.get(moves.size() - 1 - index);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            return "";
        }
    }
}
