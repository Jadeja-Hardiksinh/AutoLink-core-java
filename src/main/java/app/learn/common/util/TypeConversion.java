package app.learn.common.util;

import java.util.HashMap;
import java.util.List;
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

    public static String getCookieValFromCookieString(String cookieName, List<String> cookieList) {
        HashMap<String, String> cookieMap = new HashMap<>();
        if (cookieList != null && !cookieList.isEmpty()) {
            String cookiesString = cookieList.getFirst();
            String[] cookieArr = cookiesString.split(";");
            for (String cookie : cookieArr) {
                String[] cookiePair = cookie.split("=");
                cookieMap.put(cookiePair[0].trim(), cookiePair[1]);
            }
        }
       
        return cookieMap.get(cookieName);
    }

}
