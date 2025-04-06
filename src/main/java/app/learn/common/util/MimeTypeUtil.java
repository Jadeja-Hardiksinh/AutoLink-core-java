package app.learn.common.util;

public class MimeTypeUtil {
    public static String getMimeType(String file) {
        if (file.endsWith(".html")) return "text/html";
        if (file.endsWith(".css")) return "text/css";
        if (file.endsWith(".js")) return "application/javascript";
        if (file.endsWith(".json")) return "application/json";
        if (file.endsWith(".png")) return "image/png";
        if (file.endsWith(".jpg") || file.endsWith(".jpeg")) return "image/jpeg";
        if (file.endsWith(".gif")) return "image/gif";
        if (file.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream"; // Default for unknown types
    }
}
