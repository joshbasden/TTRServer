package Command.ServerCommand;

import Request.DrawTrainCarCardRequest;
import Request.iRequest;
import Result.iResult;
import Service.DrawTrainCarCardService;

public class DrawTrainCarCardCommand implements iServerCommand {
    iRequest data;
    private CommandType type = CommandType.S_DRAW_FROM_TRAIN_PILE;
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

    public void setData(iRequest data) {
        this.data = data;
    }
}
