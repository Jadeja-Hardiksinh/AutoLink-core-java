package app.learn.controllers.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HomePage implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        String msg = "Hello";
        httpExchange.getResponseHeaders().add("Content-Type","text/plain");
        httpExchange.sendResponseHeaders(200, msg.getBytes(StandardCharsets.UTF_8).length);
        os.write(msg.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
