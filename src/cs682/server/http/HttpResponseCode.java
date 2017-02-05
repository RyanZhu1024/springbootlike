package cs682.server.http;

/**
 * Created by RyanZhu on 10/6/15.
 */
public enum HttpResponseCode {
    OK(200, "OK"), NOT_FOUND(404, "NOT FOUND"), NOT_ALLOWED(405, "NOT ALLOWED"), BAD_REQUEST(400, "BAD REQUEST"), SERVER_ERROR(500, "SERVER ERROR");

    private final int code;
    private final String value;

    HttpResponseCode(int i, String s) {
        this.code = i;
        this.value = s;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
