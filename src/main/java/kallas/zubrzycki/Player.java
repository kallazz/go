package kallas.zubrzycki;

import java.util.Scanner;

public class Player implements IPlayer {

    private int capturedStones = 0;
    private Scanner scanner;
    private EPointColor color;

    Player(EPointColor color){
        this.color = color;
        scanner = new Scanner(System.in);
    }

    @Override
    public void attemptMove(int x, int y){

    }

    @Override
    public void pass(){

    }
    
    @Override
    public int getCapturedStones(){
        return capturedStones;
    }

    @Override
    public String readInput() {
        return scanner.nextLine();
    }
}
