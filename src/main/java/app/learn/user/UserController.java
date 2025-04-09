package app.learn.user;

import app.learn.common.enums.JsonStatusKey;
import app.learn.common.util.JsonUtil;
import app.learn.integrations.jwt.JwtIntegration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class UserController implements HttpHandler {
    private final Map<Pattern, Map<String, Consumer<HttpExchange>>> routes = new LinkedHashMap<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String reqPath = httpExchange.getRequestURI().getPath();
        String reqMethod = httpExchange.getRequestMethod();
        boolean matched = false;
        addRoutes("^/api/auth/register$", "POST", this::createUser); // /api/auth/register
        addRoutes("^/api/auth/login$", "POST", this::userLogin);    // /api/auth/login
        addRoutes("^/api/users/me$", "POST", this::createUser);// /api/users/me
        addRoutes("^/api/users/\\d+$", "POST", this::createUser);// /api/users/{id}
        addRoutes("^/api/users/\\d+$", "POST", this::createUser);// /api/users/{id}
        addRoutes("^/api/auth/logout$", "POST", this::createUser);// /api/auth/logout
        addRoutes("^/api/auth/verify-email$", "POST", this::createUser);// /api/auth/verify-email

        for (Map.Entry<Pattern, Map<String, Consumer<HttpExchange>>> entry : routes.entrySet()) {
            if (entry.getKey().matcher(reqPath).matches()) {
                Map<String, Consumer<HttpExchange>> methodMap = entry.getValue();
                if (methodMap.containsKey(reqMethod)) {
                    methodMap.get(reqMethod).accept(httpExchange);
                    matched = true;
                    break;
                }

            }
        }
        if (!matched) {
            httpExchange.sendResponseHeaders(404, -1);
            httpExchange.close();
        }

    }

    public void addRoutes(String regex, String reqMethod, Consumer<HttpExchange> handler) {
        routes.computeIfAbsent(Pattern.compile(regex), pattern -> new HashMap<>()).put(reqMethod, handler);
    }

    public void createUser(HttpExchange exchange) {
        OutputStream os = exchange.getResponseBody();
        try {
            ObjectMapper mapper = JsonUtil.getObjectMapper();
            User user = mapper.readValue(exchange.getRequestBody(), User.class);
            UserService userService = new UserService();
            if (userService.createUser(user)) {
                String resString = JsonUtil.createJson("User created Successfully", JsonStatusKey.success, user, null);
                byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(201, b.length);
                os.write(b);
                os.close();
            } else {
                String resString = JsonUtil.createJson("User already present", JsonStatusKey.error, null, null);
                byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                try {
                    exchange.sendResponseHeaders(409, b.length);
                    os.write(b);
                    os.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            String resString = JsonUtil.createJson("Something went wrong", JsonStatusKey.error, null, null);
            byte[] b = resString.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            try {
                exchange.sendResponseHeaders(500, b.length);
                os.write(b);
                os.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        }

    }

    public void userLogin(HttpExchange exchange) {
        ObjectMapper mapper = JsonUtil.getObjectMapper();
        UserService userService = new UserService();
        try {
            UserLoginDTO userLoginDTO = mapper.readValue(exchange.getRequestBody(), UserLoginDTO.class);
            if (userService.userLogin(userLoginDTO)) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                map.put("message", "Logged in successfully");
                map.put("status", JsonStatusKey.success.name());
                map.put("redirect", "/app/dashboard");
                String jws = JwtIntegration.createJwt(userLoginDTO.getId(), userLoginDTO.getUserRole());
                String resString = JsonUtil.createJson(map, null, null);
                byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                OutputStream os = exchange.getResponseBody();
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.getResponseHeaders().add("Set-Cookie", "session=" + jws + ";HttpOnly;Secure; Path=/; Max-Age=" + Duration.ofHours(2).getSeconds()
                );
                try {
                    exchange.sendResponseHeaders(200, b.length);
                    os.write(b);
                    os.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                OutputStream os = exchange.getResponseBody();
                String resString = JsonUtil.createJson("Invalid credentials", JsonStatusKey.error, null, null);
                byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                try {
                    exchange.sendResponseHeaders(401, b.length);
                    os.write(b);
                    os.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
