package kallas.zubrzycki;

public class ClientTesting {
    public static void main(String[] args)
    {
        Client client = new Client();
        client.startConnection("127.0.0.1", 6666);
        client.loop();
    }
}
