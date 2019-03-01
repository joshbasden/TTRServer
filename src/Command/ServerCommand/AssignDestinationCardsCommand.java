package Command.ServerCommand;

import Request.AssignDestinationCardsRequest;
import Request.iRequest;
import Result.AssignDestinationCardsResult;
import Service.AssignDestCardsService;

public class AssignDestinationCardsCommand implements iServerCommand {
    private iRequest data;
    public AssignDestinationCardsCommand(iRequest request) {
        data = request;
    }
    public AssignDestinationCardsResult execute() {
        AssignDestCardsService assignDestCardsService = new AssignDestCardsService();
        return assignDestCardsService.assignDestCards((AssignDestinationCardsRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }
}