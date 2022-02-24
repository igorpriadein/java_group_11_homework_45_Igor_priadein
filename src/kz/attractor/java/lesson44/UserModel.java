package kz.attractor.java.lesson44;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

    List<User> userList = new ArrayList<>();

    public UserModel(String email, String name, String password) {
        userList.add(new User(email, name, password));
        try {
            JsonSerializer.writeData(userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class User {
        private String email;
        private String name;
        private String password;

        public User(String email, String name, String password) {
            this.email = email;
            this.name = name;
            this.password = password;
        }
    }

    public List<User> getUserList() {
        return userList;
    }
}
