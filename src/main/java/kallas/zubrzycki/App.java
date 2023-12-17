package kallas.zubrzycki;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args)
    {
        final GameManager gameManager = new GameManager();

        gameManager.initializeGame();
        gameManager.startGameLoop();
    }
}
