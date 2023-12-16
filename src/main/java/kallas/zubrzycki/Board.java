package kallas.zubrzycki;

import java.util.ArrayList;

public class Board implements IBoard {
    private static EPointColor[][] boardPoints;
    private ArrayList<Stone> stones;
    private int size;

    public Board(int size) {
        this.size = size;

        boardPoints = new EPointColor[size + 2][size + 2];
        for (int i = 1; i <= size; i++) {
            for(int j = 1; j <= size; j++){
                boardPoints[i][j] = EPointColor.NONE;
            }
        }
        for (int i = 0; i <= size + 1; i++) {
            boardPoints[i][0] = EPointColor.BORDER;
            boardPoints[i][size + 1] = EPointColor.BORDER;
            boardPoints[0][i] = EPointColor.BORDER;
            boardPoints[size + 1][i] = EPointColor.BORDER;
        }
    }

    @Override
    public void printBoard() {
        
    }

    @Override
    public void updateBoard(int x, int y, EPointColor playerColor) {
        stones.add(new Stone(x,y,playerColor));
        boardPoints[x][y] = playerColor;
    }

    //Singleton OOP Pattern
    public static EPointColor getBoardPoint(int x,int y) {
        return boardPoints[x][y];
    }

    public static EPointColor[][] getBoardPoints() {
        return boardPoints;
    }

    @Override
    public boolean checkMove(int x, int y, EPointColor playerColor) {

        if(!(boardPoints[x][y] == EPointColor.NONE)){ // Check if the spot is empty
            return false;
        }

        Stone newStone = new Stone(x, y, playerColor);

        //TODO: Calculate Chains
        
        
        if(!newStone.areLibertiesAvailible()){ //Check if there are availible liberties
            //po czterech sąsiadach sprawdzić czy scapteruje ich chainy
            boolean willStoneBeCaptured = false;
            for(Stone stone : stones){
                if(stone.getX() == newStone.getX() + 1 && stone.getY() == newStone.getY()){
                    if(stone.getChain().willBeCaptured()){
                        willStoneBeCaptured = true;
                    }
                }
                else if(stone.getX() == newStone.getX() - 1 && stone.getY() == newStone.getY()){
                    if(stone.getChain().willBeCaptured()){
                        willStoneBeCaptured = true;
                    }
                }
                else if(stone.getX() == newStone.getX() && stone.getY() == newStone.getY() + 1){
                    if(stone.getChain().willBeCaptured()){
                        willStoneBeCaptured = true;
                    }
                }
                else if(stone.getX() == newStone.getX() && stone.getY() == newStone.getY() - 1){
                    if(stone.getChain().willBeCaptured()){
                        willStoneBeCaptured = true;
                    }
                }
                if(willStoneBeCaptured == false){
                    return false;
                }
            }

        }

        //Check if Ko
        String[] lastMove = GameHistory.getPreviousMove(1).split(" ");
        if(newStone.getX() == Integer.parseInt(lastMove[1]) && newStone.getY() == Integer.parseInt(lastMove[2]) ){
            return false;
        }


        return true;
    }

    private boolean checkLiberties(int x, int y, EPointColor playerColor) {
        int liberties = 0;
        if(boardPoints[x][y+1] == EPointColor.NONE || boardPoints[x][y+1] == playerColor) {
            liberties++;
        }
        if(boardPoints[x][y-1] == EPointColor.NONE || boardPoints[x][y-1] == playerColor) {
            liberties++;
        }
        if(boardPoints[x+1][y] == EPointColor.NONE || boardPoints[x][y+1] == playerColor) {
            liberties++;
        }
        if(boardPoints[x-1][y] == EPointColor.NONE || boardPoints[x][y+1] == playerColor) {
            liberties++;
        }

        if(liberties > 0){
            return true;
        } else {
            return false;
        }
    }
}
