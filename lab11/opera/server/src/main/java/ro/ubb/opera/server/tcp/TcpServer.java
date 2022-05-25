package ro.ubb.opera.server.tcp;

import ro.ubb.opera.common.tcp.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;


public class TcpServer {
    private ExecutorService executorService;

    private int port;

    private Map<String, UnaryOperator<Message>> methodHandlers;

    public TcpServer(ExecutorService executorService, int port) {
        this.executorService = executorService;
        this.port = port;
        this.methodHandlers = new HashMap<>();
    }

    public void addHandler(String methodName, UnaryOperator<Message> handler) {
        this.methodHandlers.put(methodName, handler);
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("server started; waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected");
                this.executorService.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream is = socket.getInputStream();
                 OutputStream os = socket.getOutputStream()) {
                Message request = new Message();
                request.readFrom(is);
                System.out.println("received request: " + request);

                Message response = methodHandlers.get(request.getHeader()).apply(request);
                System.out.println("computed response: " + response);

                response.writeTo(os);
                System.out.println("response sent to client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
