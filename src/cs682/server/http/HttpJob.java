package cs682.server.http;

import cs682.server.Router;
import cs682.server.exceptions.BadRequestException;
import cs682.server.exceptions.NoPathMatchedException;
import cs682.server.exceptions.UnsupportedHttpMethodException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * Created by RyanZhu on 10/6/15.
 */
public class HttpJob implements Runnable {
    private Socket socket;

    public HttpJob(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("New Communication Thread Started");
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            HttpRequest httpRequest = HttpParser.parseRequest(in);

            HttpResponse httpResponse = Router.getInstance().run(httpRequest);
            httpResponse.setHttpVersion(httpRequest.getHttpVersion());
            String httpResStr = HttpParser.generateResponse(httpResponse);
            System.out.println(httpResStr);
            out.print(httpResStr);
            out.flush();
        } catch (IOException e) {
            System.err.println("Problem with Communication Server in the running job");
        } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setCode(HttpResponseCode.SERVER_ERROR);
            out.print(HttpParser.generateResponse(httpResponse));
            out.flush();
        } catch (BadRequestException e) {
            e.printStackTrace();
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setCode(HttpResponseCode.BAD_REQUEST);
            out.print(HttpParser.generateResponse(httpResponse));
            out.flush();
        } catch (NoPathMatchedException e) {
            e.printStackTrace();
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setCode(HttpResponseCode.NOT_FOUND);
            out.print(HttpParser.generateResponse(httpResponse));
            out.flush();
        } catch (UnsupportedHttpMethodException e) {
            e.printStackTrace();
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setCode(HttpResponseCode.NOT_ALLOWED);
            out.print(HttpParser.generateResponse(httpResponse));
            out.flush();
        } finally {
            try {
                in.close();
                out.close();
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
