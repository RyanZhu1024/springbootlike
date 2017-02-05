package cs682.controllers;

import cs682.server.annotations.RequestMapping;
import cs682.server.annotations.RestController;
import cs682.server.http.*;

/**
 * Created by RyanZhu on 05/02/2017.
 */
@RestController("/")
public class ApiController {
    @RequestMapping(method = HttpMethod.GET)
    public HttpResponse get(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.addHeader(HttpHeaderKey.CONTENT_TYPE.getKey(), "application/json");
        return httpResponse;
    }
}
