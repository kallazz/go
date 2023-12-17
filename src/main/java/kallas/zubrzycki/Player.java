package kallas.zubrzycki;

import java.util.Scanner;

public class Player implements IPlayer {

    private int capturedStones = 0;
    private Scanner scanner = new Scanner(System.in);
    private EPointColor color;

    Player(EPointColor color) {
        this.color = color;
    }

    @Override
    public int getCapturedStones() {
        return capturedStones;
    }

    @Override
    public String readInput() {
        return scanner.nextLine();
    }

    public EPointColor getColor() {
        return color;
    }
}
