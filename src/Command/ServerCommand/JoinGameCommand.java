package Command.ServerCommand;

import Request.JoinGameRequest;
import Request.iRequest;
import Result.JoinGameResult;
import Service.JoinGameService;

/**
 * Created by jbasden on 1/29/19.
 */

public class JoinGameCommand implements iServerCommand {
    private iRequest data;
    public JoinGameCommand(iRequest request) {
        data = request;
    }
    @Override
    public JoinGameResult execute() {
        JoinGameService joinGameService = new JoinGameService();
        return joinGameService.joinGame((JoinGameRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }
}
