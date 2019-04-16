package Command.ServerCommand;

import Request.ClaimRouteRequest;
import Request.iRequest;
import Result.ClaimRouteResult;
import Service.ClaimRouteService;

public class ClaimRouteCommand implements iServerCommand {
    private iRequest data;

    public ClaimRouteCommand(iRequest request) {
        data = request;
    }

    @Override
    public ClaimRouteResult execute() {
        ClaimRouteService claimRouteService = new ClaimRouteService();
        return claimRouteService.claimRoute((ClaimRouteRequest)data);
    }

    public iRequest getData() {
        return data;
    }

    public void setData(ClaimRouteRequest data) {
        this.data = data;
    }
}