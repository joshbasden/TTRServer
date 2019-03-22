package Command.ServerCommand;

import Request.DrawFaceUpRequest;
import Request.DrawTrainCarCardRequest;
import Result.DrawFaceUpResult;
import Result.iResult;
import Service.DrawFaceUpService;

public class DrawFaceUpCommand implements iServerCommand {
    DrawFaceUpRequest data;

    public DrawFaceUpCommand(DrawFaceUpRequest req){
        data = req;
    }

    @Override
    public DrawFaceUpResult execute() {
        DrawFaceUpService service = new DrawFaceUpService();

        return service.drawFaceUp(data);
    }
}
