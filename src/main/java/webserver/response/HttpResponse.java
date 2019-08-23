package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private StatusLine statusLine;
    private ResponseHeader responseHeader;
    private ResponseBody responseBody;
    private DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
        responseHeader = new ResponseHeader();
    }

    private void addStatusLine(String httpVersion, HttpStatusCode statusCode) {
        statusLine = new StatusLine(httpVersion, statusCode);
    }

    private void addResponseBody(String url) {
        try {
            this.responseBody = new ResponseBody(Files.readAllBytes(Paths.get("./webapp" + url)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addResponseDynamicBody(String dynamicResource) {
        this.responseBody = new ResponseBody(dynamicResource.getBytes());
    }

    public void sendRedirect(String url) {
        addStatusLine(HttpResponseUtils.HTTP_VERSION_1_1, HttpStatusCode.FOUND);
        addHeader("Location", url);
        writeResponseMessage();
    }

    private void writeResponseMessage() {
        try {
            statusLine.addWriteStatusLine(dos);
            responseHeader.addWriteHeader(dos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forward(String filePath) {
        addStatusLine(HttpResponseUtils.HTTP_VERSION_1_1, HttpStatusCode.OK);
        log.debug("filePath : {}", filePath);
        addResponseBody(filePath);

        addHeader("Content-Length", responseBody.getBodyLength());
        writeResponseMessage();
        responseBody.responseBody(dos);
    }

    public void forwardBody(String dynamicResource) {
        addStatusLine(HttpResponseUtils.HTTP_VERSION_1_1, HttpStatusCode.OK);
        addResponseDynamicBody(dynamicResource);
        addHeader("Content-Length", responseBody.getBodyLength());
        writeResponseMessage();
        responseBody.responseBody(dos);
    }

    public void addHeader(String header, String value) {
        responseHeader.addHeader(header, value);
    }
}
