package Command.ServerCommand;

import Request.AssignDestinationCardsRequest;
import Request.iRequest;
import Result.AssignDestinationCardsResult;
import Service.AssignDestinationCardsService;

public class AssignDestinationCardsCommand implements iServerCommand {
    private iRequest data;
    private ServerCommandType type = ServerCommandType.S_ASSIGN_DEST;
    public AssignDestinationCardsCommand(iRequest request) {
        data = request;
    }
    public AssignDestinationCardsResult execute() {
        AssignDestinationCardsService assignDestinationCardsService = new AssignDestinationCardsService();
        return assignDestinationCardsService.assignDestinationCards((AssignDestinationCardsRequest)data);
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