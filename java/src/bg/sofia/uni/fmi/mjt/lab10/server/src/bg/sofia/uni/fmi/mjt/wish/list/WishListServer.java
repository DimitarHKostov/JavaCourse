package bg.sofia.uni.fmi.mjt.wish.list;

import bg.sofia.uni.fmi.mjt.wish.list.handler.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class WishListServer {
    private final int port;
    private static final int MAX_EXECUTOR_THREADS = 20;
    private final ConcurrentHashMap<String, List<String>> wishList = new ConcurrentHashMap<>();
    private final AtomicBoolean isStopped = new AtomicBoolean(false);
    ExecutorService readExecutor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);
    private final Thread serverThread;

    public WishListServer(int port) {
        this.port = port;
        this.serverThread = new Thread(this::serverMethod);
    }

    public synchronized void start() {
        if (!this.isStopped.get()) {
            serverThread.start();
        }
    }

    private void serverMethod() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            Socket clientSocket;

            while (!this.isStopped.get()) {
                clientSocket = serverSocket.accept();

                RequestHandler clientHandler = new RequestHandler(clientSocket, wishList);
                readExecutor.execute(clientHandler);
            }

            readExecutor.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.isStopped.set(true);
        serverThread.interrupt();
    }

    public static void main(String[] args) {
        WishListServer server = new WishListServer(4444);
        server.start();
        server.stop();
    }
}
