import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        try {
            new EchoClient("localhost", 3344);
        } catch (IOException e) {
            System.out.println("Не удалось соединиться с сервером!");
            e.printStackTrace();
        }
    }
}
