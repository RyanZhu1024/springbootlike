package cs682.server.http;

/**
 * Created by RyanZhu on 10/8/15.
 */
public enum HttpMethod {
    GET("GET"), POST("POST"), DELETE("DELETE"), PUT("PUT");

    private final String value;

    HttpMethod(String str) {
        this.value = str;
    }

    public String getValue() {
        return value;
    }
}
