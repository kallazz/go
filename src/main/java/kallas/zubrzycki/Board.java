package kallas.zubrzycki;

public class Board implements IBoard {
    private static EPointColor[][] boardPoints;
    private Stone[][] stones;
    private int size;

    public Board(int size) {
        this.size = size;
        stones = new Stone[size + 2][size + 2];
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
        stones[x][y] = new Stone(x, y, playerColor);
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
        resetChains();

        for (int i = 1; i <= size; i++) {
            for (Stone stone : stones[i]) {
                if (stone.getChain() == null) {
                    calculateChains(stone, new ChainOfStones());
                }
            }
        }

        int newStoneX = newStone.getX();
        int newStoneY = newStone.getY();

        if(!newStone.areLibertiesAvailible()){ //Check if there are availible liberties
            //po czterech sąsiadach sprawdzić czy scapteruje ich chainy
            boolean willStoneBeCaptured = false;


            if(stones[newStoneX + 1][newStoneY].getChain().willBeCaptured()){
                willStoneBeCaptured = true;
            }
            if(stones[newStoneX][newStoneY + 1].getChain().willBeCaptured()){
                willStoneBeCaptured = true;
            }
            if(stones[newStoneX - 1][newStoneY].getChain().willBeCaptured()){
                willStoneBeCaptured = true;
            }
            if(stones[newStoneX][newStoneY - 1].getChain().willBeCaptured()){
                willStoneBeCaptured = true;
            }

            if(willStoneBeCaptured == false){
                return false;
            }

        }

        //Check if Ko
        String[] lastMove = GameHistory.getPreviousMove(1).split(" ");
        if(newStoneX == Integer.parseInt(lastMove[1]) && newStoneY == Integer.parseInt(lastMove[2]) ){
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

    private void calculateChains(Stone stone, ChainOfStones chain) {
            chain.addStone(stone);
            stone.setChain(chain);

            // Do same shit on neighbours
            int currentX = stone.getX();
            int currentY = stone.getY();
            EPointColor currentColor = stone.getColor();

            if (stones[currentX + 1][currentY].getColor() == currentColor) {
                calculateChains(stones[currentX + 1][currentY], chain);
            }
            if (stones[currentX][currentY + 1].getColor() == currentColor) {
                calculateChains(stones[currentX][currentY + 1], chain);
            }
            if (stones[currentX - 1][currentY].getColor() == currentColor) {
                calculateChains(stones[currentX - 1][currentY], chain);
            }
            if (stones[currentX][currentY - 1].getColor() == currentColor) {
                calculateChains(stones[currentX][currentY - 1], chain);
            }
        }

    private void resetChains() {
        for (int i = 1; i <= size; i++) {
            for (Stone stone : stones[i]) {
                stone.setChain(null);
            }
        }
    }
}
