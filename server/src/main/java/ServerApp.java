import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {
        try {
            EchoServer echoServer = new EchoServer(3344);
            echoServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
