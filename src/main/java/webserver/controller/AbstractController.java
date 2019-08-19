package webserver.controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public abstract class AbstractController implements Controller {
    final static String CONTENT_TYPE = "Content-Type";
    final static String CSS_CONTENT_TYPE = "text/css; charset=utf-8";
    final static String HTML_CONTENT_TYPE = "text/html; charset=utf-8";
    final static String JS_CONTENT_TYPE = "text/javascript; charset=utf-8";   //???????????

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getMethod().equals("GET")) {
            doGet(httpRequest, httpResponse);
        }

        if (httpRequest.getMethod().equals("POST")) {
            doPost(httpRequest, httpResponse);
        }
    }

    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
    }

    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
    }
}
