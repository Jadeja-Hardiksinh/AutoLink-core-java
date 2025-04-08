package app.learn.integrations.google.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.List;

public class GoogleAuthUtil {
    private static final String cId = null;
    private static final String csKey = null;


    public static String getAuthorizationURL(List<String> scopes) {
        return new GoogleAuthorizationCodeRequestUrl(cId, "http://localhost:8080/api/google/oauthcallback", scopes).build();
    }

    public static GoogleTokenResponse setAuthTokenRequest(String code) {
        GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), GsonFactory.getDefaultInstance(),
            cId, csKey, code, "http://localhost:8080/api/google/oauthcallback");
        try {
            return tokenRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getAuthToken(String code) {
        GoogleTokenResponse tokenResponse = setAuthTokenRequest(code);
        if (tokenResponse != null) {
            System.out.println(tokenResponse.getAccessToken());
        }
    }

}
