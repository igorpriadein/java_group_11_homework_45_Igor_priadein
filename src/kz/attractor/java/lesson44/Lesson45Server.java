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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lesson45Server extends Lesson44Server{

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/login",this::loginGet);
        registerPost("/login",this::loginPost);

    }
    private void loginGet(HttpExchange exchange){
        Path path = makeFilePath("login.html");
        sendFile(exchange,path, ContentType.TEXT_HTML);
    }
    protected void registerPost(String route, RouteHandler handler){
        getRoutes().put("POST " + route,handler);
    }

    private void loginPost(HttpExchange exchange){
        String cType = getContentType(exchange);
        String raw = getBody(exchange);
        Map<String,String> parsed = Utils.parseUrlEncoded(raw,"&");
        String fmt = "Необработанные данные:%s\n"
                +"Content-type:%s\n"
                +"После обработки:%s";
        String data = String.format(fmt,raw,cType,parsed);
        try {
            redirect303(exchange, "/sample");
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

    private void registerPost(HttpExchange exchange){
        String cType = getContentType(exchange);
        String raw = getBody(exchange);
        Map<String,String> parsed = Utils.parseUrlEncoded(raw,"&");
        String fmt = "Необработанные данные:%s\n"
                +"Content-type:%s\n"
                +"После обработки:%s";
        String data = String.format(fmt,raw,cType,parsed);
        try {
            redirect303(exchange, "/sample");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
