package Command.ServerCommand;

import Request.CreateGameRequest;
import Request.iRequest;
import Result.CreateGameResult;
import Service.CreateGameService;

public class CreateGameCommand implements iServerCommand {
    private iRequest data;
    private ServerCommandType type = ServerCommandType.S_CREATE_GAME;
    public CreateGameCommand(iRequest request) {
        data = request;
    }

    @Override
    public CreateGameResult execute() {
        CreateGameService createGameService = new CreateGameService();
        return createGameService.createGame((CreateGameRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }

    @Override
    public String getType() {
        return type.name();
    }
}
