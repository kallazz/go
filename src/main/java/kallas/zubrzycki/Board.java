package kallas.zubrzycki;

import java.util.HashSet;
import java.sql.*;
import java.util.Set;

public class Board implements IBoard {
    private static String colorBlack = "\u001B[34m";
    private static String colorWhite = "\u001B[33m";
    private static String colorRed = "\u001B[31m";
    private static String colorDefault = "\u001B[0m";

    private static volatile Board instance = null;
    private Stone[][] stones;
    private GameManager gm;
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
    public void initialize(int size, GameManager gameManager) {
        this.gm = gameManager;
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

    @Override
    public String getBoardView(int playerId, EPointColor playerColor, EPointColor currentPlayerColor) {
        // Clear the console
        StringBuilder output = new StringBuilder("\033[H\033[2J");
 
        // Print info about this player's color and whose turn it is
        output.append("You are playing as ");
        if (playerColor == EPointColor.BLACK) {
            output.append(colorBlack).append("BLACK").append(colorDefault);
        } else if (playerColor == EPointColor.WHITE) {
            output.append(colorWhite).append("WHITE").append(colorDefault);
        } else if (playerColor == EPointColor.NONE) {
            output.append("NO ONE! You are replaying a game.");
        }
        output.append("\n");

        output.append("Currently, it's ");
        if (currentPlayerColor == EPointColor.BLACK) {
            output.append(colorBlack).append("BLACK").append(colorDefault).append("'s turn");
        } else if (currentPlayerColor == EPointColor.WHITE) {
            output.append(colorWhite).append("WHITE").append(colorDefault).append("'s turn");
        }
        output.append("\n");

        // Print errors
        String errorMessage = errorMessages[playerId];
        if (!errorMessage.equals("")) {
            output.append(colorRed).append(errorMessage).append(colorDefault).append("\n");
            errorMessages[playerId] = "";
        }

        for (int i = 0; i <= size + 1; i++) {
            for (int j = 0; j <= size + 1; j++) {
                if (stones[j][i].getColor() == EPointColor.NONE) {
                    output.append(" + ");
                } else if (stones[j][i].getColor() == EPointColor.BLACK) {
                    output.append(colorBlack).append(" ⬤ ").append(colorDefault);
                } else if (stones[j][i].getColor() == EPointColor.WHITE) {
                    output.append(colorWhite).append(" ⬤ ").append(colorDefault);
                } else if (stones[j][i].getColor() == EPointColor.BORDER) {
                    output.append(" X ");
                }
            }
            output.append('\n');
        }
        return output.toString();
    }

    public String getBoardView(){
        // Clear the console
        StringBuilder output = new StringBuilder("\033[H\033[2J");
        System.out.flush();
        for (int i = 0; i <= size + 1; i++) {
            for (int j = 0; j <= size + 1; j++) {
                if (stones[j][i].getColor() == EPointColor.NONE) {
                    output.append(" + ");
                } else if (stones[j][i].getColor() == EPointColor.BLACK) {
                    output.append(colorBlack).append(" ⬤ ").append(colorDefault);
                } else if (stones[j][i].getColor() == EPointColor.WHITE) {
                    output.append(colorWhite).append(" ⬤ ").append(colorDefault);
                } else if (stones[j][i].getColor() == EPointColor.BORDER) {
                    output.append(" X ");
                }
            }
            output.append('\n');
        }
        return output.toString();
    }

    @Override
    public void performMove(int x, int y, Player player) {
        stones[x][y] = new Stone(x, y, player.getColor());
        calculateChains(stones);
        checkForCaptures(stones, player);
    }

    public void performMove(int x, int y, EPointColor color) {
        stones[x][y] = new Stone(x, y, color);
        calculateChains(stones);
        checkForCaptures(stones, new Player(EPointColor.WHITE, 10));
        checkForCaptures(stones, new Player(EPointColor.BLACK, 10));
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

        //Check for KO rule
        try {
            if(simulateNewStoneAndCheckForKORuleViolation(newStone) == 0){
                addErrorMessage("KO rule would be violated", playerId);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't check for Ko");
        }

        if(simulateNewStoneAndCountLiberties(newStone) == 0){
            if(simulateNewStoneAndCheckForCaptures(newStone) == false){
                addErrorMessage("There must be at least one liberty, unless you capture", playerId);
                return false;
            }
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
        calculateChains(simulatedStones);
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
        calculateChains(simulatedStones);
        return checkForCaptures(simulatedStones, new Player(newStone.getColor(), 9));

    }

    public boolean checkForCaptures(Stone[][] allStones, Player player) {
        EPointColor color = player.getColor();
        boolean anyCaptures = false;

        for(Stone[] stones1 : allStones){
            for(Stone stone : stones1){
                if(stone.getChain() != null && stone.getColor() != color){
                    if(stone.getChain().countLiberties(allStones) == 0){
                        anyCaptures = true;
                        for(Stone capturedStone : stone.getChain().getStones()) {
                            player.setScore(player.getScore() + 1);
                            stones[capturedStone.getX()][capturedStone.getY()].setColor(EPointColor.NONE);
                            stones[capturedStone.getX()][capturedStone.getY()].setChain(null);
                        }
                    }
                }

            }
        }
        return anyCaptures;
    }


    public int simulateNewStoneAndCheckForKORuleViolation(Stone newStone) throws SQLException {
        SQLLiteJDBC db = gm.getDatabase();
        String query = "SELECT move_text FROM moves WHERE game_id="+gm.getGame_id() + " AND move_number=" + (gm.getCurrent_turn() - 1);
        Statement statement = db.getConnection().createStatement();

        try {
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                String previousMove = rs.getString("move_text");
                if (previousMove.equals("pass")) {
                    return 1;
                }
                int previousMoveX = Integer.parseInt(previousMove.split(" ")[1]);
                int previousMoveY = Integer.parseInt(previousMove.split(" ")[2]);

                if (newStone.getX() == previousMoveX && newStone.getY() == previousMoveY) {
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private void calculateChains(Stone[][] stones) {
        for(Stone[] stoneArray : stones) {
            for(Stone stone : stoneArray) {
                if(stone != null){
                    if( (stone.getColor() == EPointColor.BLACK || stone.getColor() == EPointColor.WHITE || stone.getColor() == EPointColor.NONE) && stone.getChain() == null){
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

    public int countPlayerScore(Player player){

        for (Stone[] stoneArray : stones) {
            for (Stone stone : stoneArray) {
                stone.visitedDuringScoreCount = false;
            }
        }

        EPointColor playerColor = player.getColor();
        int score = 0;

        for(Stone[] stoneArray : stones){
            for(Stone stone : stoneArray) {
                if(stone.getColor() == EPointColor.NONE && !stone.visitedDuringScoreCount){
                    HashSet<EPointColor> surroundingColors = new HashSet<>();
                    FloodFillResult result = floodFill(stone, surroundingColors, 0);
                    //System.out.println("Result count: " + result.count);
                    //System.out.println("Result surroundingColors: " + result.surroundingColors);
                    //System.out.println("Player color: " + playerColor);
                    //System.out.println("SurroundingColors size: " + result.surroundingColors.size());
                    //System.out.println("Does it contain player color? " + (result.surroundingColors.contains(playerColor) ? "yes" : "no"));
                    if(result.surroundingColors.size() == 1 && result.surroundingColors.contains(playerColor)){
                        //System.out.println("Adding score! +=" + result.count);
                        score += result.count;
                    }
                }
            }
        }
       // System.out.println("Final score: " + score);
        return score;
    }
    private FloodFillResult floodFill(Stone stone, HashSet<EPointColor> surroundingColors, int currentCount){
        int count = 1;
        stone.visitedDuringScoreCount = true;
        //System.out.println(stone.getX() + ", " + stone.getY() + " currentCount:" + currentCount);

        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for (int[] dir : directions) {
            int newX = stone.getX() + dir[0];
            int newY = stone.getY() + dir[1];

            if (isValidPoint(newX, newY)) {
                Stone adjacentStone = stones[newX][newY];
                if (adjacentStone.getColor() == EPointColor.NONE) {
                    if(adjacentStone.visitedDuringScoreCount == false){
                        FloodFillResult result = floodFill(adjacentStone, surroundingColors, currentCount + count);
                        count += result.count;
                        surroundingColors.addAll(result.surroundingColors); // Merge surrounding colors
                    }
                } else if (adjacentStone.getColor() == EPointColor.WHITE || adjacentStone.getColor() == EPointColor.BLACK) {
                    surroundingColors.add(adjacentStone.getColor());
                }
            }
        }

        return new FloodFillResult(surroundingColors, count);
    }

    private boolean isValidPoint(int x, int y) {
        return x >= 1 && x <= size && y >= 1 && y <= size;
    }

}
