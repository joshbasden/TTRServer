package Command.ServerCommand;

import Request.ClaimRouteRequest;
import Request.iRequest;
import Result.ClaimRouteResult;
import Service.ClaimRouteService;

public class ClaimRouteCommand implements iServerCommand {
    private iRequest data;
    private CommandType type = CommandType.S_CLAIM_ROUTE;
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

    @Override
    public String getType() {
        return type.name();
    }
}