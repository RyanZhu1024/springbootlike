package cs682.server.http;

import cs682.server.exceptions.BadRequestException;
import cs682.server.exceptions.UnsupportedHttpMethodException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RyanZhu on 10/6/15.
 */
public class HttpParser {
    public static HttpRequest parseRequest(BufferedReader in) throws IOException, BadRequestException, UnsupportedHttpMethodException {
        HttpRequest httpRequest = new HttpRequest();

        String inputLine = in.readLine();
        System.out.println(inputLine);
        readFirstLine(httpRequest, inputLine);
        //read headers
        while ((inputLine = in.readLine()) != null && !inputLine.trim().isEmpty()) {
            System.out.println(inputLine);
            String[] item = inputLine.split(":");
            httpRequest.addHeader(item[0].trim(), item[1].trim());
        }
        //read body
        String contentLength = httpRequest.getHeader(HttpHeaderKey.Content_LENGTH.getKey());
        Long length = (contentLength == null || contentLength.isEmpty()) ? 0L : Long.valueOf(contentLength);
        if (length > 0) {
            StringBuilder bodyBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null && length > 0) {
                System.out.println("reading body");
                bodyBuilder.append(inputLine);
                length -= inputLine.length();
            }
            httpRequest.setBody(bodyBuilder.toString());
        }
        System.out.println("finish reading body");
        return httpRequest;
    }

    private static void readFirstLine(HttpRequest request, String inputLine) throws BadRequestException, UnsupportedHttpMethodException {
        if (inputLine == null) return;
        if (inputLine.contains("HTTP")) {
            String[] eles = inputLine.split(" ");
            if (eles.length != 3) {
                throw new BadRequestException();
            } else {
                if (!eles[0].equals(HttpMethod.GET.getValue())) {
                    System.out.println(eles[0]);
                    throw new UnsupportedHttpMethodException();
                }
                request.setHttpMethod(HttpMethod.valueOf(eles[0]));
                request.setPath(eles[1]);
                request.setHttpVersion(eles[2]);
                request.setParameters(readQueryStr(request.getPath()));
            }
        }
    }

    private static Map<String, String> readQueryStr(String path) {
        Map<String, String> map = new HashMap<>();
        if (path.contains("?")) {
            String queryStr = path.split("\\?")[1];
            String[] queryArr = queryStr.split(";");
            for (String s : queryArr) {
                String[] item = s.split("=");
                map.put(item[0], item[1]);
            }
        }
        return map;
    }

    public static String generateResponse(HttpResponse responseObj) {
        StringBuilder resBuilder = new StringBuilder();
        resBuilder.append("HTTP/1.1").append(" ");
        resBuilder.append(responseObj.getCode().getCode()).append(" ").append(responseObj.getCode().getValue());
        resBuilder.append("\n");
        for (Map.Entry<String, String> httpHeader : responseObj.getHttpHeaders()) {
            resBuilder.append(httpHeader.getKey()).append(": ").append(httpHeader.getValue()).append("\n");
        }
        resBuilder.append("\n");
        if (responseObj.getBody() != null) {
            resBuilder.append(responseObj.getBody());
        }
        return resBuilder.toString();
    }
}
