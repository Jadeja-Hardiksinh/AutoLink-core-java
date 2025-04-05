package app.learn;

import app.learn.controllers.apis.TaskController;
import app.learn.controllers.pages.HomePage;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 12);
        server.createContext("/", new HomePage());
        server.createContext("/tasks", new TaskController());
        server.start();

    }
}
