package app.learn;

import app.learn.task.TaskController;
import app.learn.user.UserController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 12);
        server.createContext("/tasks", new TaskController());
        server.createContext("/api", new UserController());
        server.start();

    }
}
