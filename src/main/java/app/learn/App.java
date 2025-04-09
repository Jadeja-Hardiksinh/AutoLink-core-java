package app.learn;

import app.learn.common.controllers.JwtAuthHandler;
import app.learn.integrations.google.gmail.GoogleAuthController;
import app.learn.user.UserController;
import app.learn.user.UserRole;
import app.learn.view.RestrictedPages;
import app.learn.view.StaticPages;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;


public class App {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 12);
        server.createContext("/", new StaticPages());
        server.createContext("/app", new JwtAuthHandler(new RestrictedPages(), new UserRole[]{UserRole.ADMIN, UserRole.USER}));
        server.createContext("/api", new UserController());
        server.createContext("/api/google", new GoogleAuthController());

        server.start();

    }
}
