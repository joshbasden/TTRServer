package Command.ServerCommand;

import Request.DrawDestinationCardsRequest;
import Request.iRequest;
import Result.DrawDestinationCardsResult;
import Service.DrawDestinationCardsService;

public class DrawDestinationCardsCommand implements iServerCommand {
    private iRequest data;
    public DrawDestinationCardsCommand(iRequest request) {
        data = request;
    }
    public DrawDestinationCardsResult execute() {
        DrawDestinationCardsService drawDestinationCardsService = new DrawDestinationCardsService();
        return drawDestinationCardsService.drawDestinationCards((DrawDestinationCardsRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }
}
