package cs682.server.http;

/**
 * Created by RyanZhu on 10/6/15.
 */
public class HttpResponse extends AbstractHttpObject {
    private HttpResponseCode code;

    public HttpResponse() {
        this.code = HttpResponseCode.OK;
    }

    public HttpResponseCode getCode() {
        return code;
    }

    public void setCode(HttpResponseCode code) {
        this.code = code;
    }
}
