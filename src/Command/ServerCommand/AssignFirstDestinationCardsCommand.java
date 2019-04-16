package Command.ServerCommand;

import Request.AssignFirstDestinationCardsRequest;
import Request.iRequest;
import Result.AssignFirstDestinationCardsResult;
import Service.AssignFirstDestinationCardsService;

public class AssignFirstDestinationCardsCommand implements iServerCommand {
    private iRequest data;

    public AssignFirstDestinationCardsCommand(iRequest request) {
        data = request;
    }
    public AssignFirstDestinationCardsResult execute() {
        AssignFirstDestinationCardsService assignFirstDestinationCardsService = new AssignFirstDestinationCardsService();
        return assignFirstDestinationCardsService.assignFirstDestinationCards((AssignFirstDestinationCardsRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }
}