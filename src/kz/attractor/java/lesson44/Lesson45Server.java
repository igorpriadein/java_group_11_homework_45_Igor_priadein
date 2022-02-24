package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.server.ContentType;
import kz.attractor.java.server.ResponseCodes;
import kz.attractor.java.server.RouteHandler;
import kz.attractor.java.server.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lesson45Server extends Lesson44Server{


    private List<UserModel> users = new ArrayList<>();

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/login",this::loginGet);
        registerPost("/login",this::loginPost);
        registerGet("/register", this::registrationGet);
        registerPost("/register", this::registrationPost);
        registerGet("/register_fail", this::registrationFailedPost);
        registerGet("/profile", this::profileGet);
    }
    private void loginGet(HttpExchange exchange){
        Path path = makeFilePath("login.html");
        sendFile(exchange,path, ContentType.TEXT_HTML);
    }

    private void registrationGet(HttpExchange exchange){
        Path path = makeFilePath("register.html");
        sendFile(exchange,path, ContentType.TEXT_HTML);
    }

    private void registrationFailedPost(HttpExchange exchange){
        Path path = makeFilePath("register_fail.html");
        sendFile(exchange,path, ContentType.TEXT_HTML);
    }

    private void profileGet(HttpExchange exchange){
        Path path = makeFilePath("profile.html");
        sendFile(exchange,path, ContentType.TEXT_HTML);
    }

    protected void registerPost(String route, RouteHandler handler){
        getRoutes().put("POST " + route,handler);
    }

    private void loginPost(HttpExchange exchange){
        String raw = getBody(exchange);
        Map<String,String> parsed = Utils.parseUrlEncoded(raw,"&");
        String path = "/login";
        if(checkEmailAndPassWord(parsed.get("email"), parsed.get("user-password"))){
            var user = getUserModel(parsed.get("email"), parsed.get("user-password"));
            renderTemplate(exchange, "profile.html", user);
            path = "/profile";
        }
        try {
            redirect303(exchange, path);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void registrationPost(HttpExchange exchange){
        String raw = getBody(exchange);
        Map<String,String> parsed = Utils.parseUrlEncoded(raw,"&");
        String path = "/sample";
        if(checkUserEmail(parsed.get("email"))){
            path = "/register_fail";
        }
        users.add(new UserModel(parsed.get("email"), parsed.get("user-name"), parsed.get("user-password")));
        JsonSerializer.writeData(users);
        try {
            redirect303(exchange, path);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String getContentType(HttpExchange exchange) {
        return exchange.getRequestHeaders()
                .getOrDefault("Content-type", List.of(""))
                .get(0);
    }
    protected String getBody(HttpExchange exchange){
        InputStream input = exchange.getRequestBody();
        Charset utf8 = StandardCharsets.UTF_8;
        InputStreamReader isr = new InputStreamReader(input,utf8);
        try(BufferedReader reader = new BufferedReader(isr)){
            return reader.lines().collect(Collectors.joining(""));
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    protected void redirect303(HttpExchange exchange,String path){
        try{
            exchange.getResponseHeaders().add("Location",path);
            exchange.sendResponseHeaders(303,0);
            exchange.getResponseBody().close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private boolean checkUserEmail(String email){
            List<UserModel> users = null;
        try{
            users = JsonSerializer.getUsers();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        for(UserModel u : users){
            if(u.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }

    private boolean checkEmailAndPassWord(String email, String password){
        List<UserModel> users = null;
        try{
            users = JsonSerializer.getUsers();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        for(UserModel u : users){
            if(u.getEmail().equals(email) && u.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    private UserModel getUserModel(String email, String password){
        List<UserModel> users = null;
        try{
            users = JsonSerializer.getUsers();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return users.stream()
                .filter(u -> (u.getEmail().equals(email) && (u.getPassword().equals(password))))
                .findFirst()
                .orElse(null);

    }

}
