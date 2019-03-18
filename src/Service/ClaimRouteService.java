package Service;

import Model.Model;
import Request.ClaimRouteRequest;
import Result.ClaimRouteResult;

public class ClaimRouteService {
    Model model = Model.getInstance();
    public ClaimRouteResult claimRoute(ClaimRouteRequest req) {
        return model.claimRoute(req);
    }
}