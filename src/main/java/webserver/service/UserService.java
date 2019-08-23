package webserver.service;

import db.DataBase;
import model.User;

public class UserService {
    public static void addUser(User user) {
        DataBase.addUser(user);
    }

    public static User findByUserId(User maybeUser) {
        return DataBase.findUserById(maybeUser.getUserId());
    }

    public static User login(User maybeUser) {
        User user = findByUserId(maybeUser);
        if (user == null || !user.isSamePassword(maybeUser)) {
            return null;
        }
        return user;
    }
}
