package cs682.server.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RyanZhu on 10/6/15.
 */
public class HttpRequest extends AbstractHttpObject {
    private Map<String, String> parameters;
    private String path;
    private HttpMethod httpMethod;

    public HttpRequest() {
        super();
        this.parameters = new HashMap<>();
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }


    public String getParameter(String key) {
        return this.parameters.get(key);
    }


}
