package webserver.controller.user;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.controller.AbstractController;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

public class UserListController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            log.debug("유저 리스트 doGet");
            BufferedReader br = new BufferedReader(new FileReader("./webapp/user/list.html"));
            Map<String,String> cookie = HttpRequestUtils.parseCookies(httpRequest.getHeader("Cookie"));
            if (cookie.get("logined") != null && cookie.get("logined").equals("true")) {
                String line = "<tr><th scope=\"row\">{{number}}</th> <td>{{userId}}</td> <td>{{name}}</td> <td>{{email}}</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>";
                StringBuilder sb = new StringBuilder();
                String fileLine;
                int no = 0;

                while ((fileLine = br.readLine()) != null) {
                    if (fileLine.contains("{{#list}}")) {
                        log.debug("list 들어옴");
                        for (User user : DataBase.findAll()) {
                            sb.append(line.replace("{{number}}", String.valueOf(++no))
                                    .replace("{{userId}}", user.getUserId())
                                    .replace("{{name}}", user.getName())
                                    .replace("{{email}}", user.getEmail()));
                        }
                    } else {
                        sb.append(fileLine);
                    }
                }
                httpResponse.addHeader(CONTENT_TYPE, HTML_CONTENT_TYPE);
                httpResponse.forwardBody(sb.toString());
            } else {
                httpResponse.sendRedirect("/user/login.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
