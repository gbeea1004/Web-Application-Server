package webserver.controller.user;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.controller.AbstractController;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.service.UserService;

public class LoginController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        User maybeUser = HttpRequestUtils.createUser(httpRequest.getBody());        // 사용자가 입력한 id, password를 토대로 유저객체를 만듦
        User user = UserService.login(maybeUser);
        if (user == null) {
            httpResponse.addHeader(CONTENT_TYPE, HTML_CONTENT_TYPE);
            httpResponse.addHeader("Set-Cookie", "logined=false;Path=/");
            httpResponse.forward("/user/login_failed.html");
        } else {
            httpResponse.addHeader("Set-Cookie", "logined=true;Path=/");
            httpResponse.sendRedirect("/index.html");
        }
    }
}
