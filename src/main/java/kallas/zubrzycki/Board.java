package kallas.zubrzycki;

public class Board implements IBoard {
    private static EPointColor[][] boardPoints;
    private Stone[][] stones;
    private int size;

    public Board(int size) {
        this.size = size;
        stones = new Stone[size + 2][size + 2];
        boardPoints = new EPointColor[size + 2][size + 2];

        for (int i = 0; i <= size + 1; i++) {
            for (int j = 0; j <= size + 1; j++) {
                stones[i][j] = new Stone(0, 0, EPointColor.NONE);
            }
        }

        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
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

    // Static methods
    public static EPointColor getBoardPoint(int x, int y) {
        return boardPoints[x][y];
    }

    public static EPointColor[][] getBoardPoints() {
        return boardPoints;
    }

    // Regular methods
    @Override
    public void printBoard() {
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if (boardPoints[i][j] == EPointColor.NONE) {
                    System.out.print('+');
                } else if (boardPoints[i][j] == EPointColor.BLACK) {
                    System.out.print("\u001B[34m●\u001B[0m");
                } else if (boardPoints[i][j] == EPointColor.WHITE) {
                    System.out.print("\u001B[33m●\u001B[0m");
                }
            }
            System.out.print('\n');
        }
    }

    @Override
    public void updateBoard(int x, int y, EPointColor playerColor) {
        stones[x][y] = new Stone(x, y, playerColor);
        boardPoints[x][y] = playerColor;
    }

    @Override
    public boolean checkMove(int x, int y, EPointColor playerColor) {

        if (!(boardPoints[x][y] == EPointColor.NONE)) { // Check if the spot is empty
            System.out.println("Point: " + boardPoints[x][y].toString());
            return false;
        }


        final Stone newStone = new Stone(x, y, playerColor);

        resetChains();
        for (int i = 1; i <= size; i++) {
            for (Stone stone: stones[i]) {
                if (stone.doesExist() && stone.getChain() == null) {
                    calculateChains(stone, new ChainOfStones());
                }
            }
        }

        int newStoneX = newStone.getX();
        int newStoneY = newStone.getY();

        if (!newStone.areLibertiesAvailible()) { //Check if there are availible liberties
            //po czterech sąsiadach sprawdzić czy scapteruje ich chainy
            boolean willStoneBeCaptured = false;


            if (stones[newStoneX + 1][newStoneY].getChain().willBeCaptured()) {
                willStoneBeCaptured = true;
            }
            if (stones[newStoneX][newStoneY + 1].getChain().willBeCaptured()) {
                willStoneBeCaptured = true;
            }
            if (stones[newStoneX - 1][newStoneY].getChain().willBeCaptured()) {
                willStoneBeCaptured = true;
            }
            if (stones[newStoneX][newStoneY - 1].getChain().willBeCaptured()) {
                willStoneBeCaptured = true;
            }

            if (willStoneBeCaptured == false) {
                return false;
            }

        }

        // Check if 
        String lastMove = GameHistory.getPreviousMove(1);
        if (lastMove.equals("")) {
            return true;
        }

        String[] move = GameHistory.getPreviousMove(1).split(" ");
        if (newStoneX == Integer.parseInt(move[1]) && newStoneY == Integer.parseInt(move[2])) {
            return false;
        }


        return true;
    }

    private void calculateChains(Stone stone, ChainOfStones chain) {
        chain.addStone(stone);
        stone.setChain(chain);

        final int currentX = stone.getX();
        final int currentY = stone.getY();
        final EPointColor currentColor = stone.getColor();

        if (stones[currentX + 1][currentY].doesExist() && stones[currentX + 1][currentY].getColor() == currentColor) {
            calculateChains(stones[currentX + 1][currentY], chain);
        }
        if (stones[currentX][currentY + 1].doesExist() && stones[currentX][currentY + 1].getColor() == currentColor) {
            calculateChains(stones[currentX][currentY + 1], chain);
        }
        if (stones[currentX - 1][currentY].doesExist() && stones[currentX - 1][currentY].getColor() == currentColor) {
            calculateChains(stones[currentX - 1][currentY], chain);
        }
        if (stones[currentX][currentY - 1].doesExist() && stones[currentX][currentY - 1].getColor() == currentColor) {
            calculateChains(stones[currentX][currentY - 1], chain);
        }
    }

    private void resetChains() {
        for (int i = 1; i <= size; i++) {
            for (Stone stone: stones[i]) {
                if (stone.doesExist()) {
                    stone.setChain(null);
                }
            }
        }
    }
}
