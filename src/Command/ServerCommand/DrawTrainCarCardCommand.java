package Command.ServerCommand;

import Request.DrawTrainCarCardRequest;
import Request.iRequest;
import Result.iResult;
import Service.DrawTrainCarCardService;

public class DrawTrainCarCardCommand implements iServerCommand {
    iRequest data;

    public DrawTrainCarCardCommand(iRequest req){
        data = req;
    }

    @Override
    public iResult execute() {
        DrawTrainCarCardService drawTrainCarCardService = new DrawTrainCarCardService();

        return drawTrainCarCardService.getTopTrainCarCard((DrawTrainCarCardRequest)data);
    }

    public iRequest getData() {
        return data;
    }
}
