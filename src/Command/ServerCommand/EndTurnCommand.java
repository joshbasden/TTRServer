package Command.ServerCommand;


import Request.EndTurnRequest;
import Request.iRequest;
import Result.EndTurnResult;
import Result.iResult;
import Service.EndTurnService;

public class EndTurnCommand implements iServerCommand {
    iRequest data;
    private CommandType type = CommandType.S_END_TURN;
    public EndTurnCommand(iRequest request){
        data = request;
    }

    @Override
    public EndTurnResult execute() {
        EndTurnService endTurnService = new EndTurnService();
        return endTurnService.endTurn((EndTurnRequest)data);
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
