package app.learn.integrations.google.gmail;

import app.learn.common.enums.JsonStatusKey;
import app.learn.common.util.JsonUtil;
import app.learn.common.util.TypeConversion;
import app.learn.integrations.google.util.GoogleAuthUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoogleAuthController implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String reqURL = httpExchange.getRequestURI().getPath();
        if (reqURL.equals("/api/google/oauthcallback")) {
            String code = TypeConversion.getMapFromQueryParam(httpExchange.getRequestURI().getQuery()).get("code");
            GoogleTokenResponse tokenResponse = GoogleAuthUtil.getAuthToken(code);

            String html = """
                <!DOCTYPE html>
                <html>
                  <body>
                    <script>
                      // Notify the main window that OAuth succeeded
                      window.opener.postMessage("oauth-success", "http://localhost:3000");
                      window.close();
                    </script>
                    <p>You can close this window now.</p>
                  </body>
                </html>
                """;
            httpExchange.getResponseHeaders().set("Content-Type", "text/html");
            httpExchange.sendResponseHeaders(200, html.getBytes().length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(html.getBytes());
            }
        } else if (reqURL.equals("/api/google/auth")) {
            try (OutputStream os = httpExchange.getResponseBody()) {
                List<String> scopes = new ArrayList<>();
                scopes.add("https://www.googleapis.com/auth/gmail.readonly");
                String authReqURL = GoogleAuthUtil.getAuthorizationURL(scopes);

                HashMap<String, String> jsonKeys = new HashMap<>();
                jsonKeys.put("status", JsonStatusKey.success.name());
                jsonKeys.put("redirect", authReqURL);
                String responseJson = JsonUtil.createJson(jsonKeys, null, null);
                byte[] b = responseJson.getBytes(StandardCharsets.UTF_8);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                httpExchange.sendResponseHeaders(200, b.length);

                os.write(b);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
