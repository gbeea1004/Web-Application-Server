package webserver;

import org.junit.Before;
import org.junit.Test;
import util.HttpRequestUtils;

import java.io.*;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestHandlerTest {
    private String url;

    @Before
    public void setUp() throws Exception {
        url = "/users/?userId=forever&password=123456&name=TaeSu&email=forever@naver.com";
    }

    @Test
    public void getZeroIndex() {
        int index = url.indexOf("?");
        assertThat(index).isEqualTo(7);
    }

    @Test
    public void getQueryString() {
        int index = url.indexOf("?");
        String queryString = url.substring(index + 1);
        assertThat(queryString).isEqualTo("userId=forever&password=123456&name=TaeSu&email=forever@naver.com");
    }

    @Test
    public void getUserInfoByMap() {
        int index = url.indexOf("?");
        String queryString = url.substring(index + 1);
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(queryString);
        assertThat(userInfo.get("userId")).isEqualTo("forever");
        assertThat(userInfo.get("password")).isEqualTo("123456");
        assertThat(userInfo.get("name")).isEqualTo("TaeSu");
        assertThat(userInfo.get("email")).isEqualTo("forever@naver.com");
    }

    @Test
    public void headerBlankTest() throws IOException {
        String space = null;
        String str = "abc de f \nghi \n123\n" + space;
        InputStream in = new ByteArrayInputStream(str.getBytes());

        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = bf.readLine()) != null) {
            System.out.println(line);
        }
    }
}