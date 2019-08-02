package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("NewClientConnect!ConnectedIP:{},Port:{}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String url = br.readLine().split(" ")[1];

            //get 방식
            if (url.startsWith("/users/?")) {
                String queryString = url.split("\\?")[1];
                User user = createUser(queryString);
                log.debug(user.toString());
            }
            //post 방식
            else if (url.startsWith("/users")) {
                int contentLen = 0;
                while (!(url = br.readLine()).equals("")) {
                    if (url.startsWith("Content-Length")) {
                        contentLen = Integer.parseInt(url.split(" ")[1]);
                    }
                    log.debug("url : {}", url);
                }
                String body = IOUtils.readData(br, contentLen);
                log.debug("body : {}", body);
                User user = createUser(body);
                log.debug(user.toString());
            }


            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(Paths.get("./webapp" + url));
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private User createUser(String queryString) {
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(queryString);
        return new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
