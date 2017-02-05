package cs682.controllers;

import cs682.entities.ChannelHistoryEntity;
import cs682.entities.PostMessageResEntity;
import cs682.server.annotations.RequestMapping;
import cs682.server.annotations.RestController;
import cs682.server.http.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by RyanZhu on 05/02/2017.
 */
@RestController("api")
public class ApiController {

    private static final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> data = new ConcurrentHashMap<>();

    @RequestMapping(value = "chat.postMessage", method = HttpMethod.GET)
    public HttpResponse postMessage(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();
        String channel = httpRequest.getParameter("channel");
        String text = httpRequest.getParameter("text");
        if (channel == null || channel.isEmpty() || text == null || text.isEmpty()) {
            httpResponse.setCode(HttpResponseCode.NOT_FOUND);
        } else {
            data.putIfAbsent(channel, new ConcurrentLinkedQueue<>());
            data.get(channel).offer(text);
            httpResponse.setBody(new PostMessageResEntity(true));
        }
        httpResponse.addHeader(HttpHeaderKey.CONTENT_TYPE.getKey(), "application/json");
        return httpResponse;
    }

    @RequestMapping(value = "channels.history", method = HttpMethod.GET)
    public HttpResponse getChannelHistory(HttpRequest httpRequest) {
        String channel = httpRequest.getParameter("channel");
        HttpResponse httpResponse = new HttpResponse();
        if (channel == null || channel.isEmpty()) {
            httpResponse.setCode(HttpResponseCode.NOT_FOUND);
        } else {
            ChannelHistoryEntity history;
            if (!data.containsKey(channel)) {
                history = new ChannelHistoryEntity(false);
            } else {
                history = new ChannelHistoryEntity(true);
                history.setMessages(data.get(channel));
            }
            httpResponse.setBody(history);
        }
        httpResponse.addHeader(HttpHeaderKey.CONTENT_TYPE.getKey(), "application/json");
        return httpResponse;
    }
}
