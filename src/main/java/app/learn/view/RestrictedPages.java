package app.learn.view;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class RestrictedPages implements HttpHandler {
    public void serve404Page(HttpExchange exchange) throws IOException {
        File file = new File("src/main/resources/html/404.html");
        if (file.exists()) {
            try (OutputStream os = exchange.getResponseBody()) {
                byte[] b = Files.readAllBytes(file.toPath());
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(404, b.length);
                os.write(b);
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
            }
        } else {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String path = httpExchange.getRequestURI().getPath();
        if (path.equals("/app/dashboard")) {
            File file = new File("src/main/resources/html/dashboard.html");
            if (file.exists()) {
                try (OutputStream os = httpExchange.getResponseBody()) {
                    byte[] b = Files.readAllBytes(file.toPath());
                    httpExchange.getResponseHeaders().add("Content-Type", "text/html");
                    httpExchange.sendResponseHeaders(200, b.length);
                    os.write(b);
                } catch (IOException e) {
                    e.printStackTrace();
                    serve404Page(httpExchange);

                }
            } else {
                serve404Page(httpExchange);
            }

        } else {
            serve404Page(httpExchange);
        }
    }
}
