package webserver.request;

public class RequestLine {
    private String method;
    private String url;
    private String httpVersion;

    public RequestLine(String requestLine) {
        String[] requestLineElements = requestLine.split(" ");
        this.method = requestLineElements[0];
        this.url = requestLineElements[1];
        this.httpVersion = requestLineElements[2];
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
}
