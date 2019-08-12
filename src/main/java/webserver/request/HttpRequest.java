package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private BufferedReader br;
    private Map<String, String> headers = new HashMap<>();
    private RequestLine requestLine;

    public HttpRequest(InputStream in) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        requestLine = new RequestLine(br.readLine());
        initHeaders();
    }

    public void initHeaders() throws IOException {
        String line;
        while ((line = br.readLine()).equals("")) {
            String[] header = line.split(": ");
            headers.put(header[0], header[1]);
        }
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getMethod() throws IOException {
        return requestLine.getMethod();
    }

    public String getPath() throws IOException {
        return requestLine.getUrl();
    }
}
