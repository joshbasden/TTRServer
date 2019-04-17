package Command.ServerCommand;

import Request.GetCommandsRequest;
import Request.iRequest;
import Result.GetCommandsResult;
import Service.GetCommandsService;

/**
 * Created by jbasden on 1/29/19.
 */

public class GetCommandsCommand implements iServerCommand {
    private iRequest data;
    private ServerCommandType type = ServerCommandType.S_POLL;
    public GetCommandsCommand(iRequest request) {
        data = request;
    }

    @Override
    public GetCommandsResult execute() {
        GetCommandsService getCommandsService = new GetCommandsService();
        return getCommandsService.getCommands((GetCommandsRequest)data);
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
