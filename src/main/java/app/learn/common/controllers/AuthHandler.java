package app.learn.common.controllers;

import app.learn.integrations.jwt.JwtIntegration;
import app.learn.user.UserRole;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Arrays;

import java.io.IOException;
import java.util.List;

public class AuthHandler implements HttpHandler {
    private final HttpHandler handler;
    private final UserRole[] allowedRoles;

    public AuthHandler(HttpHandler handler, UserRole[] allowedRoles) {
        this.handler = handler;
        this.allowedRoles = allowedRoles;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        List<String> authHeaderList = httpExchange.getRequestHeaders().get("Authorization");
        if (authHeaderList != null && !authHeaderList.isEmpty()) {
            String jws = authHeaderList.getFirst().substring(7);
            try {
                Jws<Claims> claims = JwtIntegration.verifyJwt(jws);
                if (claims != null && Arrays.asList(allowedRoles).contains(UserRole.valueOf(claims.getPayload().get("role").toString()))) {
                    handler.handle(httpExchange);

                } else {
                    httpExchange.sendResponseHeaders(401, -1);
                    httpExchange.close();
                }
            } catch (JwtException | IOException e) {
                e.printStackTrace();
                httpExchange.sendResponseHeaders(401, -1);
                httpExchange.close();
            }


        } else {
            httpExchange.sendResponseHeaders(401, -1);
            httpExchange.close();
        }


    }
}
