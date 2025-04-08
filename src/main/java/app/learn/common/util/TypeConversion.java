package app.learn.common.util;

import java.util.HashMap;
import java.util.Map;

public class TypeConversion {
    public static Map<String, String> getMapFromQueryParam(String queryParam) {
        HashMap<String, String> map = new HashMap<>();
        for (String part : queryParam.split("&")) {
            String[] keyValue = part.split("=");
            map.put(keyValue[0], keyValue[1]);

        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "  " + entry.getValue());
        }
        return map;
    }
}
