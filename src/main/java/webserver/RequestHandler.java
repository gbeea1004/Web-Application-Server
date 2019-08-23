package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestGenerator;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("NewClientConnect!ConnectedIP:{},Port:{}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequestGenerator.createHttpRequest(in);
            HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(out);
            Dispatcher dispatcher = new Dispatcher(httpRequest, httpResponse);
            dispatcher.dispatch();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
