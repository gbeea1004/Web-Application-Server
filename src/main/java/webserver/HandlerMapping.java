package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.*;
import webserver.controller.user.UserListController;
import webserver.controller.user.LoginController;
import webserver.controller.user.UserCreateController;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapping.class);

    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/users/create", new UserCreateController());
        controllers.put("/users/login", new LoginController());
        controllers.put("/users/list", new UserListController());
        controllers.put("/index", new HomeController());
        controllers.put("/css", new CSSController());
        controllers.put("/js", new JSController());
        controllers.put("/font", new FontController());
        controllers.put("/icon", new IconController());
    }

    static Controller findController(String url) {
        for (String key : controllers.keySet()) {
            if (url.startsWith(key)) {
                return controllers.get(key);
            }
        }
        return new ForwardController();
    }
}
