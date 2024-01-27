package kallas.zubrzycki;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board implements IBoard {
    private static volatile Board instance = null;
    private Stone[][] stones;
    private int size;
    private String[] errorMessages = {"", ""}; // error messages for player1 and player2

    private Board() {}

    public static Board getInstance() {
        if (instance == null) {
            synchronized (Board.class) {
                if (instance == null) {
                    instance = new Board();
                }
            }
        }
        return instance;
    }

    @Override
    public void initialize(int size) {
        this.size = size;
        stones = new Stone[size + 2][size + 2];

        for (int i = 0; i <= size + 1; i++) {
            for (int j = 0; j <= size + 1; j++) {
                stones[i][j] = new Stone(i, j, EPointColor.NONE);
            }
        }

        for (int i = 0; i <= size + 1; i++) {
            stones[i][0].setColor(EPointColor.BORDER);
            stones[i][size + 1].setColor(EPointColor.BORDER);
            stones[0][i].setColor(EPointColor.BORDER);
            stones[size + 1][i].setColor(EPointColor.BORDER);
        }
    }

    public String getBoardView(int playerId) {
        // Clear the console
        String output = "\033[H\033[2J";
        //System.out.flush();

        // Print errors
        String errorMessage = errorMessages[playerId];
        if (!errorMessage.equals("")) {
            output += "\n\u001b[31m" + errorMessage + "\u001B[0m\n";
            errorMessages[playerId] = "";
        }

        for (int i = 0; i <= size + 1; i++) {
            for (int j = 0; j <= size + 1; j++) {
                if (stones[j][i].getColor() == EPointColor.NONE) {
                    output += " + ";
                } else if (stones[j][i].getColor() == EPointColor.BLACK) {
                    output += "\u001B[34m ● \u001B[0m";
                } else if (stones[j][i].getColor() == EPointColor.WHITE) {
                    output += "\u001B[33m ● \u001B[0m";
                } else if (stones[j][i].getColor() == EPointColor.BORDER) {
                    output += " X ";
                }
            }
            output += '\n';
        }
        return output;
    }

    @Override
    public void performMove(int x, int y, EPointColor playerColor) {
        stones[x][y] = new Stone(x, y, playerColor);
        calculateChains2(stones);
        checkForCaptures(stones, playerColor);
    }

    @Override
    public boolean checkMove(int x, int y, EPointColor playerColor, int playerId) {
        //Check if x and y are within boundaries
        if(!(x <= size && y <= size && x >= 1 && y >= 1)){
            addErrorMessage("x or y out of bounds", playerId);
            return false;
        }
        // Check if the spot is empty
        if(stones[x][y].getColor() != EPointColor.NONE){
            addErrorMessage("That point was taken by " + stones[x][y].getColor(), playerId);
            return false;
        }
        Stone newStone = new Stone(x, y, playerColor);
        //Check if after placing the stone, there is at least one liberty - Unless there is a capture happening "Suicide rule"
        if(simulateNewStoneAndCountLiberties(newStone) == 0){
            if(simulateNewStoneAndCheckForCaptures(newStone) == false){
                addErrorMessage("There must be at least one liberty, unless you capture", playerId);
                return false;
            }
        }
        //Check for KO rule
        if(simulateNewStoneAndCheckForKORuleViolation() == 0){
            addErrorMessage("KO rule would be violated", playerId);
            return false;
        }
        return true;
    }

    public int simulateNewStoneAndCountLiberties(Stone newStone){

        Stone[][] simulatedStones = new Stone[size + 2][size + 2];
        for(Stone[] stones1 : stones){
            for(Stone stone : stones1){
                simulatedStones[stone.getX()][stone.getY()] = new Stone(stone.getX(), stone.getY(), stone.getColor());
            }
        }

        simulatedStones[newStone.getX()][newStone.getY()] = new Stone(newStone.getX(), newStone.getY(), newStone.getColor());
        resetChains(simulatedStones);
        calculateChains2(simulatedStones);
        return newStone.countLiberties(simulatedStones);
    }

    public boolean simulateNewStoneAndCheckForCaptures(Stone newStone){
        Stone[][] simulatedStones = new Stone[size + 2][size + 2];
        for(Stone[] stones1 : stones){
            for(Stone stone : stones1){
                simulatedStones[stone.getX()][stone.getY()] = new Stone(stone.getX(), stone.getY(), stone.getColor());
            }
        }

        simulatedStones[newStone.getX()][newStone.getY()] = new Stone(newStone.getX(), newStone.getY(), newStone.getColor());
        resetChains(simulatedStones);
        calculateChains2(simulatedStones);
        return checkForCaptures(simulatedStones, newStone.getColor());

    }

    public boolean checkForCaptures(Stone[][] allStones, EPointColor color) {
        boolean anyCaptures = false;

        for(Stone[] stones1 : allStones){
            for(Stone stone : stones1){
                if(stone.getChain() != null && stone.getColor() != color){
                    if(stone.getChain().countLiberties(allStones) == 0){
                        anyCaptures = true;
                        for(Stone capturedStone : stone.getChain().stones) {
                            stones[capturedStone.getX()][capturedStone.getY()].setColor(EPointColor.NONE);
                            stones[capturedStone.getX()][capturedStone.getY()].setChain(null);
                        }
                    }
                }

            }
        }

        return anyCaptures;
    }


    public int simulateNewStoneAndCheckForKORuleViolation(){
        return 1;
    }

    private void calculateChains2(Stone[][] stones) {
        for(Stone[] stoneArray : stones) {
            for(Stone stone : stoneArray) {
                if(stone != null){
                    if( (stone.getColor() == EPointColor.BLACK || stone.getColor() == EPointColor.WHITE) && stone.getChain() == null){
                        assignChain(stone, stones);
                    }
                }

            }
        }
    }

    private void assignChain(Stone stone, Stone[][] allStones) {
        int x = stone.getX();
        int y = stone.getY();
        ChainOfStones newChain;

        if(allStones[x + 1][y].doesExist() && allStones[x + 1][y].getColor() == stone.getColor() && allStones[x + 1][y].getChain() != null){
            newChain = allStones[x+1][y].getChain();
            stone.setChain(newChain);
            newChain.addStone(stone);
        } else if (allStones[x][y + 1].doesExist() && allStones[x][y + 1].getColor() == stone.getColor() && allStones[x][y + 1].getChain() != null) {
            newChain = allStones[x][y + 1].getChain();
            stone.setChain(newChain);
            newChain.addStone(stone);
        } else if (allStones[x - 1][y].doesExist() && allStones[x - 1][y].getColor() == stone.getColor() && allStones[x - 1][y].getChain() != null) {
            newChain = allStones[x-1][y].getChain();
            stone.setChain(newChain);
            newChain.addStone(stone);
        } else if (allStones[x][y - 1].doesExist() && allStones[x][y - 1].getColor() == stone.getColor() && allStones[x][y - 1].getChain() != null) {
            newChain = allStones[x][y-1].getChain();
            stone.setChain(newChain);
            newChain.addStone(stone);
        } else {
            newChain = new ChainOfStones();
            stone.setChain(newChain);
            newChain.addStone(stone);
        }
    }

    private void calculateChains(Stone stone, ChainOfStones chain) {

        if(stone.isVisited()){
            return;
        }

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

    private void resetChains(Stone[][] stones) {
        for (int i = 1; i <= size; i++) {
            for (Stone stone: stones[i]) {
                if (stone != null) {
                    stone.setChain(null);
                }
            }
        }
    }

    @Override
    public void addErrorMessage(String errorMessage, int playerId) {
        this.errorMessages[playerId] = errorMessage;
    }

    public Stone[][] getStones(){
        return stones;
    }


    @Override
    public Stone getBoardPoint(int x, int y) {
        return stones[x][y];
    }

    @Override
    public Stone[][] getBoardPoints() {
        return stones;
    }

    public List<List<Stone>> findTerritories(EPointColor targetColor){
        List<List<Stone>> territories = new ArrayList<>();
        Set<Stone> visited = new HashSet<>();

        for(int x = 1; x <= size; x++) {
            for(int y = 1; y<= size; y++) {
                Stone stone = stones[x][y];
                if(stone.getColor() == EPointColor.NONE && !visited.contains(stone)) {
                    List<Stone> group = new ArrayList<>();
                    if(isTerritorySurrounded(stone, targetColor, visited, group)){
                        territories.add(group);
                    }
                }
            }
        }
        return territories;
    }

    private boolean isTerritorySurrounded(Stone stone, EPointColor targetColor, Set<Stone> visited, List<Stone> group){
        if (!stone.doesExist() || visited.contains(stone)) {
            return true;
        }
        if(stone.getColor() != EPointColor.NONE) {
            return stone.getColor() == targetColor || stone.getColor() == EPointColor.BORDER;
        }
        visited.add(stone);
        group.add(stone);
        boolean surrounded = true;
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for(int[] dir : directions) {
            Stone nextStone = getBoardPoint(stone.getX() + dir[0], stone.getY() + dir[1]);
            if (nextStone != null && !isTerritorySurrounded(nextStone, targetColor, visited, group)) {
                surrounded = false;
            }
        }
        return surrounded;
    }
}
