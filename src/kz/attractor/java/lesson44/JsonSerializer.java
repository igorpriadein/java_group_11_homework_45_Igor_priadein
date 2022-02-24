package kz.attractor.java.lesson44;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class JsonSerializer {
    public static List<UserModel.User> getUsers() throws IOException {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<UserModel.User>>(){}.getType();
        try (Reader reader = new FileReader("users.json")) {
            List<UserModel.User> users = gson.fromJson(reader, listType);
            return users;
        }
    }

    public static void writeData(List<UserModel.User> users) throws IOException{
        Gson gson = new Gson();
        try(Writer writer = new FileWriter("users.json")){
            String json = gson.toJson(users);
            writer.write(json);
        }
    }
}

