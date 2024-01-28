package kallas.zubrzycki;

public class App 
{
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Server server = new Server();
        server.start(6666);
    }
}
