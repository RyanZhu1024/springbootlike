package cs682.server.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RyanZhu on 10/8/15.
 */
public final class HttpReqKey {
    private final String path;
    private final HttpMethod httpMethod;
    private static Map<String, HttpReqKey> reqKeysPool = new HashMap<>();


    private HttpReqKey(String path, HttpMethod httpMethod) {
        this.path = path;
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public synchronized static HttpReqKey getHttpReqKeyInstance(String path, HttpMethod httpMethod) {
        String key = httpMethod + "-" + path;
        if (reqKeysPool.containsKey(key)) {
            return reqKeysPool.get(key);
        } else {
            HttpReqKey val = new HttpReqKey(path, httpMethod);
            reqKeysPool.put(key, val);
            return val;
        }
    }

}
