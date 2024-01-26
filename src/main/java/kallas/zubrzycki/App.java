package kallas.zubrzycki;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args)
    {
        Server x = new Server();
        x.start(6666); 
        /*
        final GameManager gameManager = new GameManager();

        gameManager.initializeGame();
        gameManager.startGameLoop();
        */
    }
}
