import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    private final ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public EchoServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("Ждем подключения клиента...");
        socket = serverSocket.accept();
        System.out.println("Соединение с клиентом установлено...");
        System.out.println("------------------------------");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        new Thread(this::startClientListener).start();
        startConsoleListener();
    }

    private void startClientListener() {
        try {
            String msg;
            while (true) {
                msg = in.readUTF();
                if (msg.equalsIgnoreCase("/end")) {
                    break;
                }
                System.out.println("Сообщение от клиента: " + msg);
                out.writeUTF(msg);
            }
        } catch (Exception e) {
        } finally {
            try {
                System.err.println("Соединение с клиентом разорвано...");
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
