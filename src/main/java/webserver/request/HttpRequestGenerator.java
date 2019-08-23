package webserver.request;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpRequestGenerator {
    public static HttpRequest createHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        RequestLine requestLine = HttpRequestUtils.createRequestLine(br);
        RequestHeader requestHeader = HttpRequestUtils.createRequestHeader(br);
        RequestBody requestBody = HttpRequestUtils.createRequestBody(br, requestHeader.getContentLength());

        return new HttpRequest(requestLine, requestHeader, requestBody);
    }
}
