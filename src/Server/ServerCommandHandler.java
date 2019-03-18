package Server;

import Command.ServerCommand.*;
import Request.*;
import Result.AssignFirstDestinationCardsResult;
import Result.DrawDestinationCardsResult;
import Result.SendMessageResult;
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
            case S_ASSIGN_DEST:
                AssignDestinationCardsCommand assignDestinationCardsCommand = new AssignDestinationCardsCommand(data);
                return assignDestinationCardsCommand.execute();
            case S_ASSIGN_FIRST_DEST:
                AssignFirstDestinationCardsCommand assignFirstDestinationCardsCommand = new AssignFirstDestinationCardsCommand(data);
                return assignFirstDestinationCardsCommand.execute();
            case S_SEND_MESSAGE:
                SendMessageCommand sendMessageCommand = new SendMessageCommand(data);
                return sendMessageCommand.execute();
            case S_DRAW_THREE_DESTINATION_CARDS_FROM_DRAW_PILE:
                DrawDestinationCardsCommand drawDestinationCardsCommand = new DrawDestinationCardsCommand(data);
                return drawDestinationCardsCommand.execute();
            case S_CLAIM_ROUTE:
                ClaimRouteCommand claimRouteCommand = new ClaimRouteCommand(data);
                return claimRouteCommand.execute();
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
                type = CommandType.valueOf(header);
                data = getCommandObject(type, reqData);
                response = execute();
                jsonStr = gson.toJson(response);
                if (!(type == CommandType.S_POLL)) {
                    System.out.println("");
                    System.out.println(String.format("A %s was received.", data));
                    System.out.println(jsonStr);
                    System.out.println("");
                }
                else {
                    System.out.print("P"); //TODO:Figure out who
                }
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(jsonStr, respBody);
                respBody.close();
                success = true;
            }

            if (!success) {
                System.out.println("Bad request came through");
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
            case S_ASSIGN_DEST:
                return gson.fromJson(reqData, AssignDestinationCardsRequest.class);
            case S_ASSIGN_FIRST_DEST:
                return gson.fromJson(reqData, AssignFirstDestinationCardsRequest.class);
            case S_SEND_MESSAGE:
                return gson.fromJson(reqData, SendMessageRequest.class);
            case S_DRAW_THREE_DESTINATION_CARDS_FROM_DRAW_PILE:
                return gson.fromJson(reqData, DrawDestinationCardsRequest.class);
            case S_CLAIM_ROUTE:
                return gson.fromJson(reqData, ClaimRouteRequest.class);
            default:
                return null;
        }
    }


    private String getHeader(Headers h){
        try {
            List<String> values = h.get("type");
            return values.get(0);
        }
        catch (Exception e) {
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


    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}

