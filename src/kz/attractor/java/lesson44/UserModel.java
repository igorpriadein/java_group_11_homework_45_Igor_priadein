package kz.attractor.java.lesson44;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserModel {
        private String email;
        private String name;
        private String password;

        public UserModel(String email, String name, String password) {
            this.email = email;
            this.name = name;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

