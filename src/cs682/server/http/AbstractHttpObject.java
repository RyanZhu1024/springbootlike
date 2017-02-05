package cs682.server.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by RyanZhu on 10/6/15.
 */
public abstract class AbstractHttpObject {
    private Map<String, String> header;
    private Object body;
    private String httpVersion;

    public String getHeader(String key) {
        return header.get(key);
    }

    public void addHeader(String key, String val) {
        header.put(key, val);
    }

    public AbstractHttpObject() {
        this.header = new HashMap<>();
    }

    public Set<Map.Entry<String, String>> getHttpHeaders() {
        return header.entrySet();
    }

    public void addHeaders(Set<Map.Entry<String, String>> headers) {
        for (Map.Entry<String, String> entry : headers) {
            addHeader(entry.getKey(), entry.getValue());
        }
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
