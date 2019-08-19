package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestGenerator;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseGenerator;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
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

            HttpRequest httpRequest = HttpRequestGenerator.createHttpRequest(in);
            HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(out);
            log.debug("requestUrl : {}", httpRequest.getPath());
            Dispatcher dispatcher = new Dispatcher(httpRequest, httpResponse);
            dispatcher.dispatch();

//            String url = httpRequest.getPath();
//
//            if (url.startsWith("/css")) {
//                DataOutputStream dos = new DataOutputStream(out);
//                byte[] contents = Files.readAllBytes(Paths.get("./webapp" + url));
//                response200HeaderWithCss(dos, contents.length);
//                responseBody(dos, contents);
//            }
//
//            //get 방식
////            else if (url.startsWith("/users/?")) {
////                String queryString = url.split("\\?")[1];
////                User user = createUser(queryString);
////                log.debug(user.toString());
////            }
//            //post 방식
//            else if (url.startsWith("/users/create")) {
//                int contentLen = 0;
//                String sentence;
//                while (!(sentence = br.readLine()).equals("")) {
//                    // TODO : GET, POST 구분하기
//                    if (sentence.startsWith("Content-Length")) {
//                        contentLen = Integer.parseInt(sentence.split(" ")[1]);
//                    }
//                    log.debug("url : {}", url);
//                }
//                String body = IOUtils.readData(br, contentLen);
//                log.debug("body : {}", body);
//                User user = createUser(body);
//                DataBase.addUser(user);
//                log.debug(user.toString());
//
//                DataOutputStream dos = new DataOutputStream(out);
//                response302Header(dos, "/index.html");
//
//            } else if (url.startsWith("/users/login")) {
//                int contentLen = 0;
//                String sentence;
//                while (!(sentence = br.readLine()).equals("")) {
//                    if (sentence.startsWith("Content-Length")) {
//                        contentLen = Integer.parseInt(sentence.split(" ")[1]);
//                    }
//                    log.debug("url : {}", url);
//                }
//                String body = IOUtils.readData(br, contentLen);
//                log.debug("body : {}", body);
//                Map<String, String> loginInfo = HttpRequestUtils.parseQueryString(body);
//                User maybeUser = DataBase.findUserById(loginInfo.get("userId"));
//                DataOutputStream dos = new DataOutputStream(out);
//                log.debug("maybeUser :{}", maybeUser);
//
//                if (maybeUser == null || !maybeUser.isSamePassword(loginInfo.get("password"))) {
//                    byte[] contents = Files.readAllBytes(Paths.get("./webapp/user/login_failed.html"));
//                    response401Header(dos, contents.length);
//                    responseBody(dos, contents);
//                } else {
//                    response302HeaderWithCookie(dos, "/index.html", "true");
//                }
//
//            } else if (url.startsWith("/users/list")) {
//                Map<String, String> cookies = new HashMap<>();
//                String sentence;
//
//                while (!(sentence = br.readLine()).equals("")) {
//                    if (sentence.startsWith("Cookie")) {
//                        cookies = HttpRequestUtils.parseCookies(sentence.substring(7));
//                        break;
//                    }
//                }
//
//                for (String s : cookies.keySet()) {
//                    log.debug("키 : {}", s);
//                    log.debug("값 : {}", cookies.get(s));
//                }
//
//                // 쿠키 true 면 리스트 보여주는 페이지
//                if (cookies.get("logined") != null && cookies.get("logined").equals("true")) {
//                    BufferedReader fr = new BufferedReader(new FileReader("./webapp/user/list.html"));
////                    List<User> users = new ArrayList<>(DataBase.findAll());
//
//                    String line = "<tr><th scope=\"row\">{{number}}</th> <td>{{userId}}</td> <td>{{name}}</td> <td>{{email}}</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>";
//                    int userNo = 0;
//                    StringBuilder sb = new StringBuilder();
//
//                    String fileLine;
//                    while ((fileLine = fr.readLine()) != null) {
//                        if (fileLine.contains("{{#list}}")) {
//                            for (User user : DataBase.findAll()) {
//                                System.out.println("유저임 : " + user.toString());
//                                sb.append(line.replace("{{number}}", String.valueOf(++userNo))
//                                        .replace("{{userId}}", user.getUserId())
//                                        .replace("{{name}}", user.getName())
//                                        .replace("{{email}}", user.getEmail()));
//                            }
//                        } else {
//                            sb.append(fileLine);
//                        }
//                    }
//                    DataOutputStream dos = new DataOutputStream(out);
//                    response200Header(dos, sb.toString().getBytes().length);
//                    responseBody(dos, sb.toString().getBytes());
//                } else {
//                    // 아니면 로그인 페이지
//                    DataOutputStream dos = new DataOutputStream(out);
//                    byte[] contents = Files.readAllBytes(Paths.get("./webapp/user/login.html"));
//                    response401Header(dos, contents.length);
//                    responseBody(dos, contents);
//                }
//
//
//            } else {
//                DataOutputStream dos = new DataOutputStream(out);
//                byte[] body = Files.readAllBytes(Paths.get("./webapp" + url));
//                response200Header(dos, body.length);
//                responseBody(dos, body);
//            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200HeaderWithCss(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css; charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response401Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 401 Unauthorized \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Set-Cookie: logined = false; Path = / \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String url, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("Set-Cookie: logined = " + cookie + "; Path = / \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
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
