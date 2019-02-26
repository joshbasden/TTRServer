package Server;

import Command.ServerCommand.*;
import Request.*;
import Result.iResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;


/**
 * Created by jbasden on 1/31/19.
 */

public class ServerCommandHandler implements HttpHandler {
    private iRequest data;
    private iResult response;
    private String jsonStr = null;
    CommandType type;

    public iResult execute() {
        switch (type){
            case S_CREATE_GAME:
                CreateGameCommand createGameCommand = new CreateGameCommand(data);
                return createGameCommand.execute();
            case S_POLL:
                GetCommandsCommand getCommandsCommand = new GetCommandsCommand(data);
                return getCommandsCommand.execute();
            case S_LOGIN:
                LoginCommand loginCommand = new LoginCommand(data);
                return loginCommand.execute();
            case S_REGISTER:
                RegisterCommand registerCommand = new RegisterCommand(data);
                return registerCommand.execute();
            case S_JOIN_GAME:
                JoinGameCommand joinGameCommand = new JoinGameCommand(data);
                return joinGameCommand.execute();
            default:
                return null;
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                Gson gson = new Gson();

                // get the type from the header
                String header = getHeader(exchange.getRequestHeaders());
                type = getCommandType(header);

                // get the command object
                data = getCommandObject(type, reqData);
                System.out.println("a request came through");
                response = execute();
                jsonStr = gson.toJson(response);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(jsonStr, respBody);
                respBody.close();
                success = true;
            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream respBody = exchange.getResponseBody();
                respBody.close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            OutputStream respBody = exchange.getResponseBody();
            respBody.close();
            e.printStackTrace();
        }
    }
    /*
        The readString method shows how to read a String from an InputStream.
    */
    private iRequest getCommandObject(CommandType type, String reqData){
        Gson gson = new Gson();

        switch (type) {
            case S_LOGIN:
                return gson.fromJson(reqData, LoginRequest.class);
            case S_REGISTER:
                return gson.fromJson(reqData, RegisterRequest.class);
            case S_JOIN_GAME:
                return gson.fromJson(reqData, JoinGameRequest.class);
            case S_CREATE_GAME:
                return gson.fromJson(reqData, CreateGameRequest.class);
            case S_POLL:
                return gson.fromJson(reqData, GetCommandsRequest.class);
            default:
                return null;
        }
    }
    private CommandType getCommandType(String s){
        switch (s) {
            case "S_LOGIN":
                return CommandType.S_LOGIN;
            case "S_REGISTER":
                return CommandType.S_REGISTER;
            case "S_JOIN_GAME":
                return CommandType.S_JOIN_GAME;
            case "S_CREATE_GAME":
                return CommandType.S_CREATE_GAME;
            case "S_POLL":
                return CommandType.S_POLL;
            default:
                return null;
        }
    }

    private String getHeader(Headers h){
        try{
            List<String> values = h.get("type");
            return values.get(0);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}

