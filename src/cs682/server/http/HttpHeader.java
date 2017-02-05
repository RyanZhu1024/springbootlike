package cs682.server.http;

/**
 * Created by RyanZhu on 10/6/15.
 */
public class HttpHeader {
    private final String key;
    private final String value;

    public HttpHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
