import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public EchoClient(String host, int port) throws IOException {
        openConnection(host, port);
        startServerListener();
        startConsoleListener();
    }

    public void openConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        System.out.println("Соединение с сервером установлено...");
        System.out.println("------------------------------");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    private void startServerListener() {
        new Thread(() -> {
            try {
                while (socket.isConnected()) {
                    String msg = in.readUTF();
                    // Сейчас клиент тоже может разорвать соединение, получив команду "/end"
                    // Но возможно это архитерктурно неправильное решение, т.к. при необходимости разрывать соединение должен сервер. ИМХО)
                    if (msg.equalsIgnoreCase("/end")) {
                        break;
                    }
                    System.out.println("Сообщение от сервера: " + msg);
                }
            } catch (Exception e) {
            } finally {
                try {
                    System.err.println("Соединение с сервером разорвано...");
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startConsoleListener() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                out.writeUTF(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
