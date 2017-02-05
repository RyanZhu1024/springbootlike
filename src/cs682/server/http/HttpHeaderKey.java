package cs682.server.http;

/**
 * Created by RyanZhu on 10/6/15.
 */
public enum HttpHeaderKey {
    ACCEPT("Accept"), LOCATION("Location"), USER_AGENT("User-Agent"), CONTENT_TYPE("Content-Type"), HOST("Host"), METHOD("Method"), Content_LENGTH("Content-Length");

    private final String key;

    HttpHeaderKey(String keyname) {
        this.key = keyname;
    }

    public String getKey() {
        return key;
    }
}
