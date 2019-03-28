package Command.ServerCommand;

import Request.ClaimGrayRequest;
import Request.iRequest;
import Result.ClaimGrayResult;
import Result.iResult;
import Service.ClaimGrayService;

public class ClaimGrayCommand implements iServerCommand {
    private iRequest data;

    public ClaimGrayCommand(iRequest req){
        data = req;
    }

    @Override
    public ClaimGrayResult execute() {
        ClaimGrayService claimGrayService = new ClaimGrayService();
        return claimGrayService.claimGray((ClaimGrayRequest) data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(iRequest data) {
        this.data = data;
    }
}