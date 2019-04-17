package Command.ServerCommand;

import Request.DrawFaceUpRequest;
import Request.iRequest;
import Result.DrawFaceUpResult;
import Service.DrawFaceUpService;

public class DrawFaceUpCommand implements iServerCommand {
    iRequest data;
    private ServerCommandType type = ServerCommandType.S_DRAW_FACE_UP_TRAIN_CAR_CARD;
    public DrawFaceUpCommand(iRequest req){
        data = req;
    }

    @Override
    public DrawFaceUpResult execute() {
        DrawFaceUpService service = new DrawFaceUpService();
        return service.drawFaceUp((DrawFaceUpRequest)data);
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
