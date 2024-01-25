package kallas.zubrzycki;

import java.util.Scanner;

public class Player implements IPlayer {

    private int capturedStones = 0;
    private Scanner scanner = new Scanner(System.in);
    private EPointColor color;
    private int id;

    Player(EPointColor color, int id) {
        this.color = color;
        this.id = id;
    }

    @Override
    public String readInput() {
        return scanner.nextLine();
    }

    @Override
    public void addCapturedStones(int newlyCaptured) {
        capturedStones = capturedStones + newlyCaptured;
    }

    @Override
    public int getCapturedStones() {
        return capturedStones;
    }

    @Override
    public EPointColor getColor() {
        return color;
    }

    @Override
    public int getId() {
        return id;
    }
}
