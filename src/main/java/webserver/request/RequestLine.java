package webserver.request;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestLine {
    private String method;
    private String url;
    private String httpVersion;
    private Map<String, String> params;


    public RequestLine(String requestLine) {
        String[] requestLineElements = requestLine.split(" ");
        this.method = requestLineElements[0];
        this.url = requestLineElements[1];
        this.httpVersion = requestLineElements[2];
        initParams();
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    private void initParams() {
        if (method.equals("GET") && url.contains("?")) {
            params =  HttpRequestUtils.parseQueryString(url.substring(url.indexOf("?") + 1));
        }
    }

    public String getParam(String key) {
        return params.get(key);
    }
}
