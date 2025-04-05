package app.learn.controllers.apis;

import app.learn.dto.GenericDTO;
import app.learn.dto.TaskDTO;
import app.learn.enums.JsonStatusKey;
import app.learn.models.Task;
import app.learn.services.TaskService;
import app.learn.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class TaskController implements HttpHandler {
    private final Map<Pattern, Map<String, Consumer<HttpExchange>>> routes = new LinkedHashMap<>();
    TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String reqPath = httpExchange.getRequestURI().getPath();
        String reqMethod = httpExchange.getRequestMethod();
        addRoutes("^/tasks$", "GET", this::fetchTasks);
        addRoutes("^/tasks$", "POST", this::createTask);
        addRoutes("^/tasks/\\d+$", "PUT", this::updateTask);
        addRoutes("^/tasks/\\d+$", "DELETE", this::deleteTask);

        for (Map.Entry<Pattern, Map<String, Consumer<HttpExchange>>> entry : routes.entrySet()) {
            if (entry.getKey().matcher(reqPath).matches()) {
                Map<String, Consumer<HttpExchange>> methodMap = entry.getValue();
                if (methodMap.containsKey(reqMethod)) {
                    methodMap.get(reqMethod).accept(httpExchange);
                    break;
                }
            }
        }
    }

    public void addRoutes(String regex, String reqMethod, Consumer<HttpExchange> handler) {
        routes.computeIfAbsent(Pattern.compile(regex), pattern -> new HashMap<>()).put(reqMethod, handler);
    }

    public void createTask(HttpExchange exchange) {
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        try (OutputStream os = exchange.getResponseBody()) {
            Task task = objectMapper.readValue(exchange.getRequestBody(), Task.class);
            taskService.createTask(task);
            TaskDTO resObj = new TaskDTO(JsonStatusKey.success, "User created Successfully", task);
            String resString = objectMapper.writeValueAsString(resObj);
            byte[] b = resString.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(201, b.length);
            os.write(b);
           
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void updateTask(HttpExchange exchange) {
        ObjectMapper mapper = JsonUtil.getObjectMapper();
        String[] parts = exchange.getRequestURI().getPath().split("/");
        Long id = Long.parseLong(parts[parts.length - 1]);
        try {
            Task task = mapper.readValue(exchange.getRequestBody(), Task.class);
            task.setId(id);
            if (taskService.updateTask(task)) {
                try (OutputStream os = exchange.getResponseBody()) {
                    String resString = mapper.writeValueAsString(new GenericDTO(JsonStatusKey.success, "Task updated successfully"));
                    byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, b.length);
                    os.write(b);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {

                try (OutputStream os = exchange.getResponseBody()) {
                    String resString = mapper.writeValueAsString(new GenericDTO(JsonStatusKey.error, "Something went wrong"));
                    byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, b.length);
                    os.write(b);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void fetchTasks(HttpExchange exchange) {
        List<Task> tasks = taskService.fetchAllTasks();
        ObjectMapper mapper = JsonUtil.getObjectMapper();
        if (!tasks.isEmpty()) {

            try (OutputStream os = exchange.getResponseBody()) {

                String resString = mapper.writeValueAsString(tasks);
                byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, b.length);
                os.write(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            try (OutputStream os = exchange.getResponseBody()) {
                String resString = mapper.writeValueAsString(new GenericDTO(JsonStatusKey.success, "No record Found"));
                byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, b.length);
                os.write(b);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }


    }

    public void deleteTask(HttpExchange exchange) {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        Long id = Long.parseLong(parts[parts.length - 1]);
        ObjectMapper mapper = JsonUtil.getObjectMapper();

        if (taskService.deleteTask(id)) {
            try (OutputStream os = exchange.getResponseBody()) {
                String resString = mapper.writeValueAsString(new GenericDTO(JsonStatusKey.success, "Task Deleted"));
                byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, b.length);
                os.write(b);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (OutputStream os = exchange.getResponseBody()) {
                String resString = mapper.writeValueAsString(new GenericDTO(JsonStatusKey.success, "No record Found"));
                byte[] b = resString.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, b.length);
                os.write(b);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
