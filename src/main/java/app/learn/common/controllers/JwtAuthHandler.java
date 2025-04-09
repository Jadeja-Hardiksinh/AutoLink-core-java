package app.learn.common.controllers;

import app.learn.common.util.TypeConversion;
import app.learn.integrations.jwt.JwtIntegration;
import app.learn.user.UserRole;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Arrays;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

public class JwtAuthHandler implements HttpHandler {
    private final HttpHandler handler;
    private final UserRole[] allowedRoles;

    public JwtAuthHandler(HttpHandler handler, UserRole[] allowedRoles) {
        this.handler = handler;
        this.allowedRoles = allowedRoles;
    }

    public void serve403Page(HttpExchange exchange) throws IOException {
        File file = new File("src/main/resources/html/403.html");
        if (file.exists()) {
            try (OutputStream os = exchange.getResponseBody()) {
                byte[] b = Files.readAllBytes(file.toPath());
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(403, b.length);
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
        List<String> authHeaderList = httpExchange.getRequestHeaders().get("Cookie");
        String jws = TypeConversion.getCookieValFromCookieString("session", authHeaderList);
        if (jws == null) {
            httpExchange.getResponseHeaders().add("Location", "/login");
            httpExchange.sendResponseHeaders(302, -1);
            httpExchange.close();
            return;
        }

        try {
            Jws<Claims> claims = JwtIntegration.verifyJwt(jws);
            if (claims != null) {
                if (Arrays.asList(allowedRoles).contains(UserRole.valueOf(claims.getPayload().get("role").toString()))) {
                    handler.handle(httpExchange);
                } else {
                    serve403Page(httpExchange);
                }

            } else {


                httpExchange.getResponseHeaders().add("Set-Cookie", "session=; Max-Age=0; Path=/; HttpOnly");
                httpExchange.sendResponseHeaders(401, -1);
                httpExchange.close();
            }
        } catch (JwtException | IOException e) {
            e.printStackTrace();
            httpExchange.getResponseHeaders().add("Set-Cookie", "session=; Max-Age=0; Path=/; HttpOnly");
            httpExchange.sendResponseHeaders(401, -1);
            httpExchange.close();
        }


    }
}
