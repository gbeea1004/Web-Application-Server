package webserver;

import org.junit.Before;
import org.junit.Test;
import webserver.request.HttpRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {
    private final String httpHeader = "GET /index.html HTTP/1.1\n" +
            "Host: localhost:8080\n" +
            "Connection: krrreep-alive\n" +
            "Accept: */*\n" +
            "\n";

    private HttpRequest httpRequest;

    @Before
    public void setUp() throws Exception {
        httpRequest = new HttpRequest(new ByteArrayInputStream(httpHeader.getBytes()));
    }

    @Test
    public void getMethod() throws IOException {
        assertThat(httpRequest.getMethod()).isEqualTo("GET");
    }

    @Test
    public void getPath() throws IOException {
        assertThat(httpRequest.getPath()).isEqualTo("/index.html");
    }

    @Test
    public void getHeader() throws IOException {
        assertThat(httpRequest.getHeader("Connection")).isEqualTo("krrreep-alive");
    }
}