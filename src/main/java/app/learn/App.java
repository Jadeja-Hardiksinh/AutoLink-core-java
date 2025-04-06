package app.learn;

import app.learn.common.controllers.AuthHandler;
import app.learn.task.TaskController;
import app.learn.user.UserController;
import app.learn.user.UserRole;
import app.learn.view.StaticPages;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 12);
        server.createContext("/", new StaticPages());
        server.createContext("/tasks", new AuthHandler(new TaskController(), new UserRole[]{UserRole.ADMIN}));
        server.createContext("/api", new UserController());
        server.createContext("/protected", new AuthHandler(new TaskController(), null));

        server.start();

    }
}
