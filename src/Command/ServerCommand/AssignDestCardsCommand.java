package Command.ServerCommand;

import Request.AssignDestCardsRequest;
import Request.iRequest;
import Result.AssignDestCardsResult;
import Service.AssignDestCardsService;

public class AssignDestCardsCommand implements iServerCommand {
    private iRequest data;
    public AssignDestCardsCommand(iRequest request) {
        data = request;
    }
    public AssignDestCardsResult execute() {
        AssignDestCardsService assignDestCardsService = new AssignDestCardsService();
        return assignDestCardsService.assignDestCards((AssignDestCardsRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }
}