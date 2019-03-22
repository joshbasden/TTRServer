package Command.ServerCommand;

import Request.DrawFaceUpRequest;
import Request.DrawTrainCarCardRequest;
import Result.DrawFaceUpResult;
import Result.iResult;
import Service.DrawFaceUpService;

public class DrawFaceUpCommand implements iServerCommand {
    DrawFaceUpRequest request;

    public DrawFaceUpCommand(DrawFaceUpRequest data){
        request = data;
    }

    @Override
    public DrawFaceUpResult execute() {
        DrawFaceUpService service = new DrawFaceUpService();

        return service.drawFaceUp(request);
    }
}
