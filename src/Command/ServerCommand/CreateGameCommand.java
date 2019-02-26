package Command.ServerCommand;

import Request.CreateGameRequest;
import Request.iRequest;
import Result.CreateGameResult;
import Service.CreateGameService;

/**
 * Created by jbasden on 1/29/19.
 */

public class CreateGameCommand implements iServerCommand {
    private iRequest data;
    public CreateGameCommand(iRequest request) {
        data = request;
    }
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
}
