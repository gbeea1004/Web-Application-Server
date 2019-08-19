package webserver.response;

import org.junit.Test;

import java.io.*;

public class HttpResponseTest {
    private HttpResponse httpResponse;
    private String testDirectory = "./src/test/resources/";


    @Test
    public void redirectTest() throws FileNotFoundException {
        httpResponse = HttpResponseGenerator.createHttpResponse(createOutputStream("Http_Redirect.txt"));
        httpResponse.sendRedirect("/index.html");
    }

    @Test
    public void forwardTest() throws FileNotFoundException {
        httpResponse = HttpResponseGenerator.createHttpResponse(createOutputStream("Http_Forward.txt"));
        httpResponse.forward("/index.html");
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }

}
